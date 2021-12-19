package catering.catalog;

import static org.salespointframework.core.Currencies.EURO;

import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import org.javamoney.moneta.Money;




@Controller
public class CatalogController {

	private static final Quantity NONE = Quantity.of(0);

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final BusinessTime businessTime;
	private final OptionCatalog catalog;


	CatalogController( UniqueInventory<UniqueInventoryItem> inventory,
					  BusinessTime businessTime, OptionCatalog catalog) {

		this.inventory = inventory;
		this.businessTime = businessTime;
		this.catalog = catalog;
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/offer")
	public String offer() {
		return "offer";
	}

	@GetMapping("/eventcatering")
	public String event(Model model) {
		String pic = "/resources/img/event-detail.jpg";
		model.addAttribute("picture", pic);
		String headline = "Eventcatering";
		String event ="Unser Cateringservice übernimmt die Verpflegung bei großen Veranstaltungen, " +
				"egal ob in Form eines Buffetts oder als Galadinner " +
				"Auf Wunsch gestaltet Mampf auch das komplette Event. Neben Speisen und " +
				"Getränken stellen wir optional Dekoration, Ausrüstung und kompetentes Personal.";
		model.addAttribute("info", event);
		model.addAttribute("headline", headline);
		model.addAttribute("catalog", catalog.findByCategory("eventcatering"));
		String order = "/order/eventcatering";
		model.addAttribute("orderformular", order);
		String price = "eventcatering";
		model.addAttribute("price", price);
		return "catalog";
	}

	@GetMapping("/partyservice")
	public String party(Model model) {
		String partypicture = "/resources/img/party-detail.jpg";
		model.addAttribute("picture", partypicture);
		String headline = "Partyservice";
		model.addAttribute("headline", headline);
		String party ="Der Partyservice von Mampf beliefert ihre privaten Feierlichkeiten mit " +
				"warmen Gerichten, kalten Platten und Desserts.";
		model.addAttribute("info", party);
		model.addAttribute("catalog", catalog.findByCategory("partyservice"));
		String order = "/order/partyservice";
		model.addAttribute("orderformular", order);
		String price = "partyservice";
		model.addAttribute("price", price);
		return "catalog";
	}

	@GetMapping("/rentacook")
	public String cook(Model model) {
		String picture = "/resources/img/cook-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Rent-A-Cook";
		model.addAttribute("headline", headline);
		String info =
				"Mieten sie Küchen- und Servicepersonal, welches bei Ihnen Zuhause, vor den Augen ihrer " +
				"Gäste, verschiedene Köstlichkeiten zubereitet. Sie müssen sich dabei weder um das " +
				"decken des Tisches kümmern, noch um den Abwasch und das Aufräumen. " +
				"Das Saubermachen übernimmt das Personal für sie. Um die Lebensmittel müssen " +
						"sich die Kunden dabei jedoch selbst kümmern.";
		model.addAttribute("info", info);
		model.addAttribute("catalog", catalog.findByCategory("rentacook"));
		String order = "/order/rentacook";
		model.addAttribute("orderformular", order);
		String price = "rentacook";
		model.addAttribute("price", price);
		return "catalog";
	}

	@GetMapping("/mobilebreakfast")
	public String mobile(Model model) {
		String picture = "/resources/img/mobile-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Mobile Breakfast";
		model.addAttribute("headline", headline);
		String mobile = "Mieten sie pro Monat eine Auswahl an Speisen und Getränken, die für die " +
				"Mitarbeiter Ihrer Firma zu festgelegten Zeiten zur Verfügung stehen.";
		model.addAttribute("info", mobile);
		model.addAttribute("catalog", catalog.findByCategory("mobilebreakfast"));
		String order = "/order/mobilebreakfast";
		model.addAttribute("orderformular", order);
		String price = "mobilebreakfast";
		model.addAttribute("price", price);
		return "catalog";
	}


	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/edit/{service}")
	public String getPrices(@PathVariable String service, Model model) {
		Assert.isTrue((service.equals("eventcatering") || service.equals("partyservice") || service.equals("rentacook") ||
				service.equals("mobilebreakfast")), "Service must be valid");

		Streamable<Option> optionStreamable = catalog.findByCategory(service);

		List<OptionFormitem> optionFormitemList = new ArrayList<>();
		for (Option option : optionStreamable) {
			optionFormitemList.add(new OptionFormitem(option.getName(), option.getPrice().getNumber().numberValue(Float.class)));
		}

		PriceEditForm form = new PriceEditForm();
		form.setOptionList(optionFormitemList);
		form.setService(service);

		model.addAttribute("form", form);

		return "edit_prices";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/editPrices")
	public String editPrices(@ModelAttribute("form") PriceEditForm form, Model model) {

		for (OptionFormitem optionItem : form.getOptionList()) {
			Option option = catalog.findByName(optionItem.getName()).stream().findFirst().get();
			option.setPrice(Money.of(optionItem.getPrice(), EURO));

			catalog.save(option);
		}

		return "redirect:/" + form.getService();
	}

}
