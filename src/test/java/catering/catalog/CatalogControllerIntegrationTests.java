package catering.catalog;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerIntegrationTests {
	@Autowired
	MockMvc mvc;
	@Autowired CatalogController controller;

	@Test
	void showsHeadline() throws Exception {
		mvc.perform(get("/eventcatering")) //
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Eventcatering")));
	}

	@Test
	void sampleMvcIntegrationTest() throws Exception {

		mvc.perform(get("/mobilebreakfast")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

}
