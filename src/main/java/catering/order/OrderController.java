package catering.order;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.catalog.OptionType;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestBody;



@Controller
@PreAuthorize(value = "isAuthenticated()")
@SessionAttributes("cart")
public class OrderController {

	private final OrderManagement<CateringOrder> orderManagement;
	private final CateringOrderRepository orderRepository;
	private final OptionCatalog catalog;
	private final UserRepository userRepository;
	private UniqueInventory<UniqueInventoryItem> inventory;

	public OrderController(OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository, 
							OptionCatalog catalog, UserRepository userRepository, UniqueInventory<UniqueInventoryItem> inventory) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;
		this.catalog = catalog;
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
		}
		else{
			return "redirect:/login";
		}
	}

	@ModelAttribute("cart")
    Cart initializeCart(){
        return new Cart();
    }

	@GetMapping("/order/{service}")
	public String getOrderForm(@PathVariable String service, Model model, CateringOrder order) {

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
		model.addAttribute("order", order);

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

	@PostMapping("/cartadd")
	String addtoCart(Model model, @ModelAttribute ("order") CateringOrder order, @ModelAttribute ("form") OrderForm form,
					 @ModelAttribute Cart cart ){
		cart.clear();

		int guestcount = form.getPersons();
		System.out.println(form.getPersons());


		if (form.getService().equals("eventcatering")){
			System.out.println("True");
		}
		
		if (form.getService().equals("eventcatering")){
			guestcount = guestcount / 10;
			if (guestcount == 0){
				guestcount = 1;
			}
			int chefcount = guestcount * 4;
			System.out.println(chefcount);
			int waitercount = guestcount * 5;
			System.out.println(waitercount);
			order.setChefcount(chefcount);
        	order.setWaitercount(waitercount);
		}
		else if (form.getService().equals("partyservice")){
			guestcount = guestcount / 10;
			if (guestcount == 0){
				guestcount = 1;
			}
			int chefcount = guestcount * 3;
			System.out.println(chefcount);
			int waitercount = guestcount * 4;
			System.out.println(waitercount);
			order.setChefcount(chefcount);
        	order.setWaitercount(waitercount);
		}
		else if (form.getService().equals("rentacook")){
			guestcount = guestcount / 5;
			if (guestcount == 0){
				guestcount = 1;
			}
			int chefcount = guestcount * 2;
			System.out.println(chefcount);
			int waitercount = guestcount * 2;
			System.out.println(waitercount);
			order.setChefcount(chefcount);
        	order.setWaitercount(waitercount);
		}
		else if (form.getService().equals("mobilebreakfast")){
			guestcount = guestcount / 3;
			if (guestcount == 0){
				guestcount = 1;
			}
			int chefcount = 1;
			System.out.println(chefcount);
			int waitercount = guestcount;
			System.out.println(waitercount);
			order.setChefcount(chefcount);
        	order.setWaitercount(waitercount);
		}


        
		


		System.out.println("-------------------------------");
		System.out.println(order.toString());

		Streamable<User> chefcountRep = userRepository.getUserByPositionIn(List.of(Position.COOK)); 
        Streamable<User> waitercountRep = userRepository.getUserByPositionIn(List.of(Position.WAITER, Position.EXPERIENCED_WAITER));
        if(chefcountRep.toList().size() < order.getChefcount() || waitercountRep.toList().size() < order.getWaitercount()){
            if (form.getService().equals("eventcatering")){
				return "redirect:/order/eventcatering";
			}
			else if (form.getService().equals("partyservice")){
				return "redirect:/order/partyservice";
			}
			else if (form.getService().equals("rentacook")){
				return "redirect:/order/rentacook";
			}
			else if (form.getService().equals("mobilebreakfast")){
				return "redirect:/order/mobilebreakast";
			}
			else{
				return "redirect:/";
			}
        }

		for (OrderFormitem optionItem : form.getFoodList()) {
			if (optionItem.getAmount() != 0){
				System.out.println(optionItem.getName() + " : " + optionItem.getAmount());
				cart.addOrUpdateItem(catalog.findByName(optionItem.getName()).stream().findFirst().get(), Quantity.of(optionItem.getAmount()));
			}
		}

		for (OrderFormitem optionItem : form.getEquipList()) {
			if (optionItem.getAmount() != 0){
				System.out.println(optionItem.getName() + " : " + optionItem.getAmount());
				cart.addOrUpdateItem(catalog.findByName(optionItem.getName()).stream().findFirst().get(), Quantity.of(optionItem.getAmount()));
			}		
		}

		model.addAttribute("order", order);
		//model.addAttribute("orderOut", new CateringOrder());
		model.addAttribute("form", form);
	


		return "orderreview";

	}



	@PostMapping("/clearcart")
    String clear(@ModelAttribute Cart cart, @ModelAttribute ("form") OrderForm form){
			System.out.println(form.getService());
			if (form.getService().equals("rentacook")){
                cart.clear();
                return "redirect:/order/rentacook";
            }else if (form.getService().equals("eventcatering")){
                cart.clear();
                return "redirect:/order/eventcatering";
            }else if (form.getService().equals("partyservice")){
                cart.clear();
                return "redirect:/order/partyservice";
            }else if (form.getService().equals("mobilebreakfast")){
                cart.clear();
                return "redirect:/order/mobilebreakfast";
            }
        cart.clear();
        return "redirect:/";
    }

	@PostMapping("/checkout")
    String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, Errors help,
               @ModelAttribute ("order") CateringOrder orderOut) {

				System.out.println(orderOut.toString());


        	return userAccount.map(account -> {
				var order = new CateringOrder(account, Cash.CASH, orderOut.getCompletionDate(),orderOut.getTime(), orderOut.getAddress(), orderOut.getService());

				for (CartItem ci : cart){
					if(catalog.findByName(ci.getProductName()).stream().findFirst().get().getType() == OptionType.EQUIP){
						saveInventoryItem(catalog.findByName(ci.getProductName()).stream().findFirst().get(), ci.getQuantity());
					}
				}

				cart.addItemsTo(order);
				
				orderManagement.payOrder(order);
				orderManagement.completeOrder(order);

				System.out.print("checkout tostring()");
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
					//TODO 
					//orderOut.setStafflist(allstaff);

					
					System.out.println("order-stafflist");
					for (User u : allstaff){
						order.addToAllocStaff(u);
						System.out.println(u);
						System.out.println(u.getPosition());
					}


					System.out.println("llllllllllllllllllll");
					/*System.out.println(userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER,
							Position.WAITER,Position.COOK)).toList().size());

					for (User u: userRepository.getUserByPositionIn(List.of(Position.EXPERIENCED_WAITER,
							Position.WAITER,Position.COOK)).toList()){
						System.out.println(u);
						System.out.println(u.getWorkcount());
					}*/

					System.out.println(order.toString());
				}

				



				if (help.hasErrors()){
					return "cart";
				}
				cart.clear();
	
				return "redirect:/";
			}).orElse("redirect:/");
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

		private void saveInventoryItem(Option option, Quantity quantity) {

			/*Option option;
			if (cartItem.getProductName() == "Eventcatering"  cartItem.getProductName() == "PartyService" 
				cartItem.getProductName() == "Rent a cook" || cartItem.getProductName() == "Mobilebreakfast"){
	
				}*/
			//Option option = catalog.findByName(cartItem.getProductName()).stream().findFirst().get();
			UniqueInventoryItem item = inventory.findByProduct(option).get();
	
	
			Quantity quantityInput = quantity;
	
				item.increaseQuantity(quantity);
	
			inventory.save(item);
		}

}


