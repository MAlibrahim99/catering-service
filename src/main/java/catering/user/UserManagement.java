package catering.user;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

@Service
@Transactional
public class UserManagement {

	private final UserRepository users;
	private final UserAccountManagement accountManagement;
	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");
	public static final Role STAFF_ROLE = Role.of("STAFF");
//	public static final Role ADMIN_ROLE = Role.of("ADMIN");

	public UserManagement(UserRepository users, @Qualifier("persistentUserAccountManagement") UserAccountManagement accountManagement) { //TODO ich soll noch UserAccountManagement Qualifizieren
		if(users == null || accountManagement == null){
			throw new IllegalArgumentException("userRepository or userAccountManager can not be assigned to null");
		}
		this.users = users;
		this.accountManagement = accountManagement;
	}

	public User createUser(RegistrationForm form){
		if(form == null){
			throw new IllegalArgumentException("User can not be created with value null of RegistrationForm");
		}
		UnencryptedPassword password= UnencryptedPassword.of(form.getPassword());
		UserAccount userAccount = accountManagement.create(form.getName(), password, form.getEmail(), CUSTOMER_ROLE);
			System.out.println("Saving " + userAccount.toString());
			return users.save(new User(userAccount, "None"));
	}

	public boolean usernameAlreadyExists(String username){
		return accountManagement.findByUsername(username).isPresent();
	}

	public boolean emailAlreadyExists(String email){
		return  accountManagement.findAll().stream().map(UserAccount::getEmail).anyMatch(Predicate.isEqual(email));

	}
}
