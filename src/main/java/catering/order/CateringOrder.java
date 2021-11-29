package catering.order;

import catering.user.Position;
import catering.user.User;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import org.springframework.util.Assert;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Entity
public class CateringOrder extends Order {

	private List<User> allocStaff;
	private Map<Position, Integer> allocPositions;
	private LocalDateTime completionDate;
	private String address;

	public CateringOrder(UserAccount userAccount, LocalDateTime completionDate, String address) {
		super(userAccount);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.address = address;

		for (Position pos : Position.values()) {
			this.allocPositions.put(pos, 0);
		}
	}

	public CateringOrder(UserAccount userAccount, PaymentMethod paymentMethod, LocalDateTime completionDate, String address) {
		super(userAccount, paymentMethod);

		this.allocStaff = new ArrayList<>();
		this.completionDate = completionDate;
		this.address = address;

		for (Position pos : Position.values()) {
			this.allocPositions.put(pos, 0);
		}
	}

	public List<User> getAllocStaff() {
		return allocStaff;
	}

	public void addToAllocStaff(User user) {
		Assert.isTrue(user.getUserAccount().hasRole(Role.of("STAFF")), "User must have Role STAFF");
		this.allocStaff.add(user);

		Integer amount = allocPositions.get(user.getPosition());
		amount += 1;
		allocPositions.put(user.getPosition(), amount);
	}

	public Map<Position, Integer> getAllocPositions() {
		return allocPositions;
	}
}
