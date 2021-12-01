package catering.order;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

public class OrderController {
	
	@GetMapping("/income-overview-month")
	@PreAuthorize(value="hasRole('ADMIN')")
	public String showIncomeOverviewMonth(){
		return "income-overview-month";
	}
	
	@GetMapping("/income-overview-year")
	@PreAuthorize(value="hasRole('ADMIN')")
	public String showIncomeOverviewYear(){
		return "income-overview-year";
	}
}
