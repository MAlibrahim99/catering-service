package catering.catalog;

import org.salespointframework.catalog.Catalog;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public interface OptionCatalog extends Catalog<Option> {

	Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	Iterable<Option> findByType(OptionType type, Sort sort);

	default Iterable<Option> findByType(OptionType type) {
		return findByType(type, DEFAULT_SORT);
	}


	default Iterable<Option> findByOptionType(OptionType type) {
		return findByType(type, DEFAULT_SORT);
	}


}
