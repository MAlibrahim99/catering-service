package catering.user.admin;

import catering.order.CateringOrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@PreAuthorize(value="hasRole('ADMIN')")
public class IncomeOverviewController {

	CateringOrderRepository orderRepository;
	private LocalDateTime currentDate = LocalDateTime.now();
	private IncomeOverview incomeOverview;


	public IncomeOverviewController(CateringOrderRepository orderRepository, IncomeOverview incomeOverview) {
		this.orderRepository = orderRepository;
		this.incomeOverview = incomeOverview;
	}

	// Initial wird eine Übersicht der letzten 30 Tage zurückgegeben, exeklusive des aktuellen Tages
	@GetMapping("/income-overview")
	public String displayIncomeOverview(@RequestParam("startDate") Optional<String> startDate,
										@RequestParam("endDate") Optional<String> endDate, Model model) {
		LocalDateTime start;
		LocalDateTime end;
		if (startDate.isEmpty() || endDate.isEmpty()) {
			start = LocalDateTime.now().minusDays(30L);
			end = LocalDateTime.now().minusDays(1L);
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			start = LocalDate.parse(startDate.get(), formatter).atStartOfDay();
			end = LocalDate.parse(endDate.get(), formatter).atStartOfDay();
		}

		if (start.isAfter(end)) {
			start = end.minusDays(30L);
		}

		model.addAttribute("totalIncome", incomeOverview.totalIncome(start, end));
		model.addAttribute("statusPercentages", incomeOverview.statusPercentages(start, end));
		model.addAttribute("start", start.toLocalDate());
		model.addAttribute("end", end.toLocalDate());

		return "income-overview";
	}

}
