package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * IngredientRepository is used to provide CRUD operations for the Ingredient
 * Spring will generate appropriate code with JPA.
 * 
 * @author Craig Abell
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Finds the Ingredient object with the provided name.
     *
     * @param ingredient
     *            Name of the ingredient
     * @return Found ingredient, null if none
     */
    Ingredient findByIngredient ( String ingredient );
}
