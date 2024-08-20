package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * The Ingredient class represents an ingredient in the CoffeeMaker.
 * Ingredients are tied to the database using Hibernate libraries.
 * See IngredientRepository and IngredientService for the other two pieces
 * used for database support.
 * 
 * @author Craig Abell
 */
@Entity
public class Ingredient extends DomainObject {
    /** Ingredient ID */
    @Id
    @GeneratedValue
    Long    id;
    /** Ingredient name */
    String  ingredient;
    /** Ingredient amount */
    @Min ( 0 )
    Integer amount;

    /**
     * Creates a default ingredient for the coffee maker.
     */
    public Ingredient () {
        super();
    }

    /**
     * Creates an ingredient with the given name and amount.
     * @param ingredient the name of the ingredient
     * @param amount the amount of the ingredient
     */
    public Ingredient ( final String ingredient, final Integer amount ) {
        super();
        this.ingredient = ingredient;
        this.amount = amount;
    }

    /**
     * Returns the ID of the ingredient.
     * @return the ID of the ingredient
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ID of the ingredient.
     * @param id the ID of the ingredient
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the name of the ingredient.
     * @return the name of the ingredient
     */
    public String getIngredient () {
        return ingredient;
    }

    /**
     * Sets the name of the ingredient.
     * @param ingredient the name of the ingredient
     */
    public void setIngredient ( final String ingredient ) {
        this.ingredient = ingredient;
    }

    /**
     * Returns the amount of the ingredient.
     * @return the amount of the ingredient
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient.
     * @param amount the amount of the ingredient
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    /**
     * Returns a string representation of the ingredient.
     * @return a string representation of the ingredient
     */
    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
    }

}
