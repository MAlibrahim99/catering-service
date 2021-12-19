package catering.order;

import java.time.LocalDate;

import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import catering.catalog.OptionCatalog;
import catering.user.Position;
import catering.user.User;
import catering.user.UserManagement;
import catering.user.UserRepository;
import catering.user.forms.RegistrationForm;

@SpringBootTest
public class OrderManagementTest {
    @Autowired 
    private UserRepository users;

    @Autowired 
    private CateringOrderRepository cateringOrder;

    @Autowired 
    private OrderManagement orderManagement;

    @Autowired 
    private UserManagement userManagement;
 
    private Cart cart;

    @Autowired
    private OrderController orderController;

    @Autowired
    private OptionCatalog catalog;



    @Autowired
    @Qualifier("persistentUserAccountManagement")
    private UserAccountManagement accountManagement;
    private Role customerRole = Role.of("CUSTOMER");

    @Test
    public void orderManagementCreateOrderSuccessfully(){

        RegistrationForm form = new RegistrationForm("user1" ,"user1", "user1@host.com",
        "123", Position.NONE);

        /*
        userManagement.createUser(form, customerRole);
        User currentUser = userManagement.findByUsername("user1 user1");


        var testOrder = new CateringOrder(currentUser.getUserAccount(), Cash.CASH, 
                                LocalDate.of(2021, 11, 11), TimeSegment.FRÜH, "Straße", "eventcatering");


        assertThat(testOrder).isNotNull();
        assertThat(testOrder.getAllocStaff()).isNotNull();
        */
    }

    @Test
    @WithMockUser (username = "Hannes" , roles = "ADMIN")
    void correctPriceCalculation() throws Exception{

        Cart cart = new Cart();
        
        cart.addOrUpdateItem(catalog.findByName("Buffet").stream().findFirst().get(), Quantity.of(2));
        cart.addOrUpdateItem(catalog.findByName("Servietten").stream().findFirst().get(), Quantity.of(2));

        assertEquals(cart.getPrice(),Money.of(27.5, Currencies.EURO));

    }
}
