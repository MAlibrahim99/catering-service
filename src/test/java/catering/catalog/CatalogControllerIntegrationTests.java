package catering.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerIntegrationTests {
	@Autowired
	MockMvc mvc;
	@Autowired
	CatalogController controller;
	@Autowired
	OptionCatalog catalog;

	@Test
	void showsHeadline() throws Exception {
		mvc.perform(get("/eventcatering")) //
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Eventcatering")));
	}

	@Test
	void showsOption() throws Exception {
		mvc.perform(get("/partyservice")) //
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Servietten")));
	}

	@Test
	void showsInformationtext() throws Exception {
		mvc.perform(get("/rentacook")) //
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Mieten sie Küchen- und Servicepersonal,")));
	}

	@Test
	void sampleMvcIntegrationTest() throws Exception {

		mvc.perform(get("/mobilebreakfast")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getCorrectOptions() {
		Model model = new ExtendedModelMap();

		String htmlReturn = controller.getPrices("eventcatering", model);
		assertThat(htmlReturn).isEqualTo("edit_prices");

		PriceEditForm form = (PriceEditForm) model.asMap().get("form");

		assertThat(form.getService()).isEqualTo("eventcatering");
		assertThat(form.getOptionList()).hasSize(9);

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void editCorrectPrices() {
		catalog.save(new Option("testOption", Money.of(1, EURO), 1, OptionType.GOODS, new String[] {}));

		List<OptionFormitem> options = new ArrayList<>();
		options.add(new OptionFormitem("testOption", 23f));

		PriceEditForm priceEditForm = new PriceEditForm(options, "eventcatering");
		Model model = new ExtendedModelMap();

		controller.editPrices(priceEditForm, model);

		assertThat(catalog.findByName("testOption").stream().findFirst().get().getPrice()).isEqualTo(Money.of(23, EURO));

		catalog.delete(catalog.findByName("testOption").stream().findFirst().get());
	}

	@Test
	void preventUnauthorizedPriceEditAccess() throws Exception {

		mvc.perform(get("/edit/eventcatering"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void adminHasPriceEditAccess() throws Exception {

		mvc.perform(get("/edit/eventcatering"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Preise bearbeiten")))
				.andExpect(model().attribute("form", is(not(empty()))));
	}
}
