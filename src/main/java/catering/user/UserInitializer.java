package catering.user;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order()
public class UserInitializer implements DataInitializer {

	private final UserAccountManagement accountManagement;
	private final UserManagement userManagement;

	public UserInitializer(@Qualifier("persistentUserAccountManagement") UserAccountManagement accountManagement, UserManagement userManagement) {
		this.accountManagement = accountManagement;
		this.userManagement = userManagement;
	}

	@Override
	public void initialize() {
		if(accountManagement.findByUsername("Hannes").isPresent()){
			return;
		}

		System.out.println("Initializing User accounts");
		accountManagement.create("Hannes", Password.UnencryptedPassword.of("123"), Role.of("ADMIN"));

		List.of(
				new RegistrationForm("stern", "stern@mampf.de", "123", Position.NONE),
				new RegistrationForm("muster", "muster@mampf.de", "123", Position.COOK),
				new RegistrationForm("vogel", "vogel@mampf.de", "123", Position.EXPERIENCED_WAITER)
		).forEach(userManagement::createUser);
	}
}
