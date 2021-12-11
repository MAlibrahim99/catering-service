package catering.inventory;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final OptionCatalog catalog;

	public InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, OptionCatalog catalog) {

		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(catalog, "OptionCatalog must not be null!");

		this.inventory = inventory;
		this.catalog = catalog;
	}

	@Override
	public void initialize() {

		inventory.save(new UniqueInventoryItem(catalog.findByName("Blumen").stream().findFirst().get(),
				Quantity.of(50)));
		inventory.save(new UniqueInventoryItem(catalog.findByName("Servietten").stream().findFirst().get(),
				Quantity.of(500)));

		inventory.save(new UniqueInventoryItem(catalog.findByName("Dekoration").stream().findFirst().get(),
				Quantity.of(50)));
		inventory.save(new UniqueInventoryItem(catalog.findByName("Geschirr").stream().findFirst().get(),
				Quantity.of(100)));
		inventory.save(new UniqueInventoryItem(catalog.findByName("Tischtücher").stream().findFirst().get(),
				Quantity.of(20)));
	}
}
