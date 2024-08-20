/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Holds REST endpoints to handle CRUD operations for Order objects
 *
 * @author Zeeshawn Hasnain
 * @author Kyle Bradford
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * OrderService object
     */
    @Autowired
    private OrderService  service;

    /**
     * UserService to get current user
     */
    @Autowired
    private UserService   userService;

    /**
     * RecipeService to check that recipe exists and correct amount paid
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Provides GET access to all orders in the system
     *
     * @return JSON representation
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( BASE_PATH + "/orders" )
    public List<Order> getOrders () {
        return service.findAll();
    }

    /**
     * GET access to all orders by the current user
     *
     * @return the list of orders
     */
    @PreAuthorize ( "hasRole('ROLE_USER')" )
    @GetMapping ( BASE_PATH + "/userorders" )
    public List<Order> getUserOrders () {
        final String currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getName();
        final UserDetails user = userService.loadUserByUsername( currentUserDetails );
        final List<Order> orders = service.findByCustomerName( user.getUsername() );
        return orders;
    }

    /**
     * GET access to active order for the current user
     *
     * @return response with active order
     */
    @PreAuthorize ( "hasRole('ROLE_USER')" )
    @GetMapping ( BASE_PATH + "/activeorder" )
    public ResponseEntity getActiveOrder () {
        final String currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getName();
        final UserDetails user = userService.loadUserByUsername( currentUserDetails );
        final List<Order> orders = service.findByCustomerNameAndStatus( user.getUsername(), Order.Status.IN_PROGRESS );
        return orders.size() == 0 ? new ResponseEntity(
                errorResponse( "Order from " + user.getUsername() + " not found." ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( orders.get( 0 ), HttpStatus.OK );
    }

    /**
     * POST access to submit order from a user account
     *
     * @param order
     *            the order to be submitted
     * @return a response
     */
    @PreAuthorize ( "hasRole('ROLE_USER')" )
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final Order order ) {
        if ( null == order.getRecipeName() ) {
            return new ResponseEntity( errorResponse( "You must select a recipe to order." ), HttpStatus.BAD_REQUEST );
        }

        final String currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getName();
        final UserDetails user = userService.loadUserByUsername( currentUserDetails );

        final List<Order> existingOrders = service.findByCustomerNameAndStatus( user.getUsername(),
                Order.Status.IN_PROGRESS );

        if ( existingOrders.size() > 0 ) {
            return new ResponseEntity( errorResponse( "This customer already has a pending order." ),
                    HttpStatus.BAD_REQUEST );
        }

        final Recipe r = recipeService.findByName( order.getRecipeName() );
        if ( null == r ) {
            return new ResponseEntity( errorResponse( "This recipe does not exist." ), HttpStatus.NOT_FOUND );
        }
        if ( order.getAmtPaid() < r.getPrice() ) {
            return new ResponseEntity( errorResponse( "Insufficient funds." ), HttpStatus.BAD_REQUEST );
        }

        order.setCustomer( user.getUsername() );
        service.save( order );
        return new ResponseEntity( order, HttpStatus.OK );
    }

    /**
     * PUT request handler to complete order from staff account
     *
     * @param orderid
     *            the unique id of the order to complete
     * @return a response
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @PutMapping ( BASE_PATH + "/complete/{orderid}" )
    public ResponseEntity completeOrder ( @PathVariable ( "orderid" ) final Long orderid ) {
        final Order order = service.findById( orderid );
        if ( null != order ) {
            order.setStatus( Order.Status.COMPLETE );
            service.save( order );
            return new ResponseEntity( order, HttpStatus.OK );
        }
        return new ResponseEntity( errorResponse( "Order not found" ), HttpStatus.NOT_FOUND );
    }
}
