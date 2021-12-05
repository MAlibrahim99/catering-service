package catering.user.admin;

import catering.order.CateringOrder;
import catering.order.CateringOrderRepository;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.salespointframework.core.Currencies.EURO;

@Controller
//@PreAuthorize(value="hasRole('ADMIN')")
public class IncomeOverviewController {
	CateringOrderRepository orderRepository;
	private LocalDateTime currentDate = LocalDateTime.now();

	public IncomeOverviewController(CateringOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	// Initial wird eine Übersicht der letzten 30 Tage zurückgegeben, exeklusive des aktuellen Tages
	@GetMapping("/income-overview")
	public String displayIncomeOverview(@RequestParam("start") Optional<LocalDateTime> start,
										@RequestParam("end") Optional<LocalDateTime> end, Model model) {
		final long PAST_DAYS = 30L;

		if (start.isPresent() && end.isPresent()) {
			System.out.println(orderRepository.findByCompletionDateBetween(start.get(), end.get().minusDays()).toList().size());
			model.addAttribute("startDate", start);
			model.addAttribute("endDate", end);
		}else{
			System.out.println(orderRepository.findByCompletionDateBetween(currentDate.minusDays(PAST_DAYS) ,
					currentDate).toList().size());
		}
		return "income-overview";
	}

}
