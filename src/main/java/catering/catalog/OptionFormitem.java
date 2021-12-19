package catering.catalog;

/**
 * Used to transfer name and price of an option to view.
 * Has standardized and parametrized constructor and getter and setter.
 */
public class OptionFormitem {
	private String name;
	private Float price;

	public OptionFormitem() {
		this.name = "";
		this.price = 0f;
	}

	public OptionFormitem(String name, Float price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
}
