package kickstart.customer;

import javax.validation.constraints.NotNull;

public class ProfileForm {
	@NotNull
	private final String name;

	@NotNull
	private final String email;

	private final String address;

	public ProfileForm(String name, String email, String address) {
		this.name = name;
		this.email = email;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "ProfileForm{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}
