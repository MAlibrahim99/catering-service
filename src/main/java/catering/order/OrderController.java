package catering.order;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.catalog.OptionType;

import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;

import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@PreAuthorize(value = "isAuthenticated()")
public class OrderController {

	private final OrderManagement<CateringOrder> orderManagement;
	private final CateringOrderRepository orderRepository;
	private final OptionCatalog catalog;

	public OrderController(OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository, OptionCatalog catalog) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;
		this.catalog = catalog;
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
		}
		else{
			return "redirect:/login";
		}
	}

	@GetMapping("/order/{service}")
	public String getOrderForm(@PathVariable String service, Model model) {

		Assert.isTrue((service.equals("eventcatering") || service.equals("partyservice") || service.equals("rentacook") ||
				service.equals("mobilebreakfast")), "Service must be valid");

		Streamable<Option> optionStreamable = catalog.findByCategory(service);

		List<OrderFormitem> foodFormitemList = new ArrayList<>();
		List<OrderFormitem> equipFormitemList = new ArrayList<>();
		for (Option option : optionStreamable) {
			if (option.getType() == OptionType.FOOD) {
				foodFormitemList.add(new OrderFormitem(option.getName(), option.getPrice().getNumber().numberValue(Float.class), option.getPersonCount(), 0));
			}
			if (option.getType() == OptionType.EQUIP || option.getType() == OptionType.GOODS) {
				equipFormitemList.add(new OrderFormitem(option.getName(), option.getPrice().getNumber().numberValue(Float.class), option.getPersonCount(), 0));
			}
		}

		OrderForm form = new OrderForm();
		form.setService(service);
		form.setFoodList(foodFormitemList);
		form.setEquipList(equipFormitemList);

		model.addAttribute("form", form);

		return "order_form";
	}

	@PostMapping("/buy")
	public String buy(@ModelAttribute("form") OrderForm form, Model model) {

		System.out.println("Service Type: " + form.getService());
		System.out.println("Number of Persons: " + form.getPersons());

		for (OrderFormitem optionItem : form.getFoodList()) {
			System.out.println(optionItem.getName() + " : " + optionItem.getAmount());
		}

		for (OrderFormitem optionItem : form.getEquipList()) {
			System.out.println(optionItem.getName() + " : " + optionItem.getAmount());
		}
		return "redirect:/";
	}
}
