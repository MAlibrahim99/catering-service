package catering.order;

import catering.user.UserManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;


/**
 * Implements {@link DataInitializer} to generate starting values of {@link CateringOrder}
 */
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


	/**
	 * Initializes some objects of class {@link CateringOrder}. The fields of generated objects get randomly
	 * values.
	 */
	@Override
	public void initialize() {
		// wenn Konto da ist, und dieses Konto Bestellungen hat dann überspringe Initailieserien
		if (userManagement.findByUsername("customer1") != null
				&& !orderManagement.findBy(userManagement.findByUsername("customer1").getUserAccount()).isEmpty()) {
				return;
		}

		List<UserAccount> accounts = List.of(userManagement.findByUsername("customer1").getUserAccount(),
			userManagement.findByUsername("customer2").getUserAccount(),
			userManagement.findByUsername("customer3").getUserAccount());

		int randUser, randDay, randMonth, randYear, randDayTime, randServiceType, randStatus;
		TimeSegment [] timeSegments = TimeSegment.values();
		String [] serviceType = {"Eventcatering", "MobileBreakfast", "Partyservice", "RentACook"};
		CateringOrder order;
		Random random = new Random();
		int currentYear = LocalDate.now().getYear();
		for (int i = 0; i < 50; i++) {
			randUser = random.nextInt(3);
			randDay = random.nextInt(28) +1;
			randMonth = random.nextInt(12) + 1;
			randYear = currentYear + 3 - random.nextInt(7);
			randDayTime = random.nextInt(3);
			randServiceType = random.nextInt(4);
			randStatus = random.nextInt(5);

			order = new CateringOrder(accounts.get(randUser),
					Cash.CASH,
					LocalDate.of(randYear, randMonth, randDay),
					timeSegments[randDayTime]
					,"some Address " + random.nextInt(50),
					serviceType[randServiceType]);

			if(LocalDate.of(randYear, randMonth, randMonth).isBefore(LocalDate.now())) {
				orderManagement.payOrder(order);
				switch (randStatus) {
					case 1:
					case 3:
					case 4:
						orderManagement.completeOrder(order);
						break;
					case 0:
					case 2:
						orderManagement.cancelOrder(order, "None");
					default:
						break;
				}
			}else{
				switch(randStatus){
					case 1: case 4:
					case 2:
						break;
					default: orderManagement.payOrder(order);
						break;
				}
			}

			orderManagement.save(order);
		}
	}
}