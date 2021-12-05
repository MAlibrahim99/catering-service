package catering.user.admin;

import catering.Catering;
import catering.order.CateringOrder;
import catering.order.CateringOrderRepository;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.salespointframework.core.Currencies.EURO;

@Component
public class IncomeOverview {

	private CateringOrderRepository orderRepository;

	public IncomeOverview(CateringOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Money totalIncome(LocalDateTime start, LocalDateTime end) {
		validateArguments(start, end);
		// prüfe, ob das Startsdatum bzw. Endsdatum  in der Zukunft liegt oder der aktuelle tag ist -> berechnung geht bis dem Vortag
		if (end.isAfter(LocalDateTime.now()) || end.isEqual(LocalDateTime.now())) {
			end = LocalDateTime.now().minusDays(1L);
		}
		if (start.isAfter(LocalDateTime.now()) || start.isEqual(LocalDateTime.now())) {
			start = LocalDateTime.now().minusDays(30L);
		}

		Streamable<CateringOrder> ordersDone = orderRepository.findByCompletionDateBetween(start, end);

		Money totalIncome = Money.of(0.0, EURO);

		for (CateringOrder order : ordersDone) {
			totalIncome.add(order.getTotal());
		}
		return totalIncome;
	}

	public Map<String, BigDecimal> servicePercentages(LocalDateTime start, LocalDateTime end) {
		validateArguments(start, end);
		if (end.isAfter(LocalDateTime.now()) || end.isEqual(LocalDateTime.now())) {
			end = LocalDateTime.now().minusDays(1L);
		}
		if (start.isAfter(LocalDateTime.now()) || start.isEqual(LocalDateTime.now())) {
			start = LocalDateTime.now().minusDays(30L);
		}

		List<ServiceType> serviceTypes = Arrays.asList(ServiceType.values());
		BigDecimal percent = BigDecimal.valueOf(0.00);
		Streamable<CateringOrder> orders;
		Map<String, BigDecimal> percentages = new HashMap<String, BigDecimal>();
		for (ServiceType type : serviceTypes) {
			orders = orderRepository.findByCompletionDateBetween(start, end);
			for(CateringOrder order : orders){

				if(order.)
				percent
			}

			percentages.put()

		}
	}

	private void validateArguments(LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Date can not be null in this call");
		}
		if (start.isAfter(end)) {
			throw new IllegalArgumentException("Start date is after the end date");
		}
	}
}
