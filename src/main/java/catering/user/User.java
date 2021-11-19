package catering.user;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;

@Entity
public class User {
	@Id()
	@GeneratedValue
	private long id;
	@Column
	private String address;
	@OneToOne
	private UserAccount userAccount;
	@Column
	private Position position;

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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
