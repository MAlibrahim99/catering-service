package catering.order;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityIntegrationTests {

    @Autowired MockMvc mvc;

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
    
}
