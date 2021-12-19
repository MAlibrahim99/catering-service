package catering.inventory;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.catalog.OptionType;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.salespointframework.core.Currencies.EURO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerIntegrationTests {

	@Autowired
	InventoryController controller;
	@Autowired
	UniqueInventory<UniqueInventoryItem> inventory;
	@Autowired
	OptionCatalog catalog;
	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(roles = "ADMIN")
	public void getCorrectInventoryItems() {

		Model model = new ExtendedModelMap();

		String htmlReturn = controller.getInventory(model);

		assertThat(htmlReturn).isEqualTo("inventory");

		InventoryEditForm form = (InventoryEditForm) model.asMap().get("form");

		assertThat(form.getGoods()).hasSize(2);
		assertThat(form.getEquip()).hasSize(3);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void editCorrectInventoryQuantities() {

		catalog.save(new Option("testGood", Money.of(1, EURO), 1, OptionType.GOODS, new String[] {}));
		catalog.save(new Option("testEquip", Money.of(1, EURO), 1, OptionType.EQUIP, new String[] {}));

		inventory.save(new UniqueInventoryItem(catalog.findByName("testGood").stream().findFirst().get(), Quantity.of(50)));
		inventory.save(new UniqueInventoryItem(catalog.findByName("testEquip").stream().findFirst().get(), Quantity.of(100)));

		List<InventoryFormitem> goods = new ArrayList<>();
		goods.add(new InventoryFormitem("testGood", "3"));
		List<InventoryFormitem> equip = new ArrayList<>();
		equip.add(new InventoryFormitem("testEquip", "-3"));

		InventoryEditForm form = new InventoryEditForm(goods, equip);
		Model model = new ExtendedModelMap();

		String htmlReturn = controller.editInventory(form, model);
		assertThat(htmlReturn).isEqualTo("redirect:/inventory");

		UniqueInventoryItem testGood = inventory.findByProduct(catalog.findByName("testGood").stream().findFirst().get()).get();
		assertThat(testGood.getQuantity().getAmount().intValue()).isEqualTo(53);

		UniqueInventoryItem testEquip = inventory.findByProduct(catalog.findByName("testEquip").stream().findFirst().get()).get();
		assertThat(testEquip.getQuantity().getAmount().intValue()).isEqualTo(97);

		inventory.delete(inventory.findByProduct(catalog.findByName("testGood").stream().findFirst().get()).get());
		catalog.delete(catalog.findByName("testGood").stream().findFirst().get());
		inventory.delete(inventory.findByProduct(catalog.findByName("testEquip").stream().findFirst().get()).get());
		catalog.delete(catalog.findByName("testEquip").stream().findFirst().get());
	}

	@Test
	void preventUnauthorizedAccess() throws Exception {

		mvc.perform(get("/inventory"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(roles = "STAFF")
	void onlyAdminAccess() throws Exception {

		mvc.perform(get("/inventory"))
				.andExpect(status().is(403));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void adminHasAccess() throws Exception {

		mvc.perform(get("/inventory"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Inventar")))
				.andExpect(model().attribute("form", is(not(empty()))));
	}
}
