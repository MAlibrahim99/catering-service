package catering.user;

import catering.user.forms.RegistrationForm;
import org.salespointframework.useraccount.*;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Service
@Transactional
public class UserManagement {

	private final UserRepository users;
	private final UserAccountManagement accountManagement;
	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");
	public static final Role STAFF_ROLE = Role.of("STAFF");
	public static final Role ADMIN_ROLE = Role.of("ADMIN");

	public UserManagement(UserRepository users,
						  @Qualifier("persistentUserAccountManagement") UserAccountManagement accountManagement) {
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
		
		return  users.save(new User(userAccount, "Sie haben noch keine Adresse gegeben", form.getPosition()));
	}

	public boolean deleteUser(long id){
		if(id < 0 || !users.existsById(id)){
			return false;
		}
		users.deleteById(id);
		return true;
	}

	public User findByUsername(String username){
		if(username == null){
			throw new NullPointerException("Id can not be null");
		}
		if(username.isEmpty()){
			throw new IllegalArgumentException("Id can not be empty");
		}
			List<User> allUsers = users.findAll().toList();
			for(User u: allUsers){
				if(u.getUserAccount().getUsername().equals(username)){
					return u;
				}
			}
			return null;
	}

	public Streamable<User> findAllByRole(String role){
		if(role == null){
			throw new NullPointerException("Role can not be null");
		}
		if(role.isEmpty()){
			throw new NullPointerException("Role can not be null");
		}
		Role userRole = Role.of(role);
		List<User> allUsers = users.findAll().toList();
		List<User> filteredUsers = new ArrayList<>();
		for(User user: allUsers){ // wenn es Konten mit der gegebenen Rolle gibt, dann füge sie in die Liste
			if(user.getUserAccount().getRoles().stream().findFirst().isPresent()){
				filteredUsers.add(user);
			}
		}
		return Streamable.of(filteredUsers);
	}

	public boolean usernameAlreadyExists(String username){
		return accountManagement.findByUsername(username).isPresent();
	}

	public boolean emailAlreadyExists(String email){
		return  accountManagement.findAll().stream().map(UserAccount::getEmail).anyMatch(Predicate.isEqual(email));

	}
}
