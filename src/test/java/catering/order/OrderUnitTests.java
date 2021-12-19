package catering.order;

import catering.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * unit tests for {@link OrderFormitem}, {@link OrderForm} and {@link CateringOrder}
 */
public class OrderUnitTests {

	@Test
	public void OrderFormitemTest() {
		OrderFormitem orderFormitem = new OrderFormitem();

		orderFormitem.setName("testName");
		assertThat(orderFormitem.getName()).isEqualTo("testName");

		orderFormitem.setPrice(25f);
		assertThat(orderFormitem.getPrice()).isEqualTo(25f);

		orderFormitem.setPersonCount(3);
		assertThat(orderFormitem.getPersonCount()).isEqualTo(3);

		orderFormitem.setAmount(12);
		assertThat(orderFormitem.getAmount()).isEqualTo(12);
	}

	@Test
	public void OrderFormTest() {
		OrderForm orderForm = new OrderForm();

		orderForm.setService("eventcatering");
		assertThat(orderForm.getService()).isEqualTo("eventcatering");

		orderForm.setPersons(34);
		assertThat(orderForm.getPersons()).isEqualTo(34);

		List<OrderFormitem> food = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			food.add(new OrderFormitem());
		}
		orderForm.setFoodList(food);
		assertThat(orderForm.getFoodList()).hasSize(10);

		List<OrderFormitem> equip = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			equip.add(new OrderFormitem());
		}
		orderForm.setEquipList(equip);
		assertThat(orderForm.getEquipList()).hasSize(5);
	}

	@Test
	public void CateringOrderTest() {
		CateringOrder cateringOrder = new CateringOrder();

		for (int i = 0; i < 23; i++) {
			cateringOrder.addToAllocStaff(new User());
		}
		assertThat(cateringOrder.getAllocStaff()).hasSize(23);

		cateringOrder.setService("partyservice");
		assertThat(cateringOrder.getService()).isEqualTo("partyservice");

		cateringOrder.setAddress("testAddress");
		assertThat(cateringOrder.getAddress()).isEqualTo("testAddress");

		cateringOrder.setCompletionDate(LocalDate.of(2021, Month.DECEMBER, 29));
		assertThat(cateringOrder.getCompletionDate()).isEqualTo(LocalDate.of(2021, Month.DECEMBER, 29));
	}

	@Test
	public void getYearNumberFromDateTest(){
		TimeZone.setDefault(Calendar.getInstance().getTimeZone());
		Date date = new Date(121, Calendar.DECEMBER, 21, TimeZone.SHORT, 0);
		//assert(OrderController.getYearNumberFromDate(date) == 2021);
		assertThat(OrderController.getYearNumberFromDate(date)).isEqualTo(2021);
	}
}
