package kickstart.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {

	private String name = "Hannes Wurst";
	private String email = "hannes.wurst@mampf.de";
	private String password = "123";
	private String address = "";

	@PostMapping("/register")
	public String registerNewCustomer(@ModelAttribute("registrationForm") RegistrationFrom form, Model model){
		model.addAttribute("userName", form.getName());
		return "main-page";
	}

	@GetMapping("/register")
	public String register(Model model, RegistrationFrom form){
		model.addAttribute("registrationForm", form);
		return "register";
	}
}
