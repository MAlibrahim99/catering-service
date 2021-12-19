package catering.order;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.catalog.OptionType;
import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.user.Position;
import catering.user.User;
import catering.user.UserRepository;

import org.apache.tomcat.websocket.server.UriTemplate;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.support.ConsoleWritingMailSender;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;

import org.springframework.data.util.Streamable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;


@Controller
@PreAuthorize(value = "isAuthenticated()")
@SessionAttributes("cart")
public class OrderController {

	private final OrderManagement<CateringOrder> orderManagement;
	private final CateringOrderRepository orderRepository;
	private final OptionCatalog catalog;
	private final UserRepository userRepository;
	private UniqueInventory<UniqueInventoryItem> inventory;
	private IncomeOverview incomeOverview;

	public OrderController(OrderManagement<CateringOrder> orderManagement, CateringOrderRepository orderRepository,
							OptionCatalog catalog, UserRepository userRepository, IncomeOverview incomeOverview,
						   UniqueInventory<UniqueInventoryItem> inventory) {
		this.orderManagement = orderManagement;
		this.orderRepository = orderRepository;
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
		model.addAttribute("userOrders", userOrders);
		return "order-history";
	}

	@GetMapping("/cancel-order")
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'CUSTOMER')")
	public String cancelOrder(@LoggedIn UserAccount account, @RequestParam("orderId") OrderIdentifier orderId) {
		if (account == null || orderId == null) {
			return "redirect:/login";
		}

		// Bestellung ist da, und aktueller Nutzer hat diese Bestellung in seinem Verlauf
		if(orderManagement.contains(orderId) && orderManagement.get(orderId).get().getUserAccount().equals(account)){
			CateringOrder cateringOrder =  orderManagement.get(orderId).get();
			ConsoleWritingMailSender mailSender = new ConsoleWritingMailSender();
			boolean isCancelDoneBefore3Days = cateringOrder.getCompletionDate().minusDays(3L).isAfter(LocalDate.now());
			orderManagement.cancelOrder(cateringOrder, "None");

			if(isCancelDoneBefore3Days) {
				mailSender.send(cancelConfirmationMessage(cateringOrder, true));
			}else{
				mailSender.send(cancelConfirmationMessage(cateringOrder, false));
			}
		}
		return "redirect:/order-history";
	}

	@ModelAttribute("cart")
    Cart initializeCart(){
        return new Cart();
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

		model.addAttribute("totalIncome", incomeOverview.totalIncome(start, end));
		model.addAttribute("statusPercentages", incomeOverview.statusPercentages(start, end));
		model.addAttribute("servicePercentages" , incomeOverview.servicePercentages(start, end));
		model.addAttribute("start", start);
		model.addAttribute("end", end);

		return "income-overview";

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
				foodFormitemList.add(new OrderFormitem(option.getName(), option.getPrice().getNumber().numberValue(Float.class), option.getPersonCount(), 1));
			}
			if (option.getType() == OptionType.EQUIP || option.getType() == OptionType.GOODS) {
				equipFormitemList.add(new OrderFormitem(option.getName(), option.getPrice().getNumber().numberValue(Float.class), option.getPersonCount(), 1));
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

		if(order.getTimeString().equals("Früh")){
			order.setTime(TimeSegment.FRÜH);
		}else if(order.getTimeString().equals("Mittag")){
			order.setTime(TimeSegment.MITTAG);
		}else if(order.getTimeString().equals("Abend")){
			order.setTime(TimeSegment.ABEND);
		}


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
		}else if (form.getService().equals("partyservice")){
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
		}else if (form.getService().equals("rentacook")){
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
		}else if (form.getService().equals("mobilebreakfast")){
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
		System.out.println(order.getTimeString());

		Streamable<User> chefcountRep = userRepository.getUserByPositionIn(List.of(Position.COOK));
        Streamable<User> waitercountRep = userRepository.getUserByPositionIn(List.of(Position.WAITER,
				Position.EXPERIENCED_WAITER));
        if(chefcountRep.toList().size() < order.getChefcount() ||
				waitercountRep.toList().size() < order.getWaitercount()){
			//model.addAttribute("not", )
            if (form.getService().equals("eventcatering")){
				return "redirect:/order/eventcatering";
			}else if (form.getService().equals("partyservice")){
				return "redirect:/order/partyservice";
			}else if (form.getService().equals("rentacook")){
				return "redirect:/order/rentacook";
			}else if (form.getService().equals("mobilebreakfast")){
				return "redirect:/order/mobilebreakast";
			}else{
				return "redirect:/";
			}
        }

		for (OrderFormitem optionItem : form.getFoodList()) {
			if (optionItem.getAmount() != 0){
				System.out.println(optionItem.getName() + " : " + optionItem.getAmount());
				cart.addOrUpdateItem(catalog.findByName(optionItem.getName()).stream().findFirst().get(),
						Quantity.of(optionItem.getAmount()));
			}
		}

		for (OrderFormitem optionItem : form.getEquipList()) {
			if (optionItem.getAmount() != 0){
				System.out.println(optionItem.getName() + " : " + optionItem.getAmount());
				cart.addOrUpdateItem(catalog.findByName(optionItem.getName()).stream().findFirst().get(),
						Quantity.of(optionItem.getAmount()));
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
				var order = new CateringOrder(account, Cash.CASH, orderOut.getCompletionDate(),
						orderOut.getTime(), orderOut.getAddress(), orderOut.getService());

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
				System.out.println(cart.getPrice());


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



	//get status from buttons and redirect to correct order-list
	@PostMapping("/setstatus")
	String list2(@RequestParam("status") String status){
		return "redirect:/order-list/" + status;
	}

	@GetMapping("/order-list/{status}")
	String list(Model model, @PathVariable("status") OrderStatus status){
		Iterable<CateringOrder> orders = orderManagement.findBy(status);
		model.addAttribute("orders", orders);
		return "order-list";
	}

	@GetMapping("/order-details/{order-id}")
	String details(Model model, @PathVariable("order-id") OrderIdentifier parameter){
		model.addAttribute("order", orderManagement.get(parameter).get());
		model.addAttribute("account", orderManagement.get(parameter));
		return "order-details";
	}

	public static Date LocalDateIntoDate(LocalDate local){
		ZoneId defaultZoneID = ZoneId.of("Europe/Paris");
		return Date.from(local.atStartOfDay(defaultZoneID).toInstant());
	}

	public static int getWeekNumberFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getYearNumberFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	//calendar setup, so the starting week is the current week
	@GetMapping("/calendar2")
	String calendar2(){
		LocalDate now = LocalDate.now();
		Date date = LocalDateIntoDate(now);
		int week = getWeekNumberFromDate(date);
		int year = getYearNumberFromDate(date);
		String YW = String.format(year + "-" + week);
		return "redirect:/calendar/" + YW ;
	}

	//Button for changing the week with redirection to calendar
	@PostMapping("/setweek/{YW}")
	String setweek(@RequestParam("button") String button, @PathVariable("YW") String YW){
		String [] split = YW.split("-");
		int year = Integer.parseInt(split[0]);
		int week = Integer.parseInt(split[1]);

		if (button.equals("prev")){
			week -= 1;
			if (week == 0) {
				year -= 1;
				try {
					week = 53;
					String date = String.format(year + "-W" + week + "-1");
					LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ISO_WEEK_DATE);
				} catch (Exception ex) {
					week = 52;
				}
			}
		} else {
			week += 1;
			if (week == 53) {
				try {
					String date = String.format(year + "-W" + week + "-1");
					LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ISO_WEEK_DATE);
				} catch (Exception ex) {
					week = 1;
					year += 1;
				}
			}
			if (week == 54) {
				week = 1;
				year += 1;
			}
		}
		YW = String.format(year + "-" + week);
		return "redirect:/calendar/" + YW;
	}

	//calendar with orders
	@GetMapping("/calendar/{YW}")
	String calendar(Model model, @PathVariable("YW") String YW){
		String [] split = YW.split("-");
		int year = Integer.parseInt(split[0]);
		int week = Integer.parseInt(split[1]);

		LocalDate date = LocalDate.of(year, Month.JANUARY, 10);
		LocalDate dayInWeek = date.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week);

		model.addAttribute("YW", YW);
		model.addAttribute("week", week);
		model.addAttribute("year", year);
		model.addAttribute("monday", dayInWeek.with(DayOfWeek.MONDAY));
		model.addAttribute("tuesday", dayInWeek.with(DayOfWeek.TUESDAY));
		model.addAttribute("wednesday", dayInWeek.with(DayOfWeek.WEDNESDAY));
		model.addAttribute("thursday", dayInWeek.with(DayOfWeek.THURSDAY));
		model.addAttribute("friday", dayInWeek.with(DayOfWeek.FRIDAY));
		model.addAttribute("saturday", dayInWeek.with(DayOfWeek.SATURDAY));
		model.addAttribute("sunday", dayInWeek.with(DayOfWeek.SUNDAY));

		Iterable<CateringOrder> fruh = orderRepository.findByOrderStatusAndTime(OrderStatus.PAID, TimeSegment.FRÜH);
		model.addAttribute("fruh", fruh);
		Iterable<CateringOrder> mittag = orderRepository.findByOrderStatusAndTime(OrderStatus.PAID, TimeSegment.MITTAG);
		model.addAttribute("mittag", mittag);
		Iterable<CateringOrder> abend = orderRepository.findByOrderStatusAndTime(OrderStatus.PAID, TimeSegment.ABEND);
		model.addAttribute("abend", abend);

		return "calendar";
	}

	SimpleMailMessage cancelConfirmationMessage(CateringOrder cateringOrder, boolean cancellationBefore3Days){

		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setSubject("\n\nStornierungsbestätigung Ihrer Bestellung am " +
				cateringOrder.getCompletionDate().toString());
		String message = "\nSehr geehrte(r) Frau/Herr " + cateringOrder.getUserAccount().getLastname()
				+ ",\n\nSie haben erfolgreich ihre Bestellung storniert."
				+ "\nFälligkeitsdatum: " + cateringOrder.getCompletionDate() + "."
				+ "\nGebuchter Betrag: " + cateringOrder.getTotal().getNumber();
		if(!cancellationBefore3Days) {
			message = message + "\nZu entrichtende Stornierungsgebühren in Höhe von 40% betragen: "
					+ cateringOrder.getTotal().multiply(40L).divide(100L);
		}
		message += "\nSie werden  den gebuchten Betrag innerhalb 7 Werktage bekommen."
				+ "\n\nMit freundlichen Grüßen."
				+ "\n\nMampf-Team";

		simpleMessage.setText(message);
		return simpleMessage;
	}
}
