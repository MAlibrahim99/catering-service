package catering.order;
import org.salespointframework.inventory.QInventoryItem;
import org.salespointframework.order.*;
import org.springframework.validation.Errors;

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
import java.util.Objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.hibernate.jdbc.WorkExecutor;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
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
import catering.user.Position;
import catering.user.User;
import catering.user.UserManagement;
import catering.user.UserRepository;

@Controller
//@PreAuthorize(value = "isAuthenticated()")
public class OrderController {
	private OrderManagement<CateringOrder> orderManagement;
	private OrderManagement<Order> oOrderManagement;
	private CateringOrderRepository orderRepository;
	private CateringCatalog cCatalog;
	private OptionCatalog catalog;
	private UserRepository userRepository;

	public OrderController(UserRepository userRepository, OrderManagement<Order> oOrderManagement, OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository, CateringCatalog cCatalog, OptionCatalog catalog) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;

		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.oOrderManagement = oOrderManagement;
		this.cCatalog = cCatalog;
		this.catalog = catalog;

		this.userRepository = userRepository;
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
	String addToCart1(Model model, Eventcatering eventcatering, @RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord1){
		cart.clear();
		int guestcount = number / 10;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 4;
        int waitercount = guestcount * 5;
		ord1.setChefcount(chefcount);
		ord1.setWaitercount(waitercount);
		System.out.println(ord1.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord1);
		model.addAttribute("orderOut", new order());
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
		return "orderreview";
	}

