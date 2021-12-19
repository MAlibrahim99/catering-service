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

/**
 * extends {@link Order} from Salespoint and adds new attributes
 */
@Entity
public class CateringOrder extends Order {

	@OneToMany(cascade = {CascadeType.ALL})
	private List<User> allocStaff;
	@DateTimeFormat(pattern= "yyyy-MM-dd")
	private LocalDate completionDate;
	private String address;
	private String service;
	private TimeSegment time;
	private int waitercount;
	private int chefcount;
	private String timeString;

	public CateringOrder(UserAccount userAccount, LocalDate completionDate, TimeSegment time,
						 String address, String service) {
		super(userAccount);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.time = time;
		this.address = address;
		this.service = service;
	}

	/**
	 * a parametrised constructor
	 * @param userAccount the account of the logged-in user, that submits the order
	 * @param paymentMethod the payment method
	 * @param completionDate the date when the order is executed
	 * @param time the time when the order is executed
	 * @param address the address where the order is executed
	 * @param service the service type of the order
	 */
	public CateringOrder(UserAccount userAccount, PaymentMethod paymentMethod, LocalDate completionDate,
						 TimeSegment time, String address, String service) {
		super(userAccount, paymentMethod);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.time = time;
		this.address = address;
		this.time = time;
		this.service = service;
	}

	// Author Mohamad: ich bekomme: "No default constructor for entity:  : catering.order.CateringOrder", wenn dieser
	// depricated Argumenetenloser Konstruktor abwesen ist.
	public CateringOrder() {
		this.allocStaff = new ArrayList<>();
	}

	/**
	 * getter and setter for the attributes
	 */
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

	public TimeSegment getTime(){
		return time;
	}

	public void setTime(TimeSegment time){
		this.time = time;
	}

	public String toString(){
		return service + " " + completionDate + " " + time + " " + address + " " + waitercount + " " + chefcount;
	}

	public String getTimeString(){
		return timeString;
	}

	public void setTimeString(String timeString){
		this.timeString = timeString;
	}
}
