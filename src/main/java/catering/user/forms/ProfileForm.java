package catering.user.forms;

import catering.user.Position;

public class ProfileForm extends RegistrationForm {

	private String address;
	private String passwordConfirmation;
	public ProfileForm(String firstName, String lastName, String email) {
		super(firstName, lastName, email, "********", Position.NONE);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}
