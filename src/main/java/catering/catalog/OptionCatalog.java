package catering.catalog;

import org.salespointframework.catalog.Catalog;

import org.springframework.data.domain.Sort;


public interface OptionCatalog extends Catalog<Options> {

	Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	Iterable<Options> findByType(OptionType type, Sort sort);

	default Iterable<Options> findByOptionType(OptionType type) {
		return findByType(type, DEFAULT_SORT);
	}


}