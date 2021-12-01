package catering.order;

import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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
		Map<OrderIdentifier, String> orderTypes = new HashMap<>();

		/*for(CateringOrder order : userOrders){
			Ware ware = (Ware) order.getOrderLines().toList().get(0);
			orderTypes.put(order.getId(), ware.getDescription);
		}
		model.addAttribute("orderTypes", orderTypes);*/
		model.addAttribute("orderForm", new CateringOrder());
		model.addAttribute("userOrders", userOrders);
		return "order-history";
	}

	@GetMapping("/cancel-order")
	@PreAuthorize(value="hasAnyRole('ADMIN', 'CUSTOMER')")
	public String cancelOrder(@LoggedIn UserAccount account, @RequestParam("orderId") String orderId){
		Iterable<CateringOrder> orders = orderManagement.findBy(account);
		CateringOrder cateringOrder;
		for(CateringOrder order: orders){
			if(order.getId().toString().equals(orderId)){
				orderManagement.cancelOrder(order, "None");
				orderManagement.save(order);
				System.out.println("order with id " + orderId + " is canceled: " + order.isCanceled());
				break;
			}
		}
		return "redirect:/order-history";
	}
}
