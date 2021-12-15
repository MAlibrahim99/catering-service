package catering.order;

import catering.catalog.CateringCatalog;
import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.catalog.Ware;
import catering.catalog.Ware.ServiceType;
import catering.catalog.services.Eventcatering;
import catering.catalog.services.Mobilebreakfast;
import catering.catalog.services.Partyservice;
import catering.catalog.services.Rentacook;
import catering.user.Position;
import catering.user.User;
import catering.user.UserRepository;

import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;

import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
//@PreAuthorize(value = "isAuthenticated()")
@SessionAttributes("cart")
public class OrderController {
	private OrderManagement<CateringOrder> orderManagement;
	private OrderManagement<org.salespointframework.order.Order> oOrderManagement;
	private CateringOrderRepository orderRepository;
	private CateringCatalog cCatalog;
	private OptionCatalog catalog;
	private UserRepository userRepository;
	private IncomeOverview incomeOverview;
	private UniqueInventory<UniqueInventoryItem> inventory;
	

	public OrderController(UserRepository userRepository, OrderManagement<org.salespointframework.order.Order> oOrderManagement,
						   OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository,
						   CateringCatalog cCatalog, OptionCatalog catalog, IncomeOverview incomeOverview, UniqueInventory<UniqueInventoryItem> inventory) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;

		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.oOrderManagement = oOrderManagement;
		this.cCatalog = cCatalog;
		this.catalog = catalog;
		this.incomeOverview = incomeOverview;
						
		this.userRepository = userRepository;

		this.inventory = inventory;
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

	// Initial wird eine Übersicht der letzten 30 Tage zurückgegeben, exeklusive des aktuellen Tages
	@GetMapping("/income-overview")
	public String displayIncomeOverview(@RequestParam("startDate") Optional<String> startDate,
										@RequestParam("endDate") Optional<String> endDate, Model model) {
		LocalDate start;
		LocalDate end;
		if (startDate.isEmpty() || endDate.isEmpty()) {
			start = LocalDate.now().minusDays(30L);
			end = LocalDate.now().minusDays(1L);
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			start = LocalDate.parse(startDate.get(), formatter);
			end = LocalDate.parse(endDate.get(), formatter);
		}

		if (start.isAfter(end)) {
			start = end.minusDays(30L);
		}

		model.addAttribute("totalIncome", incomeOverview.totalIncome(start, end));
		model.addAttribute("statusPercentages", incomeOverview.statusPercentages(start, end));
		model.addAttribute("start", start);
		model.addAttribute("end", end);

		return "income-overview";
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
	String addToCart1(Model model, Eventcatering eventcatering, @RequestParam("pid") Ware ware,
					  @RequestParam("number") int number, @ModelAttribute Cart cart,
					  @ModelAttribute ("order") Order ord1){
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
		Boolean enough = true;


		Streamable<User> chefcountRep = userRepository.getUserByPositionIn(List.of(Position.COOK)); 
		Streamable<User> waitercountRep = userRepository.getUserByPositionIn(List.of(Position.WAITER, Position.EXPERIENCED_WAITER));
		if(chefcountRep.toList().size() < chefcount || waitercountRep.toList().size() < waitercount){
			enough = false;
			return "redirect:/eventcateringform";
		}


		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord1);
		model.addAttribute("orderOut", new Order());


		for(Option o : catalog.findByName("Servietten")){
			cart.addOrUpdateItem(o, eventcatering.getServiette());
		}
		for(Option o : catalog.findByName("Geschirr")){
			cart.addOrUpdateItem(o, eventcatering.getDishes());
		}
		for(Option o : catalog.findByName("Blumen")){
			cart.addOrUpdateItem(o, eventcatering.getFlowers());
		}
		for(Option o : catalog.findByName("Dekoration")){
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
		for(Option o : catalog.findByName("Alkoholische Getränke")){
			cart.addOrUpdateItem(o, eventcatering.getAlk());
		}
		for(Option o : catalog.findByName("Alkoholfreie Getränke")){
			cart.addOrUpdateItem(o, eventcatering.getNoalk());
		}
		return "orderreview";
	}

	@PostMapping("/cartadd2")
	String addToCart2(Model model, @RequestParam("pid") Ware ware, @RequestParam("number") int number,
					  @ModelAttribute Cart cart, @ModelAttribute ("order") Order ord2,
					  Partyservice partyservice){
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

		Streamable<User> chefcountRep = userRepository.getUserByPositionIn(List.of(Position.COOK)); 
		Streamable<User> waitercountRep = userRepository.getUserByPositionIn(List.of(Position.WAITER, Position.EXPERIENCED_WAITER));
		if(chefcountRep.toList().size() < chefcount || waitercountRep.toList().size() < waitercount){
			return "redirect:/partyserviceform";
		}

		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord2);
		model.addAttribute("orderOut", new Order());
	

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
		for(Option o : catalog.findByName("Alkoholische Getränke")){
			cart.addOrUpdateItem(o, partyservice.getAlk());
		}
		for(Option o : catalog.findByName("Alkoholfreie Getränke")){
			cart.addOrUpdateItem(o, partyservice.getNoalk());
		}

		System.out.println("ppppppppppppppppppppp");
		for (CartItem ci : cart){
			System.out.println(ci.getProductName());
		}
		return "orderreview";
	}


