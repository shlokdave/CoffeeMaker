/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * @author zeeshawnhasnain
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APIOrderControllerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderService          service;

    @Autowired
    private RecipeService         recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.CoffeeMaker.controllers.APIOrderController#getOrders()}.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "guest", roles = "USER" )
    void testPost () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.setPrice( 10 );
        r.setName( "Mocha" );
        recipeService.save( r );

        final Order order = new Order( r.getName(), 10 );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( order ) ) ).andDo( print() ).andExpect( status().isOk() );
        Assertions.assertEquals( 1, service.count() );

        final List<Order> orders = service.findAll();

        Assertions.assertEquals( 1, orders.size() );
        Assertions.assertEquals( "guest", orders.get( 0 ).getCustomerName() );

        // Test multiple active orders with another post request

        final Order order2 = new Order( r.getName(), 10 );

        final String errorResponse = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( order2 ) ) )
                .andDo( print() ).andExpect( status().is4xxClientError() ).andReturn().getResponse()
                .getContentAsString();
        Assertions.assertTrue( errorResponse.contains( "This customer already has a pending order." ) );
        Assertions.assertEquals( 1, service.count() );

        // Test with no recipe
        final Order order3 = new Order();
        final String recipeError = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( order3 ) ) )
                .andDo( print() ).andExpect( status().is4xxClientError() ).andReturn().getResponse()
                .getContentAsString();

        Assertions.assertTrue( recipeError.contains( "You must select a recipe to order." ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.CoffeeMaker.controllers.APIOrderController#getOrders()}.
     *
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    void testGetOrders () throws UnsupportedEncodingException, Exception {
        final Recipe r = new Recipe();
        r.setPrice( 10 );
        r.setName( "Mocha" );
        recipeService.save( r );
        final User i1 = new User( "guest1", "password" );
        final User i2 = new User( "guest2", "password" );

        final Order order = new Order( r.getName(), 10 );
        order.setCustomer( i1.getUsername() );
        final Order order2 = new Order( r.getName(), 10 );
        order2.setCustomer( i2.getUsername() );
        service.save( order );
        service.save( order2 );

        final String orders = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        System.out.println( orders );
        Assertions.assertTrue( orders.contains( order.getCustomerName() ) );
        Assertions.assertTrue( orders.contains( order2.getCustomerName() ) );
    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.CoffeeMaker.controllers.APIOrderController#getUserOrders()}.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "guest", roles = "USER" )
    void testGetUserOrders () throws Exception {

        final Recipe r = new Recipe();
        r.setPrice( 10 );
        r.setName( "Mocha" );
        recipeService.save( r );

        // User with mock user's username
        final User i1 = new User( "guest", "password" );
        final User i2 = new User( "guest2", "password" );

        final Order order = new Order( r.getName(), 10 );
        order.setCustomer( i1.getUsername() );
        final Order order2 = new Order( r.getName(), 10 );
        order2.setCustomer( i2.getUsername() );

        service.save( order );
        service.save( order2 );

        final String orders = mvc.perform( get( "/api/v1/userorders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Check for valid username
        Assertions.assertTrue( orders.contains( i1.getUsername() ) );

        // Make sure other username is not present in list
        Assertions.assertFalse( orders.contains( i2.getUsername() ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "guest", roles = "USER" )
    void testGetActiveOrder () throws Exception {
        final Recipe r = new Recipe();
        r.setPrice( 10 );
        r.setName( "Mocha" );
        recipeService.save( r );

        // User with mock user's username
        final User i1 = new User( "guest", "password" );
        final User i2 = new User( "guest2", "password" );

        final Order order = new Order( r.getName(), 10 );
        order.setCustomer( i1.getUsername() );
        final Order order2 = new Order( r.getName(), 10 );
        order2.setCustomer( i2.getUsername() );

        service.save( order );
        service.save( order2 );

        final String activeOrder = mvc.perform( get( "/api/v1/activeorder" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( activeOrder.contains( order.getId().toString() ) );

        // No active order
        order.setStatus( Order.Status.COMPLETE );
        service.save( order );

        final String inactiveOrder = mvc.perform( get( "/api/v1/activeorder" ) ).andDo( print() )
                .andExpect( status().is4xxClientError() ).andReturn().getResponse().getContentAsString();
        Assertions.assertFalse( inactiveOrder.contains( order.getId().toString() ) );

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.CoffeeMaker.controllers.APIOrderController#completeOrder()}.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    void testCompleteOrder () throws Exception {
        final Recipe r = new Recipe();
        r.setPrice( 10 );
        r.setName( "Mocha" );
        recipeService.save( r );

        final User i1 = new User( "guest", "password" );
        final User i2 = new User( "guest2", "password" );

        final Order order = new Order( r.getName(), 10 );
        order.setCustomer( i1.getUsername() );
        final Order order2 = new Order( r.getName(), 10 );
        order2.setCustomer( i2.getUsername() );

        service.save( order );
        service.save( order2 );

        Assertions.assertEquals( Order.Status.IN_PROGRESS, order.getStatus() );

        mvc.perform( put( "/api/v1/complete/{orderid}", order.getId() ) ).andDo( print() ).andExpect( status().isOk() );

        Assertions.assertEquals( Order.Status.COMPLETE, order.getStatus() );

        // Non-existent order
        final String error = mvc.perform( put( "/api/v1/complete/{orderid}", 0 ) ).andDo( print() )
                .andExpect( status().is4xxClientError() ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();
        Assertions.assertTrue( error.contains( "Order not found" ) );
    }

}
