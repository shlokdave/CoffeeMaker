package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIInventoryController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService service;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the Inventory to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity getInventory () {
        final Inventory inventory = service.getInventory();
        return new ResponseEntity( inventory, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory" )
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    public ResponseEntity updateInventory ( @RequestBody final Inventory inventory ) {
        final Inventory inventoryCurrent = service.getInventory();
        inventoryCurrent.addIngredients( inventory.getIngredients() );
        service.save( inventoryCurrent );
        return new ResponseEntity( inventoryCurrent, HttpStatus.OK );
    }

    /**
     * POST request for Ingredient. Used to create new ingredient by converting
     * JSON RequestBody to new Ingredient object
     *
     * @param ingredient
     *            RequestBody JSON object
     * @return ResponseEntity indicating either success if the object is saved
     *         successfully or an error if not
     */
    @PostMapping ( BASE_PATH + "/inventory" )
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        final var inventory = service.getInventory();
        if ( inventory.getIngredient( ingredient.getIngredient() ) > 0 ) {
            return new ResponseEntity( errorResponse( "Ingredient already exists." ), HttpStatus.CONFLICT );
        }
        try {
            final var is = new ArrayList<Ingredient>();
            is.add( ingredient );
            inventory.addIngredients( is );
            service.save( inventory );
            return new ResponseEntity( successResponse( ingredient.getIngredient() + " successfully created" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

}
