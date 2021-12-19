package catering.user;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Represents a user of the system. This user can be Admin, Customer or employee.
 **/
@Entity
public class User{
	@Id()
	@GeneratedValue
	private long id;
	@OneToOne
	private UserAccount userAccount;
	@Column
	private String address;
	@Enumerated(EnumType.STRING)
	private Position position;
	public int workCount = 0;

	public User() {
	}

	/**
	 * @param userAccount
	 * @param address
	 */
	public User(UserAccount userAccount, String address) {
		this.address = address;
		this.userAccount = userAccount;
	}

	/**
	 * @param userAccount
	 * @param address
	 * @param position
	 */
	public User(UserAccount userAccount, String address, Position position) {
		this(userAccount, address);
		if(position == null){
			throw new NullPointerException("Position can not be set assigned to null");
		}
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

	public int getWorkcount(){
		return workCount;
	}

	public void setWorkcount(int workCount){
		this.workCount = workCount;
	}
}
