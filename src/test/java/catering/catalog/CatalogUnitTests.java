package catering.catalog;

import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * unit tests for {@link OptionFormitem}, {@link PriceEditForm} and {@link Option}
 */
public class CatalogUnitTests {

	@Test
	public void OptionFormitemTest() {
		OptionFormitem optionFormitem = new OptionFormitem();

		optionFormitem.setName("testName");
		assertThat(optionFormitem.getName()).isEqualTo("testName");

		optionFormitem.setPrice(78f);
		assertThat(optionFormitem.getPrice()).isEqualTo(78f);
	}

	@Test
	public void PriceEditFormTest() {
		PriceEditForm priceEditForm = new PriceEditForm();

		List<OptionFormitem> options = new ArrayList<>();
		for (int i = 0; i < 18; i++) {
			options.add(new OptionFormitem());
		}
		priceEditForm.setOptionList(options);
		assertThat(priceEditForm.getOptionList()).hasSize(18);

		priceEditForm.setService("rentacook");
		assertThat(priceEditForm.getService()).isEqualTo("rentacook");
	}

	@Test
	public void OptionTest() {
		Option option = new Option("testOption", Money.of(33, EURO),3, OptionType.GOODS, new String[] {"eventcatering", "rentacook"});

		assertThat(option.getName()).isEqualTo("testOption");
		assertThat(option.getPrice()).isEqualTo(Money.of(33, EURO));
		assertThat(option.getPersonCount()).isEqualTo(3);
		assertThat(option.getType()).isEqualTo(OptionType.GOODS);
		assertThat(option.getCategories()).contains("eventcatering", "rentacook");
	}
}
