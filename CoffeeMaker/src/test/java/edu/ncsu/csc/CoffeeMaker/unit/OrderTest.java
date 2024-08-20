/**
 * This file provides unit tests for order.java
 */
package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;

/**
 * Test class for Order
 *
 * @author Zeeshawn Hasnain
 * @author Kyle Bradford
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class OrderTest {

    @Mock
    private Recipe recipe;

    @Mock
    private User   staff;

    @InjectMocks
    private Order  order;

    @Test
    void constructorSetsInitialStateCorrectly () {
        final Order newOrder = new Order( "Americano", 0 );
        assertEquals( "Americano", newOrder.getRecipeName() );
        assertNull( newOrder.getCustomerName() );
        assertEquals( Order.Status.IN_PROGRESS, newOrder.getStatus() );
        assertNull( newOrder.getStaff() );
    }

    @Test
    void testGetAndSetRecipe () {
        order.setRecipeName( "Latte" );
        assertEquals( "Latte", order.getRecipeName() );
    }

    @Test
    void testGetAndSetCustomerName () {
        order.setCustomer( "Jane Doe" );
        assertEquals( "Jane Doe", order.getCustomerName() );
    }

    @Test
    void testGetAndSetStatus () {
        order.setStatus( Order.Status.COMPLETE );
        assertEquals( Order.Status.COMPLETE, order.getStatus() );
    }

    @Test
    void testGetAndSetStaff () {
        final User newUser = mock( User.class );
        order.setStaff( newUser );
        assertEquals( newUser, order.getStaff() );
    }

    @Test
    void testSetId () {
        order.setId( 14L );
        assertEquals( 14L, order.getId() );
    }

    @Test
    void testToString () {
        final String expectedOutput = "Order [id=10, recipeName=Latte, status=COMPLETE, customerName=john smith, staff="
                + staff + ", amtPaid=5.0]";
        order.setId( 10L );
        order.setRecipeName( "Latte" );
        order.setCustomer( "john smith" );
        order.setStatus( Order.Status.COMPLETE );
        order.setStaff( staff );
        order.setAmtPaid( 5.0 );
        assertEquals( expectedOutput, order.toString() );
    }

    @Test
    void testHashCode () {
        final Order order1 = new Order( "coffee", 4.0 );
        final Order order2 = new Order( "coffee", 4.0 );
        assertEquals( order1.hashCode(), order2.hashCode() );
    }

    @Test
    void testEquals () {
        final Order order1 = new Order( "coffee", 4.0 );
        final Order order2 = new Order( "coffee", 4.0 );
        assertTrue( order1.equals( order2 ) );
        order2.setRecipeName( "Cappuccino" );
        assertFalse( order1.equals( order2 ) );
    }

}
