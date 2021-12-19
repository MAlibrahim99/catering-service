package catering.inventory;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;

import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class InventoryControllerUnitTests {

	@Autowired
	InventoryController controller;
	@Autowired
	OptionCatalog catalog;
	@Autowired
	UniqueInventory<UniqueInventoryItem> inventory;

	@Test
	@WithMockUser(roles = "ADMIN")
	void saveCorrectQuantity() {
		controller.saveInventoryItem(new InventoryFormitem("Blumen", "3"));
		Option option = catalog.findByName("Blumen").stream().findFirst().get();
		UniqueInventoryItem item = inventory.findByProduct(option).get();
		assertThat(item.getQuantity().getAmount().intValue()).isEqualTo(53);
	}
}
