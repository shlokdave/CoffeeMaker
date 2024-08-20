package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.setIngredient( "Chocolate", 500 );
        ivt.setIngredient( "Coffee", 500 );
        ivt.setIngredient( "Milk", 500 );
        ivt.setIngredient( "Sugar", 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );

        recipe.setPrice( 5 );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getIngredient( "Chocolate" ) );
        Assertions.assertEquals( 480, (int) i.getIngredient( "Milk" ) );
        Assertions.assertEquals( 495, (int) i.getIngredient( "Sugar" ) );
        Assertions.assertEquals( 499, (int) i.getIngredient( "Coffee" ) );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        ingredients.add( new Ingredient( "Coffee", 5 ) );
        ingredients.add( new Ingredient( "Milk", 3 ) );
        ingredients.add( new Ingredient( "Sugar", 7 ) );
        ingredients.add( new Ingredient( "Chocolate", 2 ) );

        ivt.addIngredients( ingredients );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredient( "Coffee" ),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getIngredient( "Milk" ),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredient( "Sugar" ),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredient( "Chocolate" ),
                "Adding to the inventory should result in correctly-updated values chocolate" );

        ivt.removeIngredient( "Coffee" );

        Assertions.assertEquals( 0, (int) ivt.getIngredient( "Coffee" ),
                "Removing coffee from the database should work" );
    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

            ingredients.add( new Ingredient( "Coffee", -5 ) );
            ingredients.add( new Ingredient( "Milk", 3 ) );
            ingredients.add( new Ingredient( "Sugar", 7 ) );
            ingredients.add( new Ingredient( "Chocolate", 2 ) );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

            ingredients.add( new Ingredient( "Coffee", 5 ) );
            ingredients.add( new Ingredient( "Milk", -3 ) );
            ingredients.add( new Ingredient( "Sugar", 7 ) );
            ingredients.add( new Ingredient( "Chocolate", 2 ) );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

            ingredients.add( new Ingredient( "Coffee", 5 ) );
            ingredients.add( new Ingredient( "Milk", 3 ) );
            ingredients.add( new Ingredient( "Sugar", -7 ) );
            ingredients.add( new Ingredient( "Chocolate", 2 ) );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

            ingredients.add( new Ingredient( "Coffee", 5 ) );
            ingredients.add( new Ingredient( "Milk", 3 ) );
            ingredients.add( new Ingredient( "Sugar", 7 ) );
            ingredients.add( new Ingredient( "Chocolate", -2 ) );

            ivt.addIngredients( ingredients );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Milk" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredient( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }

    @Test
    @Transactional
    public void testCheckIngredient () {
        final Inventory ivt = inventoryService.getInventory();

        final Exception e1 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "asdf" ) );
        Assertions.assertEquals( "Units of ingredient must be a positive integer", e1.getMessage() );

        final Exception e2 = Assertions.assertThrows( IllegalArgumentException.class,
                () -> ivt.checkIngredient( "-14" ) );
        Assertions.assertEquals( "Units of ingredient must be a positive integer", e2.getMessage() );
    }

}
