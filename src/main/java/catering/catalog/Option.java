package catering.catalog;

import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import org.javamoney.moneta.Money;


@Entity
public class Option extends Product {

	private int personCount;
	private OptionType type;

	@SuppressWarnings({"unused", "deprecation"})
	private Option() {
	}

	public Option(String name, Money price, int personCount, OptionType type, String[] services) {

		super(name, price);

		this.personCount = personCount;
		this.type = type;

		for (String s : services) {
			addCategory(s);
		}
	}

	public OptionType getType() {
		return type;
	}

	public int getPersonCount() {
		return personCount;
	}
}