	@PostMapping("/cartadd2")
	String addToCart2(Model model, @RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord2, Partyservice partyservice){
		cart.clear();
		int guestcount = number / 10;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = guestcount * 3;
        int waitercount = guestcount * 4;
		ord2.setChefcount(chefcount);
		ord2.setWaitercount(waitercount);
		System.out.println(ord2.toString());
		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord2);
		model.addAttribute("orderOut", new order());

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
		return "orderreview";
	}


	@PostMapping("/cartadd3")
	//String addToCart3(Model model, @RequestParam("pid") ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart , @RequestParam("date") LocalDate date){
	String addToCart3(Model model, @RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord3){
		cart.clear();
		int guestcount = number / 5;
		System.out.println(number);
		if (guestcount == 0){
			guestcount = 1;
		}
		System.out.println(guestcount);
		int chefcount = guestcount * 2;
        int waitercount = guestcount * 2;
		ord3.setChefcount(chefcount);
		ord3.setWaitercount(waitercount);
		
		System.out.println(ord3.toString());
		System.out.println(ord3.getTime());
		
		/*ArrayList<User> staffList = new ArrayList<>();
		Iterable<User> staff = userRepository.getUserByPositionIn(List.of(Position.COOK, Position.EXPERIENCED_WAITER, Position.WAITER));
		for (User u : staff){
			System.out.println(u.workCount);
			System.out.println(u.getUserAccount());
			System.out.println(u);
			staffList.add(u);
		}*/


		/*Iterator<User> iStafflist = staffList.iterator();
		while (iStafflist.hasNext()){
			User u1 = iStafflist.next();
			User u2 = iStafflist.next();
			if (u1.workCount < u2.workCount){
				lowestWorkcount = u1;
				u2 = iStafflist.next();
			}
			else if (u1.workCount == u2.workCount){
				lowestWorkcount = u1;
				u2 = iStafflist.next();
			}
			else{
				lowestWorkcount = u2;
				u1 = iStafflist.next();
			}
		}*/
		/*System.out.println("--------------------------------------------");
		for (int i=0; i<staffList.size();i++){
			staffList.get(i).workCount = i;
			System.out.println(staffList.get(i));
			System.out.println(i);
			System.out.println(staffList.get(i).workCount);
		}
		
		for (int k=0; k>staffList.size()-1; k++){
			for (int i=0; i>staffList.size()-k; i++){
				if (staffList.get(i).workCount > staffList.get(k).workCount){
					Collections.swap(staffList, i, k);
				}
				
			}

		}
		
		int count =0;
		System.out.println(ord3.waitercount);
		System.out.println(ord3.chefcount);
		for (User u : staffList){
			System.out.println(u);
			count++;
			System.out.println(count);
		}*/

		/*if (chefcount <= chefs && waitercount <= waiter){               //Funktion aus Inventar für Personal benötigt
			System.out.println("Bestellung wird aufgegeben");           // return hinzufügen 
			return "orderrewview";
		}
		else{
			System.out.println("Bestellung kann nicht aufgegeben werden");
			return "redirect:/rentacookform";

		*/
		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord3);
		model.addAttribute("orderOut", new order());
		//model.addAttribute("date", date);
		
		return "orderreview";
	}

	@PostMapping("/cartadd4")
	String addToCart4(Model model, @RequestParam("pid") Ware ware, @RequestParam("number") int number, @ModelAttribute Cart cart, @ModelAttribute ("order") order ord4, @ModelAttribute ("mobilebreakfast") Mobilebreakfast mobilebreakfast){
		cart.clear();
		int guestcount = number / 3;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = 1;
        int waitercount = guestcount;
		ord4.setChefcount(chefcount);
		ord4.setWaitercount(waitercount);
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
		model.addAttribute("order", ord4);
		model.addAttribute("orderOut", new order());
		//cart.addOrUpdateItem(option, Quantity.of(number2));
		return "orderreview";
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
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Errors help, @ModelAttribute ("orderOut") order orderOut) {

		return userAccount.map(account -> {
			var order = new Order(account, Cash.CASH);

			cart.addItemsTo(order);

			oOrderManagement.payOrder(order);
			oOrderManagement.completeOrder(order);


			System.out.println("----------------");
			System.out.println(orderOut.toString());

			ArrayList<User> staffList = new ArrayList<>();


			sort(staffList, staffList.size());
			System.out.println(staffList.size());
			
			ArrayList<User> orderStaffList = new ArrayList<>();
			Streamable<User> staff = userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER, Position.WAITER));
			System.out.println(staff.toList().size());
			for (User u : staff){
				staffList.add(u);
			}
			
			//Streamable<User> staffTest = userRepository.getUserByPositionInOrderByWorkcount(List.of(Position.COOK));
			//System.out.println(staffTest.toList().size());

			/*or (int k=0; k>staffList.size()-1; k++){
				for (int i=0; i>staffList.size()-k; i++){
					if (staffList.get(i).workCount > staffList.get(k).workCount){
						Collections.swap(staffList, i, k);
					}
				}
			}*/

			ArrayList<User> chefList = new ArrayList<>();
			Streamable<User> chef = userRepository.getUserByPositionIn(List.of(Position.COOK));
			for (User u : chef){
				chefList.add(u);
			}


			sort(staffList, staffList.size());
			sort(chefList, chefList.size());
			System.out.println("oooooooooooooooooooooooooooo");
			System.out.println(staffList.size());
			System.out.println(chefList.size());
			for (User u: staffList){
				System.out.println(u);
			}
			
			/*for (int k=0; k>chefList.size()-1; k++){
				for (int i=0; i>chefList.size()-k; i++){
					if (chefList.get(i).workCount > chefList.get(k).workCount){
						Collections.swap(chefList, i, k);
					}
				}
			}*/

			
			/*for (int i=0; i<orderOut.getWaitercount(); i++){
				if(i>staffList.size()){
					break;
				}
				orderStaffList.add(staffList.get(i));
			}

			for (int i=0; i<orderOut.getChefcount(); i++){
				if(i>chefList.size()){
					break;
				}
				orderStaffList.add(chefList.get(i));
			}
			
			
			for (int k=0; k>staffList.size()-1; k++){
				for (int i=0; i>staffList.size()-k; i++){
					if (staffList.get(i).workCount > staffList.get(k).workCount){
						Collections.swap(staffList, i, k);
				}
				
			}

		}
			
			
			*/

			


			if(chefList.size() >= orderOut.getChefcount() && staffList.size() >= orderOut.getWaitercount()){
				
				
				ArrayList<User> allstaff = new ArrayList<>();
				//allstaff.addAll(staffList.subList(0, orderOut.getWaitercount()-1));
				//orderOut.setStafflist(allstaff);
				for (int i=0; i<orderOut.getChefcount();i++){
					allstaff.add(chefList.get(i));
				}
				for (int i=0; i<orderOut.getWaitercount();i++){
					allstaff.add(staffList.get(i));
				}
				for (User u : allstaff){
					u.setWorkcount(u.getWorkcount()+1);
					System.out.println(u);
					System.out.println(u.getWorkcount());
					userRepository.save(u);
					
				}

				orderOut.setStafflist(allstaff);
				System.out.println("llllllllllllllllllll");
				System.out.println(userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER, Position.WAITER,Position.COOK)).toList().size());

				for (User u: userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER, Position.WAITER,Position.COOK)).toList()){
					System.out.println(u);
					System.out.println(u.getWorkcount());
				}
			}


			

			

			
			
			
			/*System.out.println("+++++++++++++++++++++");
			for (int i=0; i<orderOut.getStafflist().size();i++){
				System.out.println(orderOut.getStafflist().get(i));
			}*/
			
			

			System.out.println(orderOut.getChefcount());
			System.out.println(orderOut.getWaitercount());

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




	@GetMapping("/orderform5")
	String orderform5(Model model, order order4, Mobilebreakfast mobilebreakfast){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.MOBILEBREAKFAST));
		model.addAttribute("option", catalog.findByCategory("mobilebreakfast"));
		model.addAttribute("mobilebreakfast", mobilebreakfast);
		model.addAttribute("order", order4);
		return "orderform5";
	}

	
	public void sort(ArrayList<User> list, int n){
		if (n==0){
			return;
		}
		if (n ==1){
			return;
		}
		
		for (int i=0; i<n-1; i++){
			if (list.get(i).workCount>list.get(i+1).workCount){
				Collections.swap(list, i, i+1);
			}
		}
		sort(list, n-1);

	}

}
