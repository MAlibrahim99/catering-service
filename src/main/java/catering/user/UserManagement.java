package catering.user;

import catering.user.forms.ProfileForm;
import catering.user.forms.RegistrationForm;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Helps by Creating, Updating and Deleting User entities through collaborating with {@link UserAccountManagement} and
 * Using the {@link UserRepository}.
 * */
@Service
@Transactional
public class UserManagement {

	private final UserRepository users;
	private final UserAccountManagement accountManagement;
	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");
	public static final Role STAFF_ROLE = Role.of("STAFF");
	private long index = 0;


	/**
	 * @param users UserRepository
	 * @param accountManagement UserAccountManagement
	 */
	public UserManagement(UserRepository users,
						  @Qualifier("persistentUserAccountManagement") UserAccountManagement accountManagement) {
		if(users == null || accountManagement == null){
			throw new IllegalArgumentException("userRepository or userAccountManager can not be assigned to null");
		}
		this.users = users;
		this.accountManagement = accountManagement;
	}


	/**
	 * Creates and persists a new user Entity according to the data in the data object {@link RegistrationForm} form.
	 * @param form {@link RegistrationForm}
	 * @param roles {@link Role}
	 * @return {@link User}
	 * @throws IllegalArgumentException if form provided is refers to null value
	 */
	public User createUser(RegistrationForm form, Role ... roles){
		if(form == null){
			throw new IllegalArgumentException("User can not be created with value null of RegistrationForm");
		}
		if(usernameAlreadyExists(form.getUsername()) || emailAlreadyExists(form.getEmail())){
			return null;
		}
		UnencryptedPassword password= UnencryptedPassword.of(form.getPassword());
		UserAccount userAccount = accountManagement.create(form.getUsername(), password, form.getEmail(), roles);
		userAccount.setFirstname(form.getFirstName());
		userAccount.setLastname(form.getLastName());
		
		return  users.save(new User(userAccount, "Sie haben noch keine Adresse gegeben", form.getPosition()));
	}


	/**
	 * updates new user data persisted in Database
	 * @param data  {@link ProfileForm}
	 * @param user {@link User}
	 * @return {@link User}
	 */
public User updateUser(ProfileForm data, User user) {
		if(data == null) {
			throw new IllegalArgumentException("User can not be created with value null of ProfileForm");
		}
		UserAccount userAccount = user.getUserAccount();
		userAccount.setFirstname(data.getFirstName());
		userAccount.setLastname(data.getLastName());
		userAccount.setEmail(data.getEmail());
		user.setAddress(data.getAddress());
		
		return users.save(user);
	}

	public boolean deleteUser(long id){
		if(id < 0 || !users.existsById(id)){
			return false;
		}
		UserAccount userAccount = users.findById(id).get().getUserAccount();
		userAccount.setEmail("disabledAccount" + index);
		index++;
		accountManagement.disable(userAccount.getId());
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
	
	public User findByEmail(String email) {
		if(email == null) {
			throw new NullPointerException("Id can not be null");
		}
		if(email.isEmpty()) {
			throw new IllegalArgumentException("Id can not be empty");
		}
		List<User> allUsers = users.findAll().toList();
		for(User u: allUsers) {
			if(u.getUserAccount().getEmail().equals(email)) {
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
