package catering.catalog;

import org.salespointframework.catalog.Catalog;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * extends {@link Catalog} from Salespoint and adds a functionality to search by option type
 */
@Service
public interface OptionCatalog extends Catalog<Option> {

	Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	/**
	 * search function for options
	 * @param type the required {@link OptionType}
	 * @param sort the sorting strategy (descending by product identifier by default)
	 * @return an {@link Iterable} of all {@link Option} from the {@link OptionCatalog}, that meet the search criteria
	 */
	Iterable<Option> findByType(OptionType type, Sort sort);

	default Iterable<Option> findByType(OptionType type) {
		return findByType(type, DEFAULT_SORT);
	}

}
