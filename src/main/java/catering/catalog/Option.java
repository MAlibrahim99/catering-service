package catering.catalog;

import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import org.javamoney.moneta.Money;


@Entity
public class Options extends Product {

	private OptionType type;

	@SuppressWarnings({"unused", "deprecation"})
	private Options() {
	}

	public Options(String name, Money price, OptionType type, String[] services) {

		super(name, price);

		this.type = type;

		for (String s : services) {
			addCategory(s);
		}
	}

	public OptionType getType() {
		return type;
	}
}