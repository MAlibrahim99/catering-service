package catering.user;

import catering.user.forms.RegistrationForm;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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
	public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
							   @RequestParam(value = "action") String action, Model model){
		System.out.println("register user coming on");
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

		// entweder personal oder normalen Nutzer erstellen
		switch(action){
			case "register-staff" : userManagement.createUser(form, UserManagement.STAFF_ROLE);
				model.addAttribute("userName", form.getLastName());
				return "redirect:/register";
			case "register-user" : userManagement.createUser(form, UserManagement.CUSTOMER_ROLE);
				model.addAttribute("userName", form.getLastName());
				return "welcome";
			default: return "register";
		}
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

	@GetMapping("/staff-list")
	@PreAuthorize(value="isAuthenticated()")
	public String showStaffList(@LoggedIn UserAccount account, Model model){
		if(account.hasRole(Role.of("ADMIN"))){
			// finde alle personal mit Positionen(Cook, EXPERIENCED_WAITER; WAITER)
			Iterable<User> staff = userRepository.getUserByPositionIn(List.of(Position.COOK, Position.EXPERIENCED_WAITER,
			Position.WAITER));
			model.addAttribute("allStaff", staff);
			return "staff-list";
		}else{
			return "access-denied";
		}
	}

	@GetMapping("/test")
	@PreAuthorize("isAuthenticated()")
	public String sendTest() {
		return "test";
	}
}
