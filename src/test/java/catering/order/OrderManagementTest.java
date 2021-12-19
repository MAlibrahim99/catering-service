package catering.order;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
}
