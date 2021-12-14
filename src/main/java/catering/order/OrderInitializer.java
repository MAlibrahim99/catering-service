package catering.order;

import catering.order.payment.DebitCharge;
import catering.user.UserManagement;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.PaymentMethod;
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
		PaymentMethod paymentMethod = new DebitCharge("some description");

		UserAccount account1 = userManagement.findByUsername("user 1").getUserAccount();
		UserAccount account2 = userManagement.findByUsername("user 2").getUserAccount();

		// Bestellungen von account1 Zeile 41
		System.out.println("initilizing orders 1");
		orderManagement.save(new CateringOrder(account1, paymentMethod,
				LocalDate.of(2020, 9, 1), "some Address"));
		orderManagement.save(new CateringOrder(account1, paymentMethod,
				LocalDate.of(2019, 4, 1), "some Address"));
		orderManagement.save(new CateringOrder(account1, paymentMethod,
				LocalDate.of(2018, 10, 1), "some Address"));
		orderManagement.save(new CateringOrder(account1, paymentMethod,
				LocalDate.of(2017, 12, 1), "some Address"));

		// Bestellungenn vom account2 Zeile 42
		System.out.println("initilizing orders 1");
		orderManagement.save(new CateringOrder(account2, paymentMethod,
				LocalDate.of(2018, 6, 21), "some Address"));
		orderManagement.save(new CateringOrder(account2, paymentMethod,
				LocalDate.of(2018, 7, 23), "some Address"));
		orderManagement.save(new CateringOrder(account2, paymentMethod,
				LocalDate.of(2018, 9, 16), "some Address"));
		orderManagement.save(new CateringOrder(account2, paymentMethod,
				LocalDate.of(2018, 8, 5), "some Address"));

	}
}