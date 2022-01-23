package catering.user;

import catering.user.forms.RegistrationForm;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(10)
public class UserInitializer implements DataInitializer {

	private final UserAccountManagement accountManagement;
	private final UserManagement userManagement;

	public UserInitializer(@Qualifier("persistentUserAccountManagement") UserAccountManagement accountManagement,
						   UserManagement userManagement) {
		this.accountManagement = accountManagement;
		this.userManagement = userManagement;
	}

	@Override
	public void initialize() {
		if(accountManagement.findByUsername("Hannes").isPresent()){
			return;
		}

		System.out.println("Initializing User accounts");
		userManagement.createUser(new RegistrationForm("Hannes", "Wurst", "Hannes Wurst", "admin@mampf.de",
				"123", Position.NONE), Role.of("ADMIN"));

		List<RegistrationForm> registrationForms = List.of(
			new RegistrationForm("planet", "stern1", "staff1","stern1@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern2", "staff2","stern2@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern3", "staff3","stern3@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern4", "staff4","stern4@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern5", "staff5","stern5@mampf.de", "123", Position.COOK),
			new RegistrationForm("max", "muster1", "staff6", "muster1@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster2", "staff7", "muster2@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster3", "staff8", "muster3@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster4", "staff9", "muster4@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster5", "staff10", "muster5@mampf.de", "123", Position.WAITER),
			new RegistrationForm("ghjk", "vogel", "staff11","vogel@mampf.de", "123", Position.EXPERIENCED_WAITER),
			new RegistrationForm("max", "muster6","staff12", "muster6@mampf.de", "123", Position.MINIJOB),
			new RegistrationForm("max", "muster7","staff13", "muster7@mampf.de", "123", Position.MINIJOB)
		);
		registrationForms.forEach(e -> userManagement.createUser(e, UserManagement.STAFF_ROLE));

		List.of(
				new RegistrationForm("user", "1", "customer1", "user1@f.c", "123", Position.NONE),
				new RegistrationForm("user", "2", "customer2", "user2@f.c", "123", Position.NONE),
				new RegistrationForm("user", "3", "customer3", "user3@f.c", "123", Position.NONE)
		).forEach(e -> userManagement.createUser(e, UserManagement.CUSTOMER_ROLE));
	}
}
