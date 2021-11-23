package catering.order;
import org.salespointframework.inventory.QInventoryItem;
import org.salespointframework.order.*;
import org.springframework.validation.Errors;

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

public class OrderController {


private final OrderManagement<Order> orderManagement;

	OrderController(OrderManagement<Order> orderManagement) {

		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.orderManagement = orderManagement;
	}
}


