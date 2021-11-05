package kickstart.accountServices;

import kickstart.customer.ProfileForm;
import kickstart.customer.RegistrationFrom;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
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

	@GetMapping("/profile")
	public String showCustomerAccount(Model model) {
		ProfileForm form = new ProfileForm(name, email, address);
		System.out.println("/profile: " + form);
		model.addAttribute("accountData", form);
		return "profile";
	}

	@PostMapping("/edit-profile")
	public String showProfileUpdateForm(@ModelAttribute("accountData") ProfileForm data, Model model) {
		model.addAttribute("accountData", data);
		return "edit-profile-page";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute("accountData") ProfileForm data, Model model){
		model.addAttribute("accountData", data);
		this.name = data.getName();
		this.email= data.getEmail();
		this.address = data.getAddress();
		return "redirect:profile";
	}
}
