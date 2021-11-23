package catering.catalog;

import org.salespointframework.core.DataInitializer;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import catering.catalog.ware.ServiceType;

import static org.salespointframework.core.Currencies.*;

@Component
@Order(20)
public class CatalogInitializer implements DataInitializer{

    private static final Logger LOG = LoggerFactory.getLogger(CatalogInitializer.class);

	private final CateringCatalog cateringCatalog;

	CatalogInitializer(CateringCatalog cateringCatalog) {

		Assert.notNull(cateringCatalog, "CateringCatalog must not be null!");

		this.cateringCatalog = cateringCatalog;
	}


    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
        if (cateringCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default catalog entries.");

        cateringCatalog.save(new ware("Eventcatering", Money.of(100, EURO), ServiceType.EVENTCATERING));
    }
}
