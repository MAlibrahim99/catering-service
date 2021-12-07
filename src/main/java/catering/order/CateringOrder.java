package catering.order;

import catering.user.User;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class CateringOrder extends Order {

	@OneToMany(cascade = {CascadeType.ALL})
	private List<User> allocStaff;
	private LocalDateTime completionDate;
	private String address;

	public CateringOrder(UserAccount userAccount, LocalDateTime completionDate, String address) {
		super(userAccount);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.address = address;
	}

	public CateringOrder(UserAccount userAccount, PaymentMethod paymentMethod,
						 LocalDateTime completionDate,String address) {
		super(userAccount, paymentMethod);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.address = address;
	}

	// Author Mohamad: ich bekomme: "No default constructor for entity:  : catering.order.CateringOrder", wenn dieser
	// depricated Argumenetenloser Konstruktor abwesen ist.
	public CateringOrder() {}

	public List<User> getAllocStaff() {
		return allocStaff;
	}

	public void addToAllocStaff(User user) {
		Assert.isTrue(user.getUserAccount().hasRole(Role.of("STAFF")), "User must have Role STAFF");
		this.allocStaff.add(user);
	}

	public LocalDateTime getCompletionDate() {
		return completionDate;
	}

	public String getAddress() {
		return address;
	}
}
