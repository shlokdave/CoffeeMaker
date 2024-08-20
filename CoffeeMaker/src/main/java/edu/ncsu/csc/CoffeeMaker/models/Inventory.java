package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** The ingredients in the inventory */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     */
    public Inventory ( final Integer coffee, final Integer milk, final Integer sugar, final Integer chocolate ) {
        this.ingredients = new ArrayList<Ingredient>();
        setIngredient( "Coffee", coffee );
        setIngredient( "Milk", milk );
        setIngredient( "Sugar", sugar );
        setIngredient( "Chocolate", chocolate );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the list of ingredients
     *
     * @return the list
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns the amount of an ingredient in units
     *
     * @param ingredient
     *            the ingredient to add
     * @return the amount
     */
    public Integer getIngredient ( String ingredient ) {
        return this.ingredients.stream().filter( i -> i.getIngredient().equals( ingredient ) ).findFirst()
                .map( i -> i.getAmount() ).orElse( 0 );
    }

    /**
     * Sets an ingredient to the specified amount
     *
     * @param ingredient
     *            the name of the ingredient
     * @param amount
     *            the amount to set to
     */
    public void setIngredient ( String ingredient, int amount ) {
        this.ingredients.stream().filter( i -> i.getIngredient().equals( ingredient ) ).findFirst().ifPresentOrElse(
                i -> i.setAmount( amount ), () -> ingredients.add( new Ingredient( ingredient, amount ) ) );
    }

    /**
     * Removes an ingredient from the inventory
     *
     * @param ingredient
     *            the name of the ingredient
     */
    public void removeIngredient ( String ingredient ) {
        this.ingredients.removeIf( i -> i.getIngredient().equals( ingredient ) );
    }

    /**
     * Validates an amount of ingredients to add
     *
     * @param amount
     *            amount of ingredient
     * @return checked amount of ingredient
     * @throws IllegalArgumentException
     *             if the parameter isn't a positive integer
     */
    public Integer checkIngredient ( final String amount ) throws IllegalArgumentException {
        Integer amtIngredient = 0;
        try {
            amtIngredient = Integer.parseInt( amount );
        }
        catch ( final NumberFormatException e ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }
        if ( amtIngredient < 0 ) {
            throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }

        return amtIngredient;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        return r.getIngredients().stream().allMatch( i -> getIngredient( i.getIngredient() ) >= i.getAmount() );
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( final var ingredient : r.getIngredients() ) {
                setIngredient( ingredient.getIngredient(),
                        getIngredient( ingredient.getIngredient() ) - ingredient.getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredients
     *            the list of ingredient and amounts to add
     * @return true if successful, false if not
     */
    public boolean addIngredients ( List<Ingredient> ingredients ) {
        if ( ingredients.stream().anyMatch( i -> i.amount < 0 ) ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        for ( final var i : ingredients ) {
            final int curr = getIngredient( i.getIngredient() );
            setIngredient( i.getIngredient(), curr + i.getAmount() );
        }

        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();

        for ( final var ingredient : ingredients ) {
            buf.append( ingredient.toString() );
        }

        return buf.toString();
    }

}