	@PostMapping("/cartadd3")
	String addToCart3(Model model, @RequestParam("pid") Ware ware, Rentacook rentacook,
					  @RequestParam("number") int number, @ModelAttribute Cart cart,
					  @ModelAttribute ("order") Order ord3){
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

		Streamable<User> chefcountRep = userRepository.getUserByPositionIn(List.of(Position.COOK)); 
		Streamable<User> waitercountRep = userRepository.getUserByPositionIn(List.of(Position.WAITER, Position.EXPERIENCED_WAITER));
		if(chefcountRep.toList().size() < chefcount || waitercountRep.toList().size() < waitercount){
			return "redirect:/rentacookform";
		}

		for(Option o : catalog.findByName("Servietten")){
			cart.addOrUpdateItem(o, rentacook.getServiette());
		}
		for(Option o : catalog.findByName("Geschirr")){
			cart.addOrUpdateItem(o, rentacook.getDishes());
		}
		for(Option o : catalog.findByName("Blumen")){
			cart.addOrUpdateItem(o, rentacook.getDishes());
		}
		for(Option o : catalog.findByName("Dekoration")){
			cart.addOrUpdateItem(o, rentacook.getDecoration());
		}
		for(Option o : catalog.findByName("Tischtücher")){
			cart.addOrUpdateItem(o, rentacook.getTablecloth());
		}

		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord3);
		model.addAttribute("orderOut", new Order());
		
