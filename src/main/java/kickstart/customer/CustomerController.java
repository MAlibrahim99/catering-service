package kickstart.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@PostMapping("/login")
	public String logUserIn(@RequestParam("email")String email,
							@RequestParam("password") String password,
							Model model){
		if(email.equals(this.email) && password.equals(this.password)){
			model.addAttribute("userName", this.name);
			return "main-page";
		}else{
			return "login-page";
		}
	}

	@GetMapping("/login")
	public String sendLoginForm(){
		return "login-page";
	}

	@GetMapping("/customer-list")
	public String showCustomerList() {
		return "customer-list";
	}
}
