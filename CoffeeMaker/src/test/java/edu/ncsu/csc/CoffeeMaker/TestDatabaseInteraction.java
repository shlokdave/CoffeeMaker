package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {
        final Recipe r = new Recipe();

        r.setName( "Mocha" );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );

        dbRecipe.setPrice( 15 );
        recipeService.save( dbRecipe );

        final var list = recipeService.findAll();
        assertEquals( 1, list.size() );

        final var dbRecipe2 = list.get( 0 );

        assertEquals( dbRecipe2.getName(), dbRecipe.getName() );
    }

    /**
     * Tests adding and updating the price of a recipe
     */
    @Test
    @Transactional
    public void testRecipePriceUpdate () {
        // Create and save a recipe
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setPrice( 5 );
        recipeService.save( r );

        // Check the price of the recipe
        final Recipe dbRecipe = recipeService.findByName( r.getName() );
        assertEquals( 5, dbRecipe.getPrice() );

        // Update recipe price and save
        r.setPrice( 10 );
        recipeService.save( r );

        // Find updated recipe and check for updated price
        final Recipe dbRecipe2 = recipeService.findByName( r.getName() );
        assertEquals( 10, dbRecipe2.getPrice() );

        // Make sure there's only 1 recipe in database
        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );
    }

    /**
     * Tests interactions with empty database
     */
    @Test
    @Transactional
    public void testEmptyRecipeList () {
        // Make sure database has no recipes
        final List<Recipe> dbRecipes = recipeService.findAll();
        assertTrue( dbRecipes.isEmpty() );

        // Find a recipe that does not exist
        final Recipe nullRecipe = recipeService.findByName( "Mocha" );
        assertNull( nullRecipe );

        // Create and save new recipe
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        recipeService.save( r );

        // Make sure database has 1 recipe
        final List<Recipe> dbRecipes2 = recipeService.findAll();
        assertEquals( 1, dbRecipes2.size() );

        assertEquals( r, dbRecipes2.get( 0 ) );
    }

    /**
     * Tests adding a list of recipes to the database
     */
    @Test
    @Transactional
    public void testSaveRecipeList () {
        // Creating three new recipes
        final Recipe r1 = new Recipe();

        final Recipe r2 = new Recipe();
        r2.setPrice( 5 );

        final Recipe r3 = new Recipe();

        // Creating a list of 3 recipes
        final List<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add( r1 );
        recipes.add( r2 );
        recipes.add( r3 );

        // Saving all recipes to database
        recipeService.saveAll( recipes );

        // Get the list of recipes and make sure that all are saved correctly
        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 3, dbRecipes.size() );

        assertEquals( r1, dbRecipes.get( 0 ) );
        assertEquals( r2, dbRecipes.get( 1 ) );
        assertEquals( r3, dbRecipes.get( 2 ) );

    }
}
