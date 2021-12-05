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

		// Preis pro 1 Person
		catalog.save(new Option("Blumen", Money.of(9, EURO), OptionType.GOODS, new String[] {"eventcatering"}));
		// Preis pro 1 Person
		catalog.save(new Option("Servietten", Money.of(0.75, EURO), OptionType.GOODS, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));

		// Preis pro 1 Person
		catalog.save(new Option("Dekoration", Money.of(4, EURO), OptionType.EQUIP, new String[] {"eventcatering"}));
		// Preis pro 1 Person
		catalog.save(new Option("Geschirr", Money.of(8, EURO), OptionType.EQUIP, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));
		// Preis pro 6 Personen
		catalog.save(new Option("Tischtücher", Money.of(25, EURO), OptionType.EQUIP, new String[] {"eventcatering"}));

		// Preis pro 1 Person
		catalog.save(new Option("Buffet", Money.of(13, EURO), OptionType.FOOD, new String[] {"eventcatering"}));
		// Preis pro 1 Person
		catalog.save(new Option("Galadinner", Money.of(25, EURO), OptionType.FOOD, new String[] {"eventcatering"}));

		// Preis pro 5 Personen
		catalog.save(new Option("Schinkenplatte", Money.of(20, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 3 Personen
		catalog.save(new Option("Käseplatte", Money.of(12.5, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 3 Personen
		catalog.save(new Option("Eierplatte", Money.of(14, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 6 Personen
		catalog.save(new Option("Fischplatte", Money.of(32, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 4 Personen
		catalog.save(new Option("Obstplatte", Money.of(25, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 4 Personen
		catalog.save(new Option("Salatplatte", Money.of(25, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 10 Personen
		catalog.save(new Option("Sushi-Abend", Money.of(150, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 10 Personen
		catalog.save(new Option("Pizza-Runde", Money.of(110, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 10 Personen
		catalog.save(new Option("Meeresfrüchte-Menü", Money.of(170, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		// Preis pro 10 Personen
		catalog.save(new Option("Torten-Auswahl", Money.of(120, EURO), OptionType.FOOD, new String[] {"partyservice"}));

		// Preis pro 1 Person
		catalog.save(new Option("Alkoholfreie Getränke", Money.of(9, EURO), OptionType.FOOD, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));
		// Preis pro 1 Person
		catalog.save(new Option("Alkoholische Getränke", Money.of(14, EURO), OptionType.FOOD, new String[] {"eventcatering", "partyservice"}));
	}
}
