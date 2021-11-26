package catering.order;
import org.salespointframework.inventory.QInventoryItem;
import org.salespointframework.order.*;
import org.springframework.validation.Errors;

import java.util.Optional;

import org.javamoney.moneta.Money;
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
import static org.salespointframework.core.Currencies.*;

import catering.catalog.ware;
import catering.catalog.ware.ServiceType;


@Controller
//@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
public class OrderController {


private final OrderManagement<Order> orderManagement;

	OrderController(OrderManagement<Order> orderManagement) {

		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.orderManagement = orderManagement;
	}

	

	
	@ModelAttribute("cart")
	Cart initializeCart(){
		return new Cart();
	}

	@PostMapping("/cartadd1")
	String addToCart1(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart){
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}

	@PostMapping("/cartadd2")
	String addToCart2(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart){
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}

	@PostMapping("/cartadd3")
	String addToCart3(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart){
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}

	@PostMapping("/cartadd4")
	String addToCart4(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart){
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}

	@GetMapping("/orderreview")
	String orderreview(){
		return "orderreview";
	}

	@GetMapping("/orderform1")
	String orderform1(){
		return "orderform1";
	}

	@GetMapping("/orderform2")
	String orderform2(){
		return "orderform2";
	}

	@GetMapping("/orderform3")
	String orderform3(){
		return "orderform3";
	}

	@GetMapping("/orderform4")
	String orderform4(){
		return "orderform4";
	}

	@PostMapping("/checkout")
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Errors help) {

		return userAccount.map(account -> {
			var order = new Order(account, Cash.CASH);

			cart.addItemsTo(order);

			orderManagement.payOrder(order);
			orderManagement.completeOrder(order);
			if (help.hasErrors()){
				return "cart";
			}
			cart.clear();

			return "redirect:/";
		}).orElse("redirect:/cart");

	}

	@PostMapping("/clearcart")
	String clear(@ModelAttribute Cart cart){
		cart.clear();
	return "orderform";
	}

	@GetMapping("/test")
	String testt(){
		return "test";
	}

	@GetMapping("/orderform5")
	String orderform5(){
		return "orderform5";
	}


	



}