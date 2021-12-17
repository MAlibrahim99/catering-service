package catering.order;

public class OrderFormitem {

	private String name;
	private float price;
	private int personCount;
	private int amount;

	public OrderFormitem() {
		this.name = "";
		this.price = 0f;
		this.personCount = 1;
		this.amount = 0;
	}

	public OrderFormitem(String name, float price, int personCount, int amount) {
		this.name = name;
		this.price = price;
		this.personCount = personCount;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getPersonCount() {
		return personCount;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
