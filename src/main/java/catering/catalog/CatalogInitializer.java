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

		LOG.info("Creating default catalog entries.");

        cateringCatalog.save(new Ware("Eventcatering", Money.of(500, EURO), "fda", ServiceType.EVENTCATERING));
        cateringCatalog.save(new Ware("PartyService", Money.of(400, EURO), "sad" ,ServiceType.PARTYSERVICE));
        cateringCatalog.save(new Ware("Rent a cook", Money.of(150, EURO),"hdg" ,ServiceType.RENTACOOK));
        cateringCatalog.save(new Ware("Mobilebreakfast", Money.of(100, EURO),"hgh" ,ServiceType.MOBILEBREAKFAST));

        catalog.save(new Options("Blumen", Money.of(5, EURO), OptionType.GOODS, new String[] {"eventcatering"}));
		catalog.save(new Options("Servietten", Money.of(0.2, EURO), OptionType.GOODS, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));

		catalog.save(new Options("Dekoration", Money.of(3, EURO), OptionType.EQUIP, new String[] {"eventcatering"}));
		catalog.save(new Options("Geschirr", Money.of(5, EURO), OptionType.EQUIP, new String[] {"eventcatering", "partyservice", "mobilebreakfast"}));
		catalog.save(new Options("Tischtücher", Money.of(30, EURO), OptionType.EQUIP, new String[] {"eventcatering"}));

		catalog.save(new Options("Buffet", Money.of(10, EURO), OptionType.FOOD, new String[] {"eventcatering"}));
		catalog.save(new Options("Galadinner", Money.of(25, EURO), OptionType.FOOD, new String[] {"eventcatering"}));

		catalog.save(new Options("Schinkenplatte", Money.of(20, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Käseplatte", Money.of(12.5, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Eierplatte", Money.of(14, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Fischplatte", Money.of(25, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Obstplatte", Money.of(10, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Salatplatte", Money.of(10, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Sushi", Money.of(100, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Pizza", Money.of(90, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Meeresfrüchte", Money.of(130, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Torten-Auswahl", Money.of(120, EURO), OptionType.FOOD, new String[] {"partyservice"}));
		catalog.save(new Options("Alkoholfreie Getränke", Money.of(9, EURO), OptionType.FOOD, new String[] {"eventcatering",
                "partyservice", "mobilebreakfast"}));
		
		catalog.save(new Options("Frühstück", Money.of(10, EURO), OptionType.FOOD, new String[] {"mobilebreakfast"}));
    }

}
