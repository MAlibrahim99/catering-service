package catering.order;
import org.salespointframework.inventory.QInventoryItem;
import org.salespointframework.order.*;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import catering.catalog.Option;
import catering.catalog.Ware;
import catering.catalog.Ware.ServiceType;
import catering.catalog.services.Eventcatering;
import catering.catalog.services.Mobilebreakfast;
import catering.catalog.services.Partyservice;


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
	String addToCart1(Eventcatering eventcatering, @RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord1){
		cart.clear();
		int guestcount = number / 10;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 4;
        int waitercount = guestcount * 5;
		System.out.println(ord1.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));
		for(Option o : catalog.findByName("Servietten")){
			cart.addOrUpdateItem(o, eventcatering.getServiette());
		}
		for(Option o : catalog.findByName("Geschirr")){
			cart.addOrUpdateItem(o, eventcatering.getDishes());
		}
		for(Option o : catalog.findByName("Blumen")){
			cart.addOrUpdateItem(o, eventcatering.getFlowers());
		}
		for(Option o : catalog.findByName("Deokration")){
			cart.addOrUpdateItem(o, eventcatering.getDecoration());
		}
		for(Option o : catalog.findByName("Tischtücher")){
			cart.addOrUpdateItem(o, eventcatering.getTablecloth());
		}
		for(Option o : catalog.findByName("Buffet")){
			cart.addOrUpdateItem(o, eventcatering.getBuffet());
		}
		for(Option o : catalog.findByName("Galadinner")){
			cart.addOrUpdateItem(o, eventcatering.getGaladinner());
		}
		return "redirect:/orderreview";
	}

	@PostMapping("/cartadd2")
	String addToCart2(@RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord2, Partyservice partyservice){
		cart.clear();
		int guestcount = number / 10;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 3;
        int waitercount = guestcount * 4;
		System.out.println(ord2.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));

		for(Option o : catalog.findByName("Servietten")){
			cart.addOrUpdateItem(o, partyservice.getServiette());
		}
		for(Option o : catalog.findByName("Geschirr")){
			cart.addOrUpdateItem(o, partyservice.getDishes());
		}
		for(Option o : catalog.findByName("Schinkenplatte")){
			cart.addOrUpdateItem(o, partyservice.getHamplate());
		}
		for(Option o : catalog.findByName("Käseplatte")){
			cart.addOrUpdateItem(o, partyservice.getCheeseplate());
		}
		for(Option o : catalog.findByName("Eierplatte")){
			cart.addOrUpdateItem(o, partyservice.getEggplate());
		}
		for(Option o : catalog.findByName("Fischlatte")){
			cart.addOrUpdateItem(o, partyservice.getFishplate());
		}
		for(Option o : catalog.findByName("Obstplatte")){
			cart.addOrUpdateItem(o, partyservice.getFruitplate());
		}
		for(Option o : catalog.findByName("Salatplatte")){
			cart.addOrUpdateItem(o, partyservice.getSaladplate());
		}
		for(Option o : catalog.findByName("Sushi")){
			cart.addOrUpdateItem(o, partyservice.getSushi());
		}
		for(Option o : catalog.findByName("Pizza")){
			cart.addOrUpdateItem(o, partyservice.getPizza());
		}
		for(Option o : catalog.findByName("Meeresfrüchte")){
			cart.addOrUpdateItem(o, partyservice.getSeafood());
		}
		return "redirect:/orderreview";
	}


	@PostMapping("/cartadd3")
	//String addToCart3(Model model, @RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart , @RequestParam("date") LocalDate date){
	String addToCart3(Model model, @RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord3){
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
	String addToCart4(@RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord4, @ModelAttribute ("mobilebreakfast") Mobilebreakfast mobilebreakfast){
		cart.clear();
		int guestcount = number / 3;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = 1;
        int waitercount = guestcount;

		System.out.println(mobilebreakfast.getDishes());
		for(Option o : catalog.findByName("Servietten")){
			cart.addOrUpdateItem(o, mobilebreakfast.getServiette());
		}
		for(Option o : catalog.findByName("Geschirr")){
			cart.addOrUpdateItem(o, mobilebreakfast.getDishes());
		}
		for(Option o : catalog.findByName("Frühstück")){
			cart.addOrUpdateItem(o, mobilebreakfast.getBreakfast());
		}
		

		/*HashMap<Options, Integer> mblist = new HashMap<>();
		for(Options o : catalog.findAll()){
			if(o.getName().contains("mobilebreakfast")){
				mblist.put(o, value);
			}*/
		


		System.out.println(ord4.toString());

		cart.addOrUpdateItem(ware, Quantity.of(number));
		//cart.addOrUpdateItem(option, Quantity.of(number2));
		return "redirect:/orderreview";
	}

	@GetMapping("/orderreview")
	String orderreview(){
		return "orderreview";
	}

	@GetMapping("/eventcateringform")
	String eventcateringform(Model model, order order1, Eventcatering eventcatering){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.EVENTCATERING));
		model.addAttribute("option", catalog.findByCategory("eventcatering"));
		model.addAttribute("eventcatering", eventcatering);
		model.addAttribute("order", order1);
		return "eventcateringform";
	}

	@GetMapping("/partyserviceform")
	String partyserviceform(Model model, order order2, Partyservice partyservice){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.PARTYSERVICE));
		model.addAttribute("option", catalog.findByCategory("partyservice"));
		model.addAttribute("partyservice", partyservice);
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
		model.addAttribute("option", catalog.findByCategory("rent a cook"));
		model.addAttribute("order", order3);
		//model.addAttribute("catalog", list1);
		//model.addAttribute("catalog", cCatalog);
		
		return "rentacookform";
	}

	@GetMapping("/mobilebreakfastform")
	String mobilebreakfastform(Model model, order order4, Mobilebreakfast mobilebreakfast){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.MOBILEBREAKFAST));
		model.addAttribute("option", catalog.findByCategory("mobilebreakfast"));
		model.addAttribute("mobilebreakfast", mobilebreakfast);
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
	String orderform5(Model model, order order4, Mobilebreakfast mobilebreakfast){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.MOBILEBREAKFAST));
		model.addAttribute("option", catalog.findByCategory("mobilebreakfast"));
		model.addAttribute("mobilebreakfast", mobilebreakfast);
		model.addAttribute("order", order4);
		return "orderform5";
	}

	


}