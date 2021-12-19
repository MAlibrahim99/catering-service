package catering.order;

import java.util.ArrayList;
import java.util.List;

/**
 * object to transfer the available options to the view
 */
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

	/**
	 * a parametrised constructor
	 * @param service the service type of the options
	 * @param persons the number of persons that attend the planned event
	 * @param foodList a list of all options of the option type FOOD
	 * @param equipList a list of all options of the option type GOODS or EQUIP
	 */
	public OrderForm(String service, int persons, List<OrderFormitem> foodList, List<OrderFormitem> equipList) {
		this.service = service;
		this.persons = persons;
		this.foodList = foodList;
		this.equipList = equipList;
	}

	/**
	 * getters and setters for the service type, the number of persons and the option lists
	 */
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
