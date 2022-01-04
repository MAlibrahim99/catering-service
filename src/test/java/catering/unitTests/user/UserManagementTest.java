
package catering.unitTests.user;

import catering.user.Position;
import catering.user.User;
import catering.user.UserManagement;
import catering.user.UserRepository;
import catering.user.forms.ProfileForm;
import catering.user.forms.RegistrationForm;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.salespointframework.useraccount.Role;
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

		RegistrationForm form = new RegistrationForm("user1" ,"user1", "user", "user@host.com",
				"123", Position.NONE);
		userManagement.createUser(form, customerRole);
		User currentUser = userManagement.findByUsername("user");
		System.out.println(currentUser);
		assertThat(currentUser).isNotNull();
		assertThat(currentUser.getUserAccount().getUsername()).isEqualTo(form.getUsername());
		assertThat(currentUser.getUserAccount().getFirstname()).isEqualTo(form.getFirstName());
		assertThat(currentUser.getUserAccount().getEmail()).isEqualTo(form.getEmail());
		assertThat(currentUser.getUserAccount().getRoles()).isNotEmpty();
	}
	
	@Test
	public void userIsUpdatedSuccessfully() {
		RegistrationForm form = new RegistrationForm("user1" ,"user1", "user1", "user1@host.com",
				"123", Position.NONE);
		userManagement.createUser(form, customerRole);
		User currentUser = userManagement.findByUsername("user1");
		ProfileForm data = new ProfileForm("1", "a", "email@user.de");
		userManagement.updateUser(data, currentUser);
		assertThat(currentUser.getId()).isGreaterThan(0);
		assertThat(data).isNotNull();
		assertThat(currentUser).isNotNull();
		assertThat(currentUser.getUserAccount().getFirstname()).isEqualTo("1");
		assertThat(currentUser.getUserAccount().getLastname()).isEqualTo("a");
		assertThat(currentUser.getUserAccount().getEmail()).isEqualTo("email@user.de");
		assertThat(currentUser.getAddress()).isEqualTo(data.getAddress());
	}
	
	@Test
	public void deleteUserAccount() {
		RegistrationForm form = new RegistrationForm("user1" ,"user1", "user2", "user2@host.com",
				"123", Position.NONE);
		userManagement.createUser(form, customerRole);
		User currentUser = userManagement.findByUsername("user2");
		userManagement.deleteUser(currentUser.getId());
		assertThat(users.findById(currentUser.getId())).isEmpty();
	}
}

