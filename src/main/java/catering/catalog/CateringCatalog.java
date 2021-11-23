package catering.catalog;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

import catering.catalog.ware.ServiceType;

public interface CateringCatalog extends Catalog<ware>{

    static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

    Iterable<ware> findByType(ServiceType type, Sort sort);

    default Iterable<ware> findByType(ServiceType type){
        return findByType(type, DEFAULT_SORT);
    }
    
}
