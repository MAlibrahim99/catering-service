package catering.catalog;

import org.salespointframework.core.DataInitializer;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import catering.catalog.Ware.ServiceType;

import static org.salespointframework.core.Currencies.*;

@Component
@Order(20)
public class CatalogInitializer implements DataInitializer{

    private static final Logger LOG = LoggerFactory.getLogger(CatalogInitializer.class);

	private final CateringCatalog cateringCatalog;
    private final OptionCatalog catalog;

	CatalogInitializer(CateringCatalog cateringCatalog, OptionCatalog catalog) {

		Assert.notNull(cateringCatalog, "CateringCatalog must not be null!");

		this.cateringCatalog = cateringCatalog;

        Assert.notNull(catalog, "Catalog must not be null!");

		this.catalog = catalog;
	}


    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
        if (cateringCatalog.findAll().iterator().hasNext()) {
			return;
		}
        if (catalog.findAll().iterator().hasNext()) {
			return;
		}

		catalog.save(new Option("Buffet", Money.of(13, EURO), 1, OptionType.FOOD, new String[] {"eventcatering"}));
		catalog.save(new Option("Galadinner", Money.of(25, EURO), 1, OptionType.FOOD, new String[] {"eventcatering"}));

		catalog.save(new Option("Schinkenplatte", Money.of(20, EURO), 5, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Käseplatte", Money.of(12.5, EURO), 3, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Eierplatte", Money.of(14, EURO), 3, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Fischplatte", Money.of(32, EURO), 6, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Obstplatte", Money.of(25, EURO), 4, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Salatplatte", Money.of(25, EURO), 4, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Sushi-Abend", Money.of(150, EURO), 10, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Pizza-Runde", Money.of(110, EURO), 10, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Meeresfrüchte-Menü", Money.of(170, EURO), 10, OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Torten-Auswahl", Money.of(120, EURO), 10, OptionType.FOOD, new String[] {"partyservice"}));

		catalog.save(new Option("Alkoholfreie Getränke", Money.of(9, EURO), 1, OptionType.FOOD, new String[] {"eventcatering",
				"partyservice", "mobilebreakfast"}));
		catalog.save(new Option("Alkoholische Getränke", Money.of(14, EURO), 1, OptionType.FOOD, new String[] {"eventcatering",
				"partyservice"}));

		catalog.save(new Option("Standard-Frühstück", Money.of(7, EURO), 1, OptionType.FOOD, new String[] {"mobilebreakfast"}));

		catalog.save(new Option("Blumen", Money.of(9, EURO), 1, OptionType.GOODS, new String[] {"eventcatering", "rentacook"}));
		catalog.save(new Option("Servietten", Money.of(0.75, EURO), 1, OptionType.GOODS, new String[] {"eventcatering",
				"partyservice", "mobilebreakfast", "rentacook"}));

		catalog.save(new Option("Dekoration", Money.of(4, EURO), 1, OptionType.EQUIP, new String[] {"eventcatering",
				"rentacook"}));
		catalog.save(new Option("Geschirr", Money.of(8, EURO), 1, OptionType.EQUIP, new String[] {"eventcatering",
				"partyservice", "mobilebreakfast", "rentacook"}));
		catalog.save(new Option("Tischtücher", Money.of(25, EURO), 6, OptionType.EQUIP, new String[] {"eventcatering",
				"rentacook"}));
	}
}
