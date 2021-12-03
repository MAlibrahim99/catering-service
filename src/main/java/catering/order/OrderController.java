package catering.order;
import org.salespointframework.inventory.QInventoryItem;
import org.salespointframework.order.*;
import org.springframework.validation.Errors;

import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.format.annotation.DateTimeFormat;
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

import catering.catalog.CateringCatalog;
import catering.catalog.OptionCatalog;
import catering.catalog.ware;
import catering.catalog.ware.ServiceType;


@Controller
//@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
public class OrderController {


private final OrderManagement<Order> orderManagement;

	private final CateringCatalog cCatalog;
	private final OptionCatalog catalog;

	OrderController(OrderManagement<Order> orderManagement, CateringCatalog cCatalog, OptionCatalog catalog) {


		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.orderManagement = orderManagement;
		this.cCatalog = cCatalog;
		this.catalog = catalog;
	}

	

	
	@ModelAttribute("cart")
	Cart initializeCart(){
		/*Cart cart = new Cart();
		for (ware ware1 : cCatalog.findByName("Eventcatering")){
		cart.addOrUpdateItem(ware1, 1);
		}
		return /*new Cart() cart;*/
		return new Cart();
	}

	@PostMapping("/cartadd1")
	String addToCart1(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord1){
		cart.clear();
		int guestcount = number / 10;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 4;
        int waitercount = guestcount * 5;
		System.out.println(ord1.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}

	@PostMapping("/cartadd2")
	String addToCart2(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord2){
		cart.clear();
		int guestcount = number / 10;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 3;
        int waitercount = guestcount * 4;
		System.out.println(ord2.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}


	@PostMapping("/cartadd3")
	//String addToCart3(Model model, @RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart , @RequestParam("date") LocalDate date){
	String addToCart3(Model model, @RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord3){
		cart.clear();
		int guestcount = number / 5;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 2;
        int waitercount = guestcount * 2;
		System.out.println(ord3.toString());
		
		/*if (chefcount <= chefs && waitercount <= waiter){               //Funktion aus Inventar für Personal benötigt
			System.out.println("Bestellung wird aufgegeben");           // return hinzufügen 
			return "orderrewview";
		}
		else{
			System.out.println("Bestellung kann nicht aufgegeben werden");
			return "redirect:/rentacookform";

		*/
		cart.addOrUpdateItem(ware, Quantity.of(number));
		//model.addAttribute("date", date);
		
		return "orderreview";
	}

	@PostMapping("/cartadd4")
	String addToCart4(@RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord4){
		cart.clear();
		int guestcount = number / 3;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = 1;
        int waitercount = guestcount;
		System.out.println(ord4.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));
		return "redirect:/orderreview";
	}

	@GetMapping("/orderreview")
	String orderreview(){
		return "orderreview";
	}

	@GetMapping("/eventcateringform")
	String eventcateringform(Model model, order order1){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.EVENTCATERING));
		model.addAttribute("order", order1);
		return "eventcateringform";
	}

	@GetMapping("/partyserviceform")
	String partyserviceform(Model model, order order2){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.PARTYSERVICE));
		model.addAttribute("order", order2);
		return "partyserviceform";
	}

	@GetMapping("/rentacookform")
	String rentacookform(Model model, order order3){
		/*ArrayList<Option> list1 = new ArrayList<>();
		for(Option o : catalog.findAll()){
			if(o.getName().contains("Rent a cook")){
				list1.add(o);
			}
		}*/
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.RENTACOOK));
		model.addAttribute("order", order3);
		//model.addAttribute("catalog", list1);
		//model.addAttribute("catalog", cCatalog);
		
		return "rentacookform";
	}

	@GetMapping("/mobilebreakfastform")
	String mobilebreakfastform(Model model, order order4){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.MOBILEBREAKFAST));
		model.addAttribute("option", catalog.findByCategory("mobilebreakfast"));
		model.addAttribute("order", order4);
		return "mobilebreakfastform";
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
		}).orElse("redirect:/");

	}

	@PostMapping("/clearcart")
	String clear(@ModelAttribute Cart cart){
		for (CartItem ci : cart){
			if (ci.getProductName().contains("Rent a cook")){
				cart.clear();
				return "redirect:/rentacookform";
			}
			else if (ci.getProductName().contains("Eventcatering")){
				cart.clear();
				return "redirect:/eventcateringform";
			}
			else if (ci.getProductName().contains("PartyService")){
				cart.clear();
				return "redirect:/partyserviceform";
			}
			else if (ci.getProductName().contains("Mobilebreakfast")){
				cart.clear();
				return "redirect:/mobilebreakfastform";
			}
		}
		cart.clear();
		return "redirect:/";
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