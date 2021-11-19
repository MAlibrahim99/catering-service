package catering.user;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Id;

@Entity
public class User {
	@Id()
	@GeneratedValue
	private long id;
	@OneToOne
	private UserAccount userAccount;
	@Column
	private String address;
	@Column
	private Position position;

	public User() {
	}

	public User(UserAccount userAccount, String address) {
		this.address = address;
		this.userAccount = userAccount;
	}

	public User(UserAccount userAccount, String address, Position position) {
		if(position == null){
			throw new NullPointerException("Position can not be set assigned to null");
		}
		this.address = address;
		this.userAccount = userAccount;
		this.position = position;
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
