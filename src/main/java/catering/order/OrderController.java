package catering.order;

import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@PreAuthorize(value = "isAuthenticated()")
public class OrderController {
	private OrderManagement<CateringOrder> orderManagement;
	private CateringOrderRepository orderRepository;

	public OrderController(OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;
	}

	@GetMapping(value = "/order-history")
	@PreAuthorize(value = "hasRole('CUSTOMER')")
	public String getOrderHistoryForCurrentUser(@LoggedIn UserAccount account, Model model) {
		Iterable<CateringOrder> userOrders = orderManagement.findBy(account);
		Map<OrderIdentifier, String> orderTypes = new HashMap<>();
		// TODO die Implementation ist nicht vollständig, denn Infos für Bestellungen werden aus anderen Klassen benötigt
		/*for(CateringOrder order : userOrders){
			Ware ware = (Ware) order.getOrderLines().toList().get(0);
			orderTypes.put(order.getId(), ware.getDescription);
		}
		model.addAttribute("orderTypes", orderTypes);*/
//		model.addAttribute("orderForm", new CateringOrder());
		model.addAttribute("userOrders", userOrders);
		return "order-history";
	}

	@GetMapping("/cancel-order")
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'CUSTOMER')")
	public String cancelOrder(@LoggedIn UserAccount account, @RequestParam("orderId") String orderId) {
		if (account != null && orderId != null) {
			Iterable<CateringOrder> orders = orderManagement.findBy(account);
			CateringOrder cateringOrder;
			for (CateringOrder order : orders) {
				if (Objects.requireNonNull(order.getId()).toString().equals(orderId)) {
					orderManagement.cancelOrder(order, "None");
					orderManagement.save(order);
					System.out.println("order with id " + orderId + " is canceled: " + order.isCanceled());
					break;
				}
			}
			return "redirect:/order-history";
		}else{
			return "redirect:/login";
		}
	}

	@PostMapping("/setstatus")
	String list2(@RequestParam("status") String status){

		System.out.println(status);
		return "redirect:/order-list/" + status;
	}

	@GetMapping("/order-list/{status}")
	String list(Model model, @PathVariable("status") OrderStatus status){

		Iterable<CateringOrder> orders = orderManagement.findBy(status);
		model.addAttribute("orders", orders);


		/*Iterable<CateringOrder> ordersOpen = orderManagement.findBy(OrderStatus.OPEN);
		model.addAttribute("ordersOpen", ordersOpen);
		Iterable<CateringOrder> ordersPaid = orderManagement.findBy(OrderStatus.PAID);
		model.addAttribute("ordersPaid", ordersPaid);
		Iterable<CateringOrder> ordersCompleted = orderManagement.findBy(OrderStatus.COMPLETED);
		model.addAttribute("ordersCompleted", ordersCompleted);
		Iterable<CateringOrder> ordersCancelled = orderManagement.findBy(OrderStatus.CANCELLED);
		model.addAttribute("ordersCancelled", ordersCancelled); */
		return "order-list";
	}

	@GetMapping("/order-details/{order-id}")
	String details(Model model, @PathVariable("order-id") OrderIdentifier parameter){
		model.addAttribute("order", orderManagement.get(parameter).get());
		model.addAttribute("account", orderManagement.get(parameter));
		return "order-details";
	}
}
