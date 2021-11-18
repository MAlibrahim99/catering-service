package catering.user;

import org.salespointframework.useraccount.UserAccount;

public class User {
	// annotate for DB
	private long id;
	private String address;

	private UserAccount userAccount;

	public User() {
	}

	public User(String address, UserAccount userAccount) {
		this.address = address;
		this.userAccount = userAccount;
	}

	public String getAddress() {
		return address;
	}

	public long getId() {
		return id;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}
}
