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
import java.time.LocalDateTime;

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

		UserAccount account1 = userManagement.findByUsername("user 1").getUserAccount();
		UserAccount account2 = userManagement.findByUsername("user 2").getUserAccount();
		UserAccount account3 = userManagement.findByUsername("user 3").getUserAccount();

		System.out.println("+++++++++initilizing orders");
		orderManagement.save( new CateringOrder(account1, Cash.CASH ,LocalDate.of(2019,3,23),
				 TimeSegment.ABEND, "some Address 3", "some service 1"));
		orderManagement.save( new CateringOrder(account1,  Cash.CASH, LocalDate.of(2019, 4, 1),
				 TimeSegment.FRÜH, "some Address 2", "some service 2"));
		orderManagement.save( new CateringOrder(account1,  Cash.CASH, LocalDate.of(2018, 10, 1),
				 TimeSegment.ABEND, "some Address 5", "some service 3"));
		orderManagement.save( new CateringOrder(account1,  Cash.CASH, LocalDate.of(2018, 6, 21),
				 TimeSegment.FRÜH, "some Address 7", "some service 4"));
		orderManagement.save( new CateringOrder(account1,  Cash.CASH, LocalDate.of(2018, 7, 23),
				 TimeSegment.MITTAG, "some Address 34", "some service 5"));

		orderManagement.save( new CateringOrder(account2, Cash.CASH, LocalDate.of(2020,5,23),
				TimeSegment.FRÜH, "some Address", "some service 2"));
		orderManagement.save( new CateringOrder(account2, Cash.CASH, LocalDate.of(2019, 7, 1),
				TimeSegment.MITTAG, "some Address", "some service 3"));
		orderManagement.save( new CateringOrder(account2, Cash.CASH, LocalDate.of(2018, 1, 5),
				TimeSegment.ABEND, "some Address", "some service 5"));
		orderManagement.save( new CateringOrder(account2, Cash.CASH, LocalDate.of(2021, 3, 26),
				TimeSegment.MITTAG, "some Address", "some service 7"));


		orderManagement.save( new CateringOrder(account3, Cash.CASH, LocalDate.of(2018, 2, 12),
				TimeSegment.ABEND, "some Address", "some service 6"));
		orderManagement.save( new CateringOrder(account3, Cash.CASH, LocalDate.of(2019, 8, 17),
				TimeSegment.MITTAG, "some Address", "some service 7"));
		orderManagement.save( new CateringOrder(account3, Cash.CASH, LocalDate.of(2017, 12, 13),
				TimeSegment.FRÜH, "some Address", "some service 9"));
		orderManagement.save( new CateringOrder(account3,  Cash.CASH, LocalDate.of(2018, 9, 16),
				TimeSegment.ABEND, "some Address 21", "some service 4"));
		orderManagement.save( new CateringOrder(account3,  Cash.CASH, LocalDate.of(2017, 12, 1),
				TimeSegment.MITTAG, "some Address 65", "some service 8"));
	}
}