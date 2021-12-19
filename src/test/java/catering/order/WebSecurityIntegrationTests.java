package catering.order;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import catering.catalog.OptionCatalog;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityIntegrationTests {

    @Autowired MockMvc mvc;

    private Cart cart;

    @Autowired
    private OptionCatalog catalog;

    @Test
    void redirectsToLoginPageOrderlist() throws Exception{
        mvc.perform(get("/order-list"))  //
                .andExpect(status().isFound()) //
                .andExpect(header().string("Location", endsWith("/login")));
    }


    @Test
    void redirectsToLoginPageOrderform() throws Exception{
        mvc.perform(get("/order/eventcatering"))  //
                .andExpect(status().isFound()) //
                .andExpect(header().string("Location", endsWith("/login")));
    }

/*
    @Test
    @WithMockUser(username = "Hannes" , roles = "ADMIN")
    void returnsModelAndView() throws Exception {
        mvc.perform(get("/order-list")) //
                .andExpect(status().isOk()) //
                .andExpect(view().name("orders")) //
                .andExpect(model().attributeExists("ordersCompleted"));
    }

*/


    //TODO Test in inventoryTests verschieben
    @Test
    @WithMockUser(username = "Hannes" , roles = "ADMIN")
    void returnsInventoryView() throws Exception {
        mvc.perform(get("/inventory")) //
                .andExpect(status().isOk()) //
                .andExpect(view().name("inventory")); //
                
    }

    /*@Test
    @WithMockUser(username = "Hannes", roles = "ADMIN")
    void returnsCorrectOrderreview() throws Exception{

        Cart cart = new Cart();
        cart.addOrUpdateItem(catalog.findByName("Buffet").stream().findFirst().get(), Quantity.of(2));

        mvc.perform(get("/cartadd")) //
                    .andExpect(status().isOk()) //
                    .andExpect(view().name("cartItem"));
        

    }*/
    
}
