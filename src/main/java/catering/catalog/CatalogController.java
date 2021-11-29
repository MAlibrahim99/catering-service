package catering.catalog;

import static org.salespointframework.core.Currencies.EURO;

import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import org.javamoney.moneta.Money;

@Controller
public class CatalogController {

	private final OptionCatalog catalog;

	public CatalogController(OptionCatalog catalog) {
		this.catalog = catalog;
	}

	@GetMapping("/welcome")
	public String index() {
		return "welcome";
	}

	@GetMapping("/")
	public String start() {
		return "index";
	}

	@GetMapping("/offer")
	public String offer() {
		return "offer";
	}

	@GetMapping("/eventcatering")
	public String event() {
		return "catalog";
	}

	@GetMapping("/partyservice")
	public String party() {
		return "catalog";
	}

	@GetMapping("/rentacook")
	public String cook() {
		return "catalog";
	}

	@GetMapping("/mobilebreakfast")
	public String mobile() {
		return "catalog";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/edit/{service}")
	public String getPrices(@PathVariable String service, Model model) {
		Assert.isTrue((service.equals("eventcatering") || service.equals("partyservice") || service.equals("mobilebreakfast")), "Service must be valid");

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
