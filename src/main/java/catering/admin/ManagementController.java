package catering.admin;

import catering.order.CateringOrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

@Controller
//@PreAuthorize(value="hasRole('ADMIN')")
public class ManagementController {
	CateringOrderRepository orderRepository;
	@ModelAttribute("startDate")
	private Temporal startDate(){
		return (Temporal) LocalDateTime.now().minusDays(30L);
	}

	@ModelAttribute("endDate")
	private Temporal endDate(){
		return (Temporal) LocalDateTime.now();
	}

	public ManagementController(CateringOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/income-overview")
	public String displayIncomeOverview(Model model){
		return "income-overview";
	}

}
