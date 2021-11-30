package catering.catalog;

import static org.salespointframework.core.Currencies.EURO;

import com.mysema.commons.lang.Assert;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
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

	private final OptionCatalog catalog;

	public CatalogController(OptionCatalog catalog) {
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
		String picture = "/resources/img/event-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Eventcatering";
		model.addAttribute("headline", headline);


		model.addAttribute("catalog", catalog.findByCategory("eventcatering"));
		return "catalog";
	}
	@GetMapping("/partyservice")
	public String party(Model model) {
		String picture = "/resources/img/party-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Partyservice";
		model.addAttribute("headline", headline);

		model.addAttribute("catalog", catalog.findByCategory("partyservice"));
		return "catalog";
	}
	@GetMapping("/rentacook")
	public String cook(Model model) {
		String picture = "/resources/img/cook-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Rent-A-Cook";
		model.addAttribute("headline", headline);

		model.addAttribute("catalog", catalog.findByCategory("rentacook"));
		return "catalog";
	}
	@GetMapping("/mobilebreakfast")
	public String mobile(Model model) {
		String picture = "/resources/img/mobile-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Mobile Breakfast";
		model.addAttribute("headline", headline);

		model.addAttribute("catalog", catalog.findByCategory("mobilebreakfast"));
		return "catalog";
	}





}
