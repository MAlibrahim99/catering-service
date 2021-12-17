package catering.user;

import catering.user.forms.RegistrationForm;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
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
		accountManagement.create("Hannes", Password.UnencryptedPassword.of("123"),"admin@mampf.de", Role.of("ADMIN"));

		List<RegistrationForm> registrationForms = List.of(
			new RegistrationForm("planet", "stern1", "stern1@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern2", "stern2@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern3", "stern3@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern4", "stern4@mampf.de", "123", Position.COOK),
			new RegistrationForm("planet", "stern5", "stern5@mampf.de", "123", Position.COOK),
			new RegistrationForm("max", "muster1", "muster1@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster2", "muster2@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster3", "muster3@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster4", "muster4@mampf.de", "123", Position.WAITER),
			new RegistrationForm("max", "muster5", "muster5@mampf.de", "123", Position.WAITER),
			new RegistrationForm("ghjk", "vogel", "vogel@mampf.de", "123", Position.EXPERIENCED_WAITER)
		);
		registrationForms.forEach(e -> userManagement.createUser(e, UserManagement.STAFF_ROLE));

		List.of(
				new RegistrationForm("user", "1", "user1@f.c", "123", Position.NONE),
				new RegistrationForm("user", "2", "user2@f.c", "123", Position.NONE),
				new RegistrationForm("user", "3", "user3@f.c", "123", Position.NONE)
		).forEach(e -> userManagement.createUser(e, UserManagement.CUSTOMER_ROLE));
	}
}
