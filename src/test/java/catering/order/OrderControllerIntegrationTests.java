package catering.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * integration tests for the {@link OrderController}
 */
@SpringBootTest
public class OrderControllerIntegrationTests {

	@Autowired
	OrderController controller;

	/**
	 * tests direct interaction with the order form page
	 */
	@Test
	@WithMockUser(roles = "CUSTOMER")
	public void getCorrectOrderForm() {

		Model model = new ExtendedModelMap();

		String htmlReturn = controller.getOrderForm("eventcatering", model, new CateringOrder());
		assertThat(htmlReturn).isEqualTo("order_form");

		OrderForm form = (OrderForm) model.asMap().get("form");

		assertThat(form.getService()).isEqualTo("eventcatering");
		assertThat(form.getFoodList()).hasSize(4);
		assertThat(form.getEquipList()).hasSize(5);

		model = new ExtendedModelMap();

		htmlReturn = controller.getOrderForm("partyservice", model, new CateringOrder());
		assertThat(htmlReturn).isEqualTo("order_form");

		form = (OrderForm) model.asMap().get("form");

		assertThat(form.getService()).isEqualTo("partyservice");
		assertThat(form.getFoodList()).hasSize(12);
		assertThat(form.getEquipList()).hasSize(2);

		model = new ExtendedModelMap();

		htmlReturn = controller.getOrderForm("mobilebreakfast", model, new CateringOrder());
		assertThat(htmlReturn).isEqualTo("order_form");

		form = (OrderForm) model.asMap().get("form");

		assertThat(form.getService()).isEqualTo("mobilebreakfast");
		assertThat(form.getFoodList()).hasSize(2);
		assertThat(form.getEquipList()).hasSize(2);

		model = new ExtendedModelMap();

		htmlReturn = controller.getOrderForm("rentacook", model, new CateringOrder());
		assertThat(htmlReturn).isEqualTo("order_form");

		form = (OrderForm) model.asMap().get("form");

		assertThat(form.getService()).isEqualTo("rentacook");
		assertThat(form.getFoodList()).hasSize(0);
		assertThat(form.getEquipList()).hasSize(5);
	}
}
