package kickstart.customer;

import javax.validation.constraints.NotNull;

public class RegistrationFrom {
	@NotNull
	private final String name;

	@NotNull
	private final String email;

	@NotNull
	private final String password;

	public RegistrationFrom(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
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


}
