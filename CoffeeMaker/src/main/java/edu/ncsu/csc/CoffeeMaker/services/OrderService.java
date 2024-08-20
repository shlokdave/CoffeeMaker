/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.repositories.OrderRepository;

/**
 * OrderService handles CRUD operations with the Order model
 *
 * @author Zeeshawn Hasnain
 * @author Kyle Bradford
 *
 */
@Component
@Transactional
public class OrderService extends Service<Order, Long> {

    /**
     * OrderRepository, autowired by Spring to provide CRUD operations on Order
     * model
     */
    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected JpaRepository<Order, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Find order by customer name
     *
     * @param customerName
     *            the username of the customer to search by
     * @return a list of orders from the customer
     */
    public List<Order> findByCustomerName ( final String customerName ) {
        return orderRepository.findByCustomerName( customerName );
    }

    /**
     * Find orders by customer name and order status
     *
     * @param customerName
     *            the customer username to search by
     * @param status
     *            the status enum to search by
     * @return a list of orders from the passed customer with the passed order
     *         status
     */
    public List<Order> findByCustomerNameAndStatus ( final String customerName, final Order.Status status ) {
        return orderRepository.findByCustomerNameAndStatus( customerName, status );
    }
}
