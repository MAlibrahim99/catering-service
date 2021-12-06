package catering.catalog;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

import catering.catalog.Ware.ServiceType;

public interface CateringCatalog extends Catalog<Ware>{

    static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

    Iterable<Ware> findByType(ServiceType type, Sort sort);

    default Iterable<Ware> findByType(ServiceType type){
        return findByType(type, DEFAULT_SORT);
    }
    
}
