package catering.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * an object to transfer inventory items to the view
 */
public class InventoryEditForm {

	private List<InventoryFormitem> goods;
	private List<InventoryFormitem> equip;

	public InventoryEditForm() {
		this.goods = new ArrayList<>();
		this.equip = new ArrayList<>();
	}

	/**
	 * a parametrised constructor
	 * @param goods List of all inventory items with the option type goods
	 * @param equip List of all inventory items with the option type equip
	 */
	public InventoryEditForm(List<InventoryFormitem> goods, List<InventoryFormitem> equip) {
		this.goods = goods;
		this.equip = equip;
	}

	/**
	 * getter and setter
	 */
	public List<InventoryFormitem> getGoods() {
		return goods;
	}

	public void setGoods(List<InventoryFormitem> goods) {
		this.goods = goods;
	}

	public List<InventoryFormitem> getEquip() {
		return equip;
	}

	public void setEquip(List<InventoryFormitem> equip) {
		this.equip = equip;
	}
}
