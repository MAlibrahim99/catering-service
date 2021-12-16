package catering.order;

import catering.user.User;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class CateringOrder extends Order {

	@OneToMany(cascade = {CascadeType.ALL})
	private List<User> allocStaff;
	@DateTimeFormat(pattern= "yyyy-MM-dd")
	private LocalDate completionDate;
	private String address;
	private String service;
	private String time;
	private int waitercount;
	private int chefcount;

	public CateringOrder(UserAccount userAccount, LocalDate completionDate, String time, String address, String service) {
		super(userAccount);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.time = time;
		this.address = address;
		this.service = service;
	}

	public CateringOrder(UserAccount userAccount, PaymentMethod paymentMethod, LocalDate completionDate, String address,
						String time, String service) {
		super(userAccount, paymentMethod);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.address = address;
		this.time = time;
		this.service = service;
	}

	// Author Mohamad: ich bekomme: "No default constructor for entity:  : catering.order.CateringOrder", wenn dieser
	// depricated Argumenetenloser Konstruktor abwesen ist.
	public CateringOrder() {}

	public List<User> getAllocStaff() {
		return allocStaff;
	}

	public void addToAllocStaff(User user) {
		//Assert.isTrue(user.getUserAccount().hasRole(Role.of("STAFF")), "User must have Role STAFF");
		this.allocStaff.add(user);
	}


	public LocalDate getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(LocalDate completionDate){
		this.completionDate = completionDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public int getChefcount(){
		return chefcount;
	}

	public void setChefcount(int chefcount){
		this.chefcount = chefcount;
	}

	public int getWaitercount(){
		return waitercount;
	}

	public void setWaitercount(int waitercount){
		this.waitercount = waitercount;
	}

	public String getService(){
		return service;
	}

	public void setService(String service){
		this.service = service;
	}

	public String getTime(){
		return time;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String toString(){
		return service + " " + completionDate + " " + time + " " + address + " " + waitercount + " " + chefcount;
	}
}
