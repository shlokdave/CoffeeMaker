package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /** The ingredients in the recipe */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Adds the given ingredient to the recipe
     *
     * @param ingredient
     *            the ingredient to add
     */
    public void addIngredient ( Ingredient ingredient ) {
        // if ( ingredient.amount >= 0 ) {
        ingredients.add( ingredient );
        // }
    }

    /**
     * Removes the given ingredient from the recipe
     *
     * @param ingredient
     *            the ingredient to remove
     */
    public void removeIngredient ( String ingredient ) {
        this.ingredients.removeIf( i -> i.getIngredient().equals( ingredient ) );
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
     * Gets the list of ingredients
     *
     * @return the list
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        return ingredients.stream().allMatch( i -> i.amount == 0 );
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Updates the fields to be equal to the passed Recipe
     *
     * @param r
     *            with updated fields
     */
    public void updateRecipe ( final Recipe r ) {
        setName( r.getName() );
        setPrice( r.getPrice() );
        ingredients = r.ingredients;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
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
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
