package catering.order;

import catering.user.User;
import catering.user.UserManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
@Order(30)
public class OrderInitializer implements DataInitializer {

	private final OrderManagement<CateringOrder> orderManagement;
	private final UserManagement userManagement;

	public OrderInitializer(@Qualifier("persistentOrderManagement") OrderManagement<CateringOrder> management,
							UserManagement userManagement) {
		this.orderManagement = management;
		this.userManagement = userManagement;
	}

	@Override
	public void initialize() {
		// wenn Konto da ist, und dieses Konto Bestellungen hat dann überspringe Initailieserien
		if (userManagement.findByUsername("user 1") != null
				&& !orderManagement.findBy(userManagement.findByUsername("user 1").getUserAccount()).isEmpty()) {
				return;
		}

		System.out.println("+++++++++initilizing orders");

		List<UserAccount> accounts = List.of(userManagement.findByUsername("user 1").getUserAccount(),
			userManagement.findByUsername("user 2").getUserAccount(),
			userManagement.findByUsername("user 3").getUserAccount());

		int randUser, randDay, randMonth, randYear, randDayTime, randServiceType, randStatus;
		TimeSegment [] timeSegments = TimeSegment.values();
		String [] serviceType = {"Eventcatering", "MobileBreakfast", "Partyservice", "RentACook"};
		CateringOrder order;
		Random random = new Random();

		for (int i = 0; i < 50; i++) {
			randUser = random.nextInt(3);
			randDay = random.nextInt(28) +1;
			randMonth = random.nextInt(12) + 1;
			randYear = random.nextInt(8) + 2017;
			randDayTime = random.nextInt(3);
			randServiceType = random.nextInt(4);
			randStatus = random.nextInt(5);

			order = new CateringOrder(accounts.get(randUser),
					Cash.CASH,
					LocalDate.of(randYear, randMonth, randDay),
					timeSegments[randDayTime]
					,"some Address " + random.nextInt(50),
					serviceType[randServiceType]);


			orderManagement.payOrder(order);

			switch (randStatus){
				case 0: orderManagement.completeOrder(order);
					break;
				case 1: orderManagement.completeOrder(order);
					break;
				case 2: orderManagement.completeOrder(order);
					break;
				case 3: orderManagement.cancelOrder(order, "None");
					break;
				case 4:
					break;
			}
			orderManagement.save(order);
		}
	}
}