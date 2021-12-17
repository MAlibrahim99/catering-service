package catering.order;

import java.util.ArrayList;
import java.util.List;

public class OrderForm {

	private String service;
	private int persons;
	private List<OrderFormitem> foodList;
	private List<OrderFormitem> equipList;

	public OrderForm() {
		this.service = "";
		this.persons = 1;
		this.foodList = new ArrayList<>();
		this.equipList = new ArrayList<>();
	}

	public OrderForm(String service, int persons, List<OrderFormitem> foodList, List<OrderFormitem> equipList) {
		this.service = service;
		this.persons = persons;
		this.foodList = foodList;
		this.equipList = equipList;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public int getPersons() {
		return persons;
	}

	public void setPersons(int persons) {
		this.persons = persons;
	}

	public List<OrderFormitem> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<OrderFormitem> foodList) {
		this.foodList = foodList;
	}

	public List<OrderFormitem> getEquipList() {
		return equipList;
	}

	public void setEquipList(List<OrderFormitem> equipList) {
		this.equipList = equipList;
	}
}
