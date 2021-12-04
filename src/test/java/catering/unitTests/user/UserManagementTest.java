
package catering.unitTests.user;

import catering.user.Position;
import catering.user.User;
import catering.user.UserManagement;
import catering.user.UserRepository;
import catering.user.forms.RegistrationForm;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserManagementTest {

	@Autowired
	private UserRepository users;

	@Autowired
	private UserManagement userManagement;

	@Autowired
	@Qualifier("persistentUserAccountManagement")
	private UserAccountManagement accountManagement;

	private Role customerRole = Role.of("CUSTOMER");

	@Test
	public void userManagementCreatesUserSuccessfully(){

		RegistrationForm form = new RegistrationForm("user1" ,"user1", "user1@host.com",
				"123", Position.NONE);
		userManagement.createUser(form, customerRole);
		User currentUser = userManagement.findByUsername("user1 user1");
		System.out.println(currentUser);
		assertThat(currentUser).isNotNull();
		assertThat(currentUser.getUserAccount().getUsername()).isEqualTo(form.getUsername());
		assertThat(currentUser.getUserAccount().getFirstname()).isEqualTo(form.getFirstName());
		assertThat(currentUser.getUserAccount().getEmail()).isEqualTo(form.getEmail());
		assertThat(currentUser.getUserAccount().getRoles()).isNotEmpty();
	}
}

