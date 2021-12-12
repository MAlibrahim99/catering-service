package catering.order;

import org.junit.jupiter.api.Test;
import org.salespointframework.order.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import catering.catalog.CateringCatalog;
import catering.catalog.OptionCatalog;


@SpringBootTest
public class PurchaseProcedureTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderController orderController;
    
    @Autowired
    private CateringCatalog cCatalog;
    
    @Autowired
    private OptionCatalog oCatalog;


    @Test
    public void PurchaseCalculateTest(){
        Cart cart = new Cart();
        for (Ware ware : )
    }
    
}
