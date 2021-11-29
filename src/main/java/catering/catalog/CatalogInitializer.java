package catering.catalog;

import org.salespointframework.core.DataInitializer;
import static org.salespointframework.core.Currencies.EURO;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.javamoney.moneta.Money;


@Component
public class CatalogInitializer implements DataInitializer {

	private final OptionCatalog catalog;

	public CatalogInitializer(OptionCatalog catalog) {

		Assert.notNull(catalog, "Catalog must not be null!");

		this.catalog = catalog;
	}

	@Override
	public void initialize() {

		if (catalog.findAll().iterator().hasNext()) {
			return;
		}

		catalog.save(new Option("Blumen", Money.of(5, EURO), OptionType.GOODS, new String[] {"eventcatering"}));
		catalog.save(new Option("Servietten", Money.of(0.2, EURO), OptionType.GOODS, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));

		catalog.save(new Option("Dekoration", Money.of(3, EURO), OptionType.EQUIP, new String[] {"eventcatering"}));
		catalog.save(new Option("Geschirr", Money.of(5, EURO), OptionType.EQUIP, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));
		catalog.save(new Option("Tischtücher", Money.of(30, EURO), OptionType.EQUIP, new String[] {"eventcatering"}));

		catalog.save(new Option("Buffet", Money.of(10, EURO), OptionType.FOOD, new String[] {"eventcatering"}));
		catalog.save(new Option("Galadinner", Money.of(25, EURO), OptionType.FOOD, new String[] {"eventcatering"}));

		catalog.save(new Option("Schinkenplatte", Money.of(20, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Käseplatte", Money.of(12.5, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Eierplatte", Money.of(14, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Fischplatte", Money.of(25, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Obstplatte", Money.of(10, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Salatplatte", Money.of(10, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Sushi", Money.of(100, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Pizza", Money.of(90, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Option("Meeresfrüchte", Money.of(130, EURO), OptionType.FOOD, new String[] {"partyservice"}));

		catalog.save(new Option("Frühstück", Money.of(10, EURO), OptionType.FOOD, new String[] {"mobilebreakfast"}));
	}
}
