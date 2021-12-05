package catering.inventory;

import catering.catalog.Option;
import catering.catalog.OptionCatalog;
import catering.catalog.OptionType;

import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class InventoryController {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final OptionCatalog catalog;

	public InventoryController(UniqueInventory<UniqueInventoryItem> inventory, OptionCatalog catalog) {

		this.inventory = inventory;
		this.catalog = catalog;
	}

	@GetMapping("/inventory")
	public String getInventory(Model model) {

		Streamable<UniqueInventoryItem> goodsStreamable = Streamable
				.of(catalog.findByType(OptionType.GOODS))
				.map(element -> inventory.findByProductIdentifier(element.getId()).get());

		Streamable<UniqueInventoryItem> equipStreamable = Streamable
				.of(catalog.findByType(OptionType.EQUIP))
				.map(element -> inventory.findByProductIdentifier(element.getId()).get());

		List<InventoryFormitem> goods = new ArrayList<>();
		for (UniqueInventoryItem item : goodsStreamable) {
			goods.add(new InventoryFormitem(item.getProduct().getName(), item.getQuantity().toString()));
		}

		List<InventoryFormitem> equip = new ArrayList<>();
		for (UniqueInventoryItem item : equipStreamable) {
			equip.add(new InventoryFormitem(item.getProduct().getName(), item.getQuantity().toString()));
		}

		InventoryEditForm form = new InventoryEditForm();
		form.setGoods(goods);
		form.setEquip(equip);

		model.addAttribute("form", form);

		return "inventory";
	}

	@PostMapping("/inventory")
	public String editInventory(@ModelAttribute("form") InventoryEditForm form, Model model) {

		for (InventoryFormitem inventoryFormitem : form.getGoods()) {
			saveInventoryItem(inventoryFormitem);
		}

		for (InventoryFormitem inventoryFormitem : form.getEquip()) {
			saveInventoryItem(inventoryFormitem);
		}

		return "redirect:/inventory";
	}

	private void saveInventoryItem(InventoryFormitem inventoryFormitem) {

		Option option = (Option) catalog.findByName(inventoryFormitem.getName()).stream().findFirst().get();
		UniqueInventoryItem item = inventory.findByProduct(option).get();

		if (inventoryFormitem.getQuantity().equals("")) {
			inventoryFormitem.setQuantity("0");
		}

		Quantity quantityInput = Quantity.of(Integer.parseInt(inventoryFormitem.getQuantity()));

		if (item.getQuantity().add(quantityInput).isNegative()) {
			item.decreaseQuantity(item.getQuantity());
		} else {
			item.increaseQuantity(quantityInput);
		}

		inventory.save(item);
	}
}
