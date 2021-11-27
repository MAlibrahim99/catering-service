package catering.user;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class UserController {

	private final UserManagement userManagement;
	private final UserRepository userRepository;

	public UserController(UserManagement userManagement, UserRepository userRepository) {
		this.userManagement = userManagement;
		this.userRepository = userRepository;
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm form, Model model){
		// wenn name oder email bereits besetzt ist, gebe nutzer meldung zurück
		if(userManagement.usernameAlreadyExists(form.getUsername())){
			model.addAttribute("usernameAlreadyExists", true);
		}
		if(userManagement.emailAlreadyExists(form.getEmail())){
			model.addAttribute("emailAddressAlreadyExists", true);
		}
		if(model.containsAttribute("usernameAlreadyExists") || model.containsAttribute("emailAddressAlreadyExists")){
			return "register";
		}

		userManagement.createUser(form);
		model.addAttribute("userName", form.getLastName()); // für Benutzer begrüßen
		return "welcome";
	}

	@GetMapping("/register")
	public String register(Model model, RegistrationForm form){
		model.addAttribute("registrationForm", form);
		return "register";
	}

	@GetMapping("/profile")
	public String sendProfilePage(@LoggedIn Optional<UserAccount> account, Model model){
		System.out.println("Profile info: " + account.isPresent());
		account.ifPresent(userAccount -> System.out.println(userAccount.getUsername()));
		if(account.isPresent()){
			model.addAttribute("user", userManagement.findByUsername(account.get().getUsername()));
			return "profile";
		}
		return "login";
	}

	@GetMapping("/test")
	@PreAuthorize("isAuthenticated()")
	public String sendTest() {
		return "test";
	}
}
