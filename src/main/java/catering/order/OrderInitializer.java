package catering.order;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.user.UserManagement;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
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
	private OptionCatalog catalog;
	private UniqueInventory<UniqueInventoryItem> inventory;
	private final Random random = new Random();


	public OrderInitializer(@Qualifier("persistentOrderManagement") OrderManagement<CateringOrder> management,
							UserManagement userManagement, OptionCatalog catalog,
							UniqueInventory<UniqueInventoryItem> inventory) {
		this.orderManagement = management;
		this.userManagement = userManagement;
		this.catalog = catalog;
		this.inventory = inventory;
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

		int randUser, randDay, randMonth, randYear, randDayTime, randServiceType, randStatus, randQuantity, randOption;
		int currentYear = LocalDate.now().getYear();
		TimeSegment [] timeSegments = TimeSegment.values();
		String [] serviceType = {"Eventcatering", "MobileBreakfast", "Partyservice", "RentACook"};
		CateringOrder order;
		String [] eventCateringOptions = {"Buffet", "Galadinner", "Servietten", "Geschirr"};
		String [] partyOptions = {"Schinkenplatte", "Käseplatte", "Fischplatte", "Pizza-Runde"};
		String [] rentACookOptions = {"Servietten", "Dekoration", "Geschirr", "Tischtücher"};

		Cart cart;
		UniqueInventoryItem item;

		for (int i = 0; i < 20; i++) {
			randUser = random.nextInt(3);
			randDay = random.nextInt(28) +1;
			randMonth = random.nextInt(12) + 1;
			randYear = currentYear + 2 - random.nextInt(4);
			randDayTime = random.nextInt(3);
			randServiceType = random.nextInt(4);
			randStatus = random.nextInt(5);

			order = new CateringOrder(accounts.get(randUser),
					Cash.CASH,
					LocalDate.of(randYear, randMonth, randDay),
					timeSegments[randDayTime]
					,"some Address " + random.nextInt(50),
					serviceType[randServiceType]);
			cart = new Cart();
			if(i < 10) {
				bookOrder(order, eventCateringOptions, cart);
			}
			if(i >= 10 && i < 15) {
				bookOrder(order, partyOptions, cart);
			}
			if(i >= 15) {
				bookOrder(order, rentACookOptions, cart);
			}

			if(LocalDate.of(randYear, randMonth, randMonth).isBefore(LocalDate.now())) {
				orderManagement.payOrder(order);
				if(randStatus <= 3) {
					orderManagement.completeOrder(order);
				}else{
					orderManagement.cancelOrder(order, "None");
				}
			}else{
				orderManagement.payOrder(order);
				}
			orderManagement.save(order);
		}
	}

	private void bookOrder(CateringOrder order, String[] rentACookOptions, Cart cart) {
		int randQuantity;
		int randOption;
		UniqueInventoryItem item;
		for (int j = 0; j < 5; j++) {
			randQuantity = random.nextInt(7);
			randOption = random.nextInt(4);
			Option product = catalog.findByName(rentACookOptions[randOption]).stream().findFirst().get();
			item = inventory.findByProduct(product).get();
			cart.addOrUpdateItem(product, randQuantity);
			item.increaseQuantity(Quantity.of(randQuantity));
			inventory.save(item);
			cart.addItemsTo(order);
			cart.clear();
		}
	}
}