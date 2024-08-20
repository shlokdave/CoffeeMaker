package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This API Controller class has the endpoints handling requests for the
 * Ingredient class
 *
 * @author Zeeshawn Hasnain
 * @author Craig Abell
 * @author Abin Santy
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object
     */
    @Autowired
    private IngredientService service;

    /**
     * GET request method for all Ingredients in repository
     *
     * @return a JSON list of all ingredient objescts
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * GET request method for Ingredient with specified name
     *
     * @param id
     *            the id of the Ingredient
     * @return JSON object response
     */
    @GetMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity get ( @PathVariable ( "id" ) final Long id ) {
        final Ingredient ingredient = service.findById( id );
        return null == ingredient ? new ResponseEntity( errorResponse( "No ingredient found." ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( ingredient, HttpStatus.OK );
    }

    /**
     * POST request for Ingredient. Used to create new recipe by converting JSON
     * RequestBody to new Ingredient object
     *
     * @param ingredient
     *            RequestBody JSON object
     * @return ResponseEntity indicating either success if the object is saved
     *         successfully or an error if not
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        if ( null != service.findByIngredient( ingredient.getIngredient() ) ) {
            return new ResponseEntity( errorResponse( "Ingredient already exists." ), HttpStatus.CONFLICT );
        }
        try {
            service.save( ingredient );
            return new ResponseEntity( successResponse( ingredient.getIngredient() + " successfully created" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * DELETE request for Ingredient. Used to remove the given ingredient from
     * the database
     *
     * @param id
     *            id of the ingredient to delete
     * @return ResponseEntity indicating success if the ingredient is
     *         successfully deleted or an error if not
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @DeleteMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity deleteIngredient ( @PathVariable ( "id" ) final Long id ) {
        final Ingredient ingredient = service.findById( id );
        if ( null == ingredient ) {
            return new ResponseEntity( errorResponse( "No ingredient found." ), HttpStatus.NOT_FOUND );
        }
        try {
            service.delete( ingredient );
            return new ResponseEntity( successResponse( ingredient.getIngredient() + " successfully deleted" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

}
