package catering.user;

import org.salespointframework.useraccount.*;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.function.Predicate;

@Service
@Transactional
public class UserManagement {

	private final UserRepository users;
	private final UserAccountManagement accountManagement;
	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");
	public static final Role STAFF_ROLE = Role.of("STAFF");
//	public static final Role ADMIN_ROLE = Role.of("ADMIN");

	public UserManagement(UserRepository users, @Qualifier("persistentUserAccountManagement") UserAccountManagement accountManagement) {
		if(users == null || accountManagement == null){
			throw new IllegalArgumentException("userRepository or userAccountManager can not be assigned to null");
		}
		this.users = users;
		this.accountManagement = accountManagement;
	}

	public User createUser(RegistrationForm form, Role ... roles){
		if(form == null){
			throw new IllegalArgumentException("User can not be created with value null of RegistrationForm");
		}
		UnencryptedPassword password= UnencryptedPassword.of(form.getPassword());
		UserAccount userAccount = accountManagement.create(form.getUsername(), password, form.getEmail(), roles);
		userAccount.setFirstname(form.getFirstName());
		userAccount.setLastname(form.getLastName());
			return users.save(new User(userAccount, "Sie haben noch keine Adresse gegeben"));
	}

	public User createStaff(RegistrationForm form){
		if(form == null){
			throw new IllegalArgumentException("Staff can not be created with value null of RegistrationForm");
		}
		return this.createUser(form, STAFF_ROLE);
	}

	public boolean deleteUserAccount(UserAccountIdentifier id){
		if(id ==null){
			throw new NullPointerException("UserAccountIdentifier can not be null");
		}
		if(accountManagement.get(id).isPresent()){
			accountManagement.delete(accountManagement.get(id).get());
			return true;
		}
		return false;
	}

	public User findByUsername(String id){
		if(id == null){
			throw new NullPointerException("Id can not be null");
		}
		if(id.isEmpty()){
			throw new IllegalArgumentException("Id can not be empty");
		}
			ArrayList<User> allUsers = (ArrayList<User>) users.findAll();
			User user;
			for(User u: allUsers){
				if(u.getUserAccount().getUsername().equals(id)){
					return u;
				}
			}
			return null;
	}

	public boolean usernameAlreadyExists(String username){
		return accountManagement.findByUsername(username).isPresent();
	}

	public boolean emailAlreadyExists(String email){
		return  accountManagement.findAll().stream().map(UserAccount::getEmail).anyMatch(Predicate.isEqual(email));

	}
}