		return "orderreview";
	}

	@PostMapping("/cartadd4")
	String addToCart4(Model model, @RequestParam("pid") Ware ware, @RequestParam("number") int number,
					  @ModelAttribute Cart cart, @ModelAttribute ("order") Order ord4,
					  @ModelAttribute ("mobilebreakfast") Mobilebreakfast mobilebreakfast){
		cart.clear();
		int guestcount = number / 3;
		if (guestcount == 0){
			guestcount = 1;
		}
		int chefcount = 1;
        int waitercount = guestcount;

		Streamable<User> chefcountRep = userRepository.getUserByPositionIn(List.of(Position.COOK)); 
		Streamable<User> waitercountRep = userRepository.getUserByPositionIn(List.of(Position.WAITER, Position.EXPERIENCED_WAITER));
		if(chefcountRep.toList().size() < chefcount || waitercountRep.toList().size() < waitercount){
			return "redirect:/eventcateringform";
		}
		
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
		for(Option o : catalog.findByName("Alkoholfreie Getränke")){
			cart.addOrUpdateItem(o, mobilebreakfast.getNoalk());
		}

		System.out.println(ord4.toString());

		cart.addOrUpdateItem(ware, Quantity.of(number));
		model.addAttribute("order", ord4);
		model.addAttribute("orderOut", new Order());
		
		return "orderreview";
	}

	@GetMapping("/orderreview")
	String orderreview(){
		return "orderreview";
	}


	@GetMapping("/eventcateringform")
	String eventcateringform(Model model, Order order1, Eventcatering eventcatering){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.EVENTCATERING));
		model.addAttribute("option", catalog.findByCategory("eventcatering"));
		model.addAttribute("eventcatering", eventcatering);
		model.addAttribute("order", order1);
		return "eventcateringform";
	}

	@GetMapping("/partyserviceform")
	String partyserviceform(Model model, Order order2, Partyservice partyservice){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.PARTYSERVICE));
		model.addAttribute("option", catalog.findByCategory("partyservice"));
		model.addAttribute("partyservice", partyservice);
		model.addAttribute("order", order2);
		return "partyserviceform";
	}

	@GetMapping("/rentacookform")
	String rentacookform(Model model, Order order3, Rentacook rentacook){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.RENTACOOK));
		model.addAttribute("option", catalog.findByCategory("rentacook"));
		model.addAttribute("rentacook", rentacook);
		model.addAttribute("order", order3);

		return "rentacookform";
	}

	@GetMapping("/mobilebreakfastform")
	String mobilebreakfastform(Model model, Order order4, Mobilebreakfast mobilebreakfast){
		model.addAttribute("catalog", cCatalog.findByType(ServiceType.MOBILEBREAKFAST));
		model.addAttribute("option", catalog.findByCategory("mobilebreakfast"));
		model.addAttribute("mobilebreakfast", mobilebreakfast);
		model.addAttribute("order", order4);
		return "mobilebreakfastform";
	}

	@PostMapping("/checkout")
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Errors help,
			   @ModelAttribute ("orderOut") Order orderOut) {

		return userAccount.map(account -> {
			var order = new org.salespointframework.order.Order(account, Cash.CASH);

			
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			/*for (CartItem ci : cart){ 
				System.out.println("x");
				long amount = orderOut.getChefcount()/4*10;
				if (ci.getProductName().equals("Eventcatering")){
					saveInventoryItem(catalog.findByName("Blumen").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Servietten").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Dekoration").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Geschirr").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Tischtücher").stream().findFirst().get(), amount/6);
					saveInventoryItem(catalog.findByName("Buffet").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Galadinner").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Alkoholfreie Getränke").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Alkoholische Getränke").stream().findFirst().get(), amount);
					}
				else if(ci.getProductName().equals("Partyservice")){
					saveInventoryItem(catalog.findByName("Servietten").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Geschirr").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Schinkenplatte").stream().findFirst().get(), amount/5);
					saveInventoryItem(catalog.findByName("Käseplatte").stream().findFirst().get(), amount/3);
					saveInventoryItem(catalog.findByName("Eierplatte").stream().findFirst().get(), amount/3);
					saveInventoryItem(catalog.findByName("Fischplatte").stream().findFirst().get(), amount/6);
					saveInventoryItem(catalog.findByName("Obstplatte").stream().findFirst().get(), amount/4);
					saveInventoryItem(catalog.findByName("Salatplatte").stream().findFirst().get(), amount/4);
					saveInventoryItem(catalog.findByName("Sushi-Abend").stream().findFirst().get(), amount/10);
					saveInventoryItem(catalog.findByName("Pizza-Runde").stream().findFirst().get(), amount/10);
					saveInventoryItem(catalog.findByName("Meeresfrüchte-Menü").stream().findFirst().get(), amount/10);
					saveInventoryItem(catalog.findByName("Torten-Auswahl").stream().findFirst().get(), amount/10);
					saveInventoryItem(catalog.findByName("Alkoholfreie Getränke").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Alkoholische Getränke").stream().findFirst().get(), amount);
				}
				else if(ci.getProductName().equals("Rent a cook")){
					saveInventoryItem(catalog.findByName("Servietten").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Geschirr").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Blumen").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Dekoration").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Tischtücher").stream().findFirst().get(), amount/6);
					

				}
				else if(ci.getProductName().equals("Mobilebreakfast")){
					saveInventoryItem(catalog.findByName("Servietten").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Geschirr").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Alkoholfreie Getränke").stream().findFirst().get(), amount);
					saveInventoryItem(catalog.findByName("Frühstück").stream().findFirst().get(), amount);
				}
				
				
					
				
				
				
				System.out.println(ci.getProductName());
				System.out.println(ci.getQuantity());
			}*/
			
			cart.addItemsTo(order);


			

			oOrderManagement.payOrder(order);
			System.out.print(order.getOrderStatus());
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
			

			if(chefList.size() >= orderOut.getChefcount() && staffList.size() >= orderOut.getWaitercount()){
				
				
				ArrayList<User> allstaff = new ArrayList<>();
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
				System.out.println(userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER,
						Position.WAITER,Position.COOK)).toList().size());

				for (User u: userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER,
						Position.WAITER,Position.COOK)).toList()){
					System.out.println(u);
					System.out.println(u.getWorkcount());
				}
			}


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
			}else if (ci.getProductName().contains("Eventcatering")){
				cart.clear();
				return "redirect:/eventcateringform";
			}else if (ci.getProductName().contains("PartyService")){
				cart.clear();
				return "redirect:/partyserviceform";
			}else if (ci.getProductName().contains("Mobilebreakfast")){
				cart.clear();
				return "redirect:/mobilebreakfastform";
			}
		}
		cart.clear();
		return "redirect:/";
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

	private void saveInventoryItem(Option option, long amount) {

		/*Option option;
		if (cartItem.getProductName() == "Eventcatering" || cartItem.getProductName() == "PartyService" ||
			cartItem.getProductName() == "Rent a cook" || cartItem.getProductName() == "Mobilebreakfast"){

			}*/ 
        //Option option = catalog.findByName(cartItem.getProductName()).stream().findFirst().get(); 
        UniqueInventoryItem item = inventory.findByProduct(option).get();


        Quantity quantityInput = Quantity.of(amount);
        
            item.decreaseQuantity(item.getQuantity());

        inventory.save(item);
    }

}
