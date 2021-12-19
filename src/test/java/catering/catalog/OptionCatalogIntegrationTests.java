package catering.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * integrations tests for direct interaction with the {@link OptionCatalog}
 */
@SpringBootTest
public class OptionCatalogIntegrationTests {

	@Autowired OptionCatalog catalog;

	@Test
	public void findCorrectOptions() {
		Iterable<Option> goods = catalog.findByType(OptionType.GOODS);
		assertThat(goods).hasSize(2);

		Iterable<Option> equip = catalog.findByType(OptionType.EQUIP);
		assertThat(equip).hasSize(3);

		Iterable<Option> food = catalog.findByType(OptionType.FOOD);
		assertThat(food).hasSize(15);
	}
}
