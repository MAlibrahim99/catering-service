package catering.order;

import org.javamoney.moneta.Money;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderStatus;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.salespointframework.core.Currencies.EURO;

@Component
public class IncomeOverview {

	private CateringOrderRepository orderRepository;

	public IncomeOverview(CateringOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Money totalIncome(LocalDate start, LocalDate end) {
		validateArguments(start, end);
		// prüfe, ob das Startsdatum bzw. Endsdatum  in der Zukunft liegt oder der aktuelle tag ist ->
		// berechnung geht bis dem Vortag
		if (end.isAfter(LocalDate.now()) || end.isEqual(LocalDate.now())) {
			end = LocalDate.now().minusDays(1L);
		}
		if (start.isAfter(end) || start.isEqual(end)) {
			start = end.minusDays(30L);
		}
		Streamable<CateringOrder> ordersDone = orderRepository.findByCompletionDateBetween(start, end);

		Money totalIncome = Money.of(0.0, EURO);

		for (CateringOrder order : ordersDone) {
			totalIncome = totalIncome.add(order.getTotal());
		}
//		totalIncome = totalIncome.add(Money.of(100000.99, EURO)); // zu prüfen ob ich den gewünschten Wert bekomme
		return totalIncome;
	}

//	public Map<String, String> servicePercentages(LocalDate start, LocalDate end) {
//		validateArguments(start, end);
//		if (end.isAfter(LocalDate.now()) || end.isEqual(LocalDate.now())) {
//			end = LocalDate.now().minusDays(1L);
//		}
//
//		if (start.isAfter(LocalDate.now()) || start.isEqual(LocalDate.now())) {
//			start = LocalDate.now().minusDays(30L);
//		}
//
//		Streamable<CateringOrder> orders = orderRepository.findByCompletionDateBetween(start, end);
//		BigDecimal countOfAllOrders = BigDecimal.valueOf(orders.toList().size());
//		BigDecimal countOfSameTypeOrders = BigDecimal.valueOf(0.00);
//		Map<String, String> percentages = new HashMap<>();
//
//			for (CateringOrder order : orders) {
//
//				percentages.put(type.toString().toLowerCase(),
//						countOfSameTypeOrders.divide(countOfAllOrders, RoundingMode.CEILING).multiply(BigDecimal.valueOf(100)));
//				countOfSameTypeOrders = BigDecimal.valueOf(0.00);
//			}
//		return percentages;
//}

	public Map<String, String> statusPercentages(LocalDate start, LocalDate end) {
		validateArguments(start, end);
		if (start.isAfter(end) || start.isEqual(end)) {
			start = end.minusDays(30L);
		}

		Map<String, String> percentages = new HashMap<>();
		BigDecimal allOrders = new BigDecimal(orderRepository.countByCompletionDateBetween(start, end));

		if (allOrders.equals(BigDecimal.valueOf(0))) {
			percentages.put("PAID", "0.0");
			percentages.put("COMPLETED", "0.0");
			percentages.put("CANCELLED", "0.0");
		}else {
			BigDecimal orderCount;
			for (OrderStatus status : Arrays.asList(OrderStatus.PAID, OrderStatus.COMPLETED, OrderStatus.CANCELLED)) {
				orderCount = new BigDecimal(orderRepository.countByOrderStatusAndCompletionDateBetween(status, start, end));
				// prozent berchnen und runden
				orderCount = orderCount.multiply(new BigDecimal("100.0")).divide(allOrders, RoundingMode.HALF_DOWN);
				percentages.put(status.name(), orderCount.toString());
			}
		}
		return percentages;
	}

	private void validateArguments(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Date can not be null in this call");
		}
		if (start.isAfter(end)) {
			throw new IllegalArgumentException("Start date is after the end date");
		}
	}
}
