package catering.order;

import org.javamoney.moneta.Money;
import org.salespointframework.order.OrderStatus;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Helper class to calculate income parameters like total income and service percentages according to service type
 * or service status.
 */
@Component
public class IncomeOverview {

	private CateringOrderRepository orderRepository;

	public IncomeOverview(CateringOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	/**
	 * Calculates the total of orders booked in a period specified with {@link LocalDate} start and {@link LocalDate}end.
	 * If the start date after the end date, the default start will be set to 30 Days before end date.
	 * The calculated value only includes orders placed up to the day before the current day.
	 * @param start {@link LocalDate}
	 * @param end {@link LocalDate}
	 * @return {@link Money} as sum of all paid orders in the specified interval
	 * @throws IllegalArgumentException if start or end date has null value or start date is after end date.
	 */
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
			if(order.getOrderStatus().equals(OrderStatus.CANCELLED)){
				continue;
			}
			totalIncome = totalIncome.add(order.getTotal());
		}
//		totalIncome = totalIncome.add(Money.of(100000.99, EURO)); // zu prüfen ob ich den gewünschten Wert bekomme
		return totalIncome;
	}


	/**
	 * Calculates service percentages of orders booked in a period specified with {@link LocalDate} start
	 * and {@link LocalDate}end.
	 * @param start {@link LocalDate}
	 * @param end {@link LocalDate}
	 * @return {@link Map} contains service type as key and service percentage as value. If there are no orders
	 * the percentages will be set to zero.
	 * @throws IllegalArgumentException if start or end date has null value or start date is after end date.
	 */
	public Map<String, BigDecimal> servicePercentages(LocalDate start, LocalDate end) {
		validateArguments(start, end);

		if (start.isAfter(end) || start.isEqual(end)) {
			start = end.minusDays(30L);
		}

		List<CateringOrder> orders = orderRepository.findByCompletionDateBetween(start, end).toList();
		Map<String, BigDecimal> orderPercentages = new HashMap<>();
		List<String> services = List.of("Eventcatering", "MobileBreakfast", "Partyservice", "RentACook");

		int eventCatering = (int) orders.stream().filter(order -> order.getService().equals("Eventcatering")).count();
		int mobileBreakfast = (int) orders.stream().filter(order -> order.getService().equals("MobileBreakfast")).count();
		int partyService = (int) orders.stream().filter(order -> order.getService().equals("Partyservice")).count();
		int rentACook = (int) orders.stream().filter(order -> order.getService().equals("RentACook")).count();

		if(orders.isEmpty()){
			for(String service: services) {
				orderPercentages.put(service, BigDecimal.ZERO);
			}
		}else{
			BigDecimal orderCount = BigDecimal.valueOf(orders.size());

//			berechne Prozentsatz
			BigDecimal percentage = BigDecimal.valueOf(eventCatering).multiply(BigDecimal.valueOf(100.0))
					.divide(orderCount, RoundingMode.HALF_UP);
			orderPercentages.put("Eventcatering", percentage);

			percentage = BigDecimal.valueOf(mobileBreakfast).multiply(BigDecimal.valueOf(100.0))
					.divide(orderCount, RoundingMode.HALF_UP);
			orderPercentages.put("MobileBreakfast", percentage);

			percentage = BigDecimal.valueOf(partyService).multiply(BigDecimal.valueOf(100.0))
					.divide(orderCount, RoundingMode.HALF_UP);
			orderPercentages.put("Partyservice", percentage);

			percentage = BigDecimal.valueOf(rentACook).multiply(BigDecimal.valueOf(100.0))
					.divide(orderCount, RoundingMode.HALF_UP);
			orderPercentages.put("RentACook", percentage);
		}
		return orderPercentages;
	}

	/**
	 * Calculates status percentages of orders booked in a period specified with {@link LocalDate} start
	 * and {@link LocalDate}end.
	 * @param start {@link LocalDate}
	 * @param end {@link LocalDate}
	 * @return {@link Map} contains order status as key and percentage as value. If there are no orders
	 * the percentages will be set to "0.0".
	 * @throws IllegalArgumentException if start or end date has null value or start date is after end date.
	 */
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
