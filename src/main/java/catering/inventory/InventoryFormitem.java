package catering.inventory;

/**
 * Used to transfer name and quantity of an inventory item to view.
 * Has standardized and parametrized constructor and getter and setter.
 */
public class InventoryFormitem {

	private String name;
	private String quantity;

	public InventoryFormitem() {
		this.name = "";
		this.quantity = "";
	}

	public InventoryFormitem(String name, String quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
