package catering.order;

import org.salespointframework.order.OrderManagement;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize(value="isAuthenticated()")
public class OrderController {
	private OrderManagement<CateringOrder> orderManagement;
	private CateringOrderRepository orderRepository;

	public OrderController(OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;
	}

	@GetMapping(value="/order-history")
	@PreAuthorize(value="hasRole('CUSTOMER')")
	public String getOrderHistoryForCurrentUser(@LoggedIn UserAccount account, Model model){
		Iterable<CateringOrder> userOrders = orderManagement.findBy(account);
		model.addAttribute("userOrders", userOrders);
		return "order-history";
	}


}
