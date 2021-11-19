package catering.user;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;

public class RegistrationForm {
	@NotEmpty(message = "Username may not be empty")
	private final String name;
	@NotEmpty(message = "Email may not be empty")
	private final String email;
	@NotEmpty(message = "Password may not be empty")
	private final String password;
	private final Position position;

	public RegistrationForm(String name, String email, String password, Position position) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.position = position;
	}

	public String getName() {
		return name;
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
}
