package catering.user;

import catering.user.forms.ProfileForm;
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

/**
 * Contains the functionality that enables users register, modify and delete their accounts on the system
 */
@Controller
public class UserController {

	private final UserManagement userManagement;
	private final UserRepository userRepository;


	/**
	 * @param userManagement
	 * @param userRepository
	 */
	public UserController(UserManagement userManagement, UserRepository userRepository) {
		this.userManagement = userManagement;
		this.userRepository = userRepository;
	}

	/**
	 * Receives the registration requests and delegates creating new {@link User} entity to the class {@link UserManagement}
	 * if the firstname and the lastname or the email are already token the registration will fail, because the first two
	 * fields are used as id for {@link UserAccount} and the email will be used for login.
	 *
	 * This method handles the registration of new Customers and employees as well based on the{@param action} argument.
	 */
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
							   @RequestParam(value = "action") String action, Model model){
		// wenn name oder email bereits besetzt ist, gebe nutzer meldung zurück
		if(userManagement.usernameAlreadyExists(form.getUsername())){
			model.addAttribute("usernameAlreadyExists", true);
		}
		if(userManagement.emailAlreadyExists(form.getEmail())){
			model.addAttribute("emailAddressAlreadyExists", true);
		}
		if(model.containsAttribute("usernameAlreadyExists") ||
				model.containsAttribute("emailAddressAlreadyExists")){
			return "register";
		}

		if(action.equals("register-staff")){
			userManagement.createUser(form, UserManagement.STAFF_ROLE);
		}else{
			userManagement.createUser(form, UserManagement.CUSTOMER_ROLE);
			model.addAttribute("userName", form.getLastName());
			return "index";
		}
		return "redirect:/register";
	}

	/**
	 * Returns a view of the Registration page with Data object {@link RegistrationForm} and a sublist of {@link Position}
	 * enums
	 * @param model Model
	 * @param form RegistrationForm
	 * @return A name of the register view
	 * */
	@GetMapping("/register")
	public String register(Model model, RegistrationForm form){
		model.addAttribute("registrationForm", form);
		model.addAttribute("positions", List.of(Position.COOK, Position.EXPERIENCED_WAITER, Position.WAITER));
		return "register";
	}

	@GetMapping("/profile")
	@PreAuthorize(value="hasAnyRole('CUSTOMER', 'ADMIN')")
	public String sendProfilePage(@LoggedIn Optional<UserAccount> account, Model model){
		if(account.isPresent()){
			model.addAttribute("user", userManagement.findByUsername(account.get().getUsername()));
			return "profile";
		}
		return "login";
	}
	
	@GetMapping("/customer-list")
	@PreAuthorize(value = "isAuthenticated()")
	public String showCustomerList(@LoggedIn UserAccount account, Model model){
		if(account.hasRole(Role.of("ADMIN"))) {
			Iterable<User> customers = userRepository.getUserByPositionIn(List.of(Position.NONE));
			model.addAttribute("allCustomers", customers);
			return "customer-list";
		}else {
			return "login";
		}
	}


	/**
	 * Returns view of employee accounts registered in the system.
	 * @param account UserAccount
	 * @param model Model
	 * @return A name of the view template
	 */
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

	@GetMapping("/edit-profile")
	public String showProfileForm(@ModelAttribute("profileForm") ProfileForm data, Model model) {
		model.addAttribute("profileForm", data);
		return "edit-profile";
	}


	/**
	 * handles updating and persisting {@link User}'s new submitted data. It also guarantees that there will be no
	 * duplicates in emails or usernames.
	 * @param data ProfileForm
	 * @param account UserAccount
	 * @param model Model
	 * @return view name
	 */
	@PostMapping("/update-profile")
	public String updateUserAccount(@Valid @ModelAttribute("profileForm") ProfileForm data, 
									@LoggedIn UserAccount account, Model model) {
		User user = userManagement.findByEmail(account.getEmail());
		if (!account.getUsername().equals(data.getUsername()) && userManagement.usernameAlreadyExists(data.getUsername())){
				model.addAttribute("usernameAlreadyExists", true);
		}
		if (!account.getEmail().equals(data.getEmail()) && userManagement.emailAlreadyExists(data.getEmail())) {
			model.addAttribute("emailAddressAlreadyExists", true);
		}
		if(model.containsAttribute("usernameAlreadyExists") ||
				model.containsAttribute("emailAddressAlreadyExists")){
			return "edit-profile";
		}
		userManagement.updateUser(data, user);
		return "index";
		
	}


	/**
	 * Delegates deleting user Entity to {@link UserManagement}
	 * @param userId long
	 * @param user UserAccount
	 * @return view name
	 */
	@GetMapping("/delete-account/{id}")
	@PreAuthorize(value="isAuthenticated()")
	public String deleteUseraccount(@PathVariable ("id") long userId, @LoggedIn UserAccount user) {
		if(user.hasRole(Role.of("CUSTOMER"))) {
			userManagement.deleteUser(userId);
			return "redirect:/logout";
		}
		if(user.hasRole(Role.of("ADMIN"))) {
			userManagement.deleteUser(userId);
			return "redirect:/staff-list";
		}else {
		return "access-denied";
		}
	}
}
