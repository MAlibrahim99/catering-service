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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class CateringOrder extends Order {

	@OneToMany(cascade = {CascadeType.ALL})
	private List<User> allocStaff;
	private LocalDate completionDate;
	private TimeSegment time;
	private String address;
	private String service;

	public CateringOrder(UserAccount userAccount, LocalDate completionDate, TimeSegment time, String address, String service) {
		super(userAccount);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.time = time;
		this.address = address;
		this.service = service;
	}

	public CateringOrder(UserAccount userAccount, PaymentMethod paymentMethod,
						 LocalDate completionDate, TimeSegment time, String address, String service) {
		super(userAccount, paymentMethod);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.time = time;
		this.address = address;
		this.service = service;
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

	public LocalDate getCompletionDate() {
		return completionDate;
	}

	public TimeSegement getTime() {
		return time;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getService() {
		return service;
	}
}
