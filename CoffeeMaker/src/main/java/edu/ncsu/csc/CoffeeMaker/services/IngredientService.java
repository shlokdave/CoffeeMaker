package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * The IngredientService is used to handle CRUD operations on the Ingredient
 * model. In addition to all functionality in `Service`, we also manage the
 * Ingredient singleton.
 *
 * @author Craig Abell
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    /** The ingredient repository to use */
    @Autowired
    private IngredientRepository instance;

    /**
     * Retrieves the Ingredient instance
     * @return the instance
     */
    @Override
    public JpaRepository<Ingredient, Long> getRepository () {
        return instance;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param ingredient
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByIngredient ( final String ingredient ) {
        return instance.findByIngredient( ingredient );
    }

}
