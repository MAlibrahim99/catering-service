package catering.catalog;

import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import org.javamoney.moneta.Money;

/**
 * Extends {@link Product} from Salespoint and adds personCount and {@link OptionType} to it.
 */
@Entity
public class Option extends Product {

	private int personCount;
	private OptionType type;

	@SuppressWarnings({"unused", "deprecation"})
	private Option() {
	}

	/**
	 * Constructor uses the categories from {@link Product} to save the assigned service types from
	 * @param name name of the option
	 * @param price price of the option
	 * @param personCount number of persons who use one of this product
	 * @param type option type, either goods, equipment or food
	 * @param services the service types; can be eventcatering, partyserice, mobile breakfast or rent-a-cook
	 */
	public Option(String name, Money price, int personCount, OptionType type, String[] services) {

		super(name, price);

		this.personCount = personCount;
		this.type = type;

		for (String s : services) {
			addCategory(s);
		}
	}

	/**
	 * getters for the option type and person count
	 */
	public OptionType getType() {
		return type;
	}

	public int getPersonCount() {
		return personCount;
	}
}
