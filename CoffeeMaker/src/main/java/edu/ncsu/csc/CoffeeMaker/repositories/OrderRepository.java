/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Order;

/**
 * OrderRepository is used for CRUD operations Spring will generate appropriate
 * JPA code
 *
 * @author Zeeshawn Hasnain
 * @author Kyle Bradford
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders using the passed customer name
     *
     * @param customerName
     *            the name of the customer
     * @return the list of orders corresponding to the passed customer name
     */
    List<Order> findByCustomerName ( String customerName );

    /**
     * Finds all orders from the passed customer of the given order status
     *
     * @param customerName
     *            the name of the customer
     * @param status
     *            of the customer
     * @return list of customers matching name and status
     */
    List<Order> findByCustomerNameAndStatus ( String customerName, Order.Status status );
}
