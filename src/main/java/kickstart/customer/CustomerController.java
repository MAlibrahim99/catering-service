package kickstart.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

	@GetMapping("/customer-list")
	public String showCustomerList() {
		return "customer-list";
	}

}
