package catering.inventory;


import catering.catalog.Option;
import catering.catalog.OptionCatalog;


import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * initializes the inventory with options from the {@link OptionCatalog}
 */
@Component
@Order(25)
class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final OptionCatalog catalog;


	public InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory,
								OptionCatalog catalog) {

		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(catalog, "OptionCatalog must not be null!");
		Assert.notNull(catalog, "CateringCatalog must not be null!");

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
		
		inventory.save(new UniqueInventoryItem(catalog.findByName("Buffet").stream().findFirst().get(),
        Quantity.of(20)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Galadinner").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Schinkenplatte").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Käseplatte").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Eierplatte").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Fischplatte").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Obstplatte").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Salatplatte").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Sushi-Abend").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Pizza-Runde").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Meeresfrüchte-Menü").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Torten-Auswahl").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Alkoholfreie Getränke").stream().findFirst().get(),
                Quantity.of(100)));
        inventory.save(new UniqueInventoryItem(catalog.findByName("Alkoholische Getränke").stream().findFirst().get(),
                Quantity.of(100)));
		inventory.save(new UniqueInventoryItem(catalog.findByName("Standard-Frühstück").stream().findFirst().get(),
                Quantity.of(100)));
	}
}
