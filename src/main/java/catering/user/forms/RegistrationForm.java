package catering.user.forms;

import catering.user.Position;

import javax.validation.constraints.NotEmpty;

/**
 * Data object for transporting data between templates and application logic
 * */
public class RegistrationForm {
	@NotEmpty(message = "First name may not be empty")
	private final String firstName;
	@NotEmpty(message = "Last name may not be empty")
	private final String lastName;
	@NotEmpty(message = "Username can not be empty")
	private String username;
	@NotEmpty(message = "Email may not be empty")
	private final String email;
	@NotEmpty(message = "Password may not be empty")
	private final String password;
//	@NotEmpty(message = "Position can not be empty")
	private Position position ;


	/**
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param position
	 * @param username
	 */
	public RegistrationForm(String firstName, String lastName, String username, String email, String password, Position position) {
		this.firstName = firstName;
		this.username = username;
		this.email = email;
		this.password = password;
		if(position == null) {
			this.position = Position.NONE;
		}else{
			this.position = position;
		}
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "RegistrationForm{" +
				"firstName='" + firstName + '\'' +
				"lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", position=" + position +
				'}';
	}
}
