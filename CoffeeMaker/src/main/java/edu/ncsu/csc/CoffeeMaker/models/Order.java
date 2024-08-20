package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Order entity class for CoffeeMaker
 *
 * @author Zeeshawn Hasnain
 * @author Kyle Bradford
 *
 */
@Entity
@Table ( name = "orders" )
public class Order extends DomainObject {

    /**
     * Represents the status of an operation or process.
     */
    public enum Status {
        /**
         * Indicates that the operation or process is currently in progress.
         */
        IN_PROGRESS,

        /**
         * Indicates that the operation or process is complete.
         */
        COMPLETE
    }

    /** Order id */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long   id;

    /** Recipe name corresponding to order */
    private String recipeName;

    /** Order status */
    private Status status;

    /** Customer that has submitted the order */
    private String customerName;

    /** Amount paid by the customer */
    private double amtPaid;

    /** Staff member that is working on the order */
    @OneToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private User   staff;

    /**
     * Default constructor for Order
     */
    public Order () {
        setStatus( Status.IN_PROGRESS );
    }

    /**
     * Constructor for Order object
     *
     * @param recipeName
     *            name of recipe being ordered
     * @param amtPaid
     *            amount being paid
     */
    public Order ( final String recipeName, final double amtPaid ) {
        setRecipeName( recipeName );
        setCustomer( null );
        setStatus( Status.IN_PROGRESS );
        setStaff( null );
        setAmtPaid( amtPaid );
    }

    /**
     * Retrieves unique ID
     *
     * @return id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets unique id
     *
     * @param id
     *            id to be set
     */
    @SuppressWarnings ( "unused" )
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns recipeName
     *
     * @return recipeName
     */
    public String getRecipeName () {
        return recipeName;
    }

    /**
     * Assigns recipe attribute
     *
     * @param recipeName
     *            recipe to be assigned
     */
    public void setRecipeName ( final String recipeName ) {
        this.recipeName = recipeName;
    }

    /**
     * Gets order status
     *
     * @return order status
     */
    public Status getStatus () {
        return status;
    }

    /**
     * Assigns order status
     *
     * @param status
     *            status to be set
     */
    public void setStatus ( final Status status ) {
        this.status = status;
    }

    /**
     * Retrieve customer entity corresponding to order
     *
     * @return customer
     */
    public String getCustomerName () {
        return customerName;
    }

    /**
     * Set customer entity corresponding to order
     *
     * @param customerName
     *            customer entity to be set
     */
    public void setCustomer ( final String customerName ) {
        this.customerName = customerName;
    }

    /**
     * Retrieve staff member assigned to this order
     *
     * @return staff member assigned to this order
     */
    public User getStaff () {
        return staff;
    }

    /**
     * Assign staff member to this order
     *
     * @param staff
     *            staff member to be assigned
     */
    public void setStaff ( final User staff ) {
        this.staff = staff;
    }

    /**
     * Returns amount paid by customer
     *
     * @return amount paid by customer
     */
    public double getAmtPaid () {
        return amtPaid;
    }

    /**
     * Sets amount paid by customer
     *
     * @param amtPaid
     *            amount to be set
     */
    public void setAmtPaid ( final double amtPaid ) {
        this.amtPaid = amtPaid;
    }

    @Override
    public String toString () {
        return "Order [id=" + id + ", recipeName=" + recipeName + ", status=" + status + ", customerName="
                + customerName + ", staff=" + staff + ", amtPaid=" + amtPaid + "]";
    }

    @Override
    public int hashCode () {
        return Objects.hash( customerName, id, recipeName, staff, status );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Order other = (Order) obj;
        return Objects.equals( customerName, other.customerName ) && Objects.equals( id, other.id )
                && Objects.equals( recipeName, other.recipeName ) && Objects.equals( staff, other.staff )
                && status == other.status;
    }

}
