package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This API Controller class has the endpoints handling requests for the User
 * class
 *
 * @author Zeeshawn Hasnain
 * @author Craig Abell
 * @author Abin Santy
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController {

    /**
     * UserService object
     */
    @Autowired
    private UserService     service;

    /**
     * Password encoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * GET request method for all Users in repository
     *
     * @return a JSON list of all user objects
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( BASE_PATH + "/users" )
    public List<User> getUsers () {
        return service.getRepository().findAll().stream().map( u -> {
            u.setPassword( "" );
            return u;
        } ).collect( Collectors.toList() );
    }

    /**
     * GET request method for User with specified name
     *
     * @param username
     *            the username of the User
     * @return JSON object response
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( BASE_PATH + "/users/{username}" )
    public ResponseEntity get ( @PathVariable ( "username" ) final String username ) {
        final Optional<User> user = service.getRepository().findByUsername( username );
        if ( user.isPresent() ) {
            final User u = user.get();
            u.setPassword( "" );
            return new ResponseEntity( u, HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "No user found." ), HttpStatus.NOT_FOUND );
        }
    }

    /**
     * POST request for User. Used to create new user by converting JSON
     * RequestBody to new User object
     *
     * @param user
     *            RequestBody JSON object
     * @return ResponseEntity indicating either success if the object is saved
     *         successfully or an error if not
     */
    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        // Encrypt the password
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        // Strip the roles
        user.setRoles( new HashSet<String>() );

        final var existingUser = service.getRepository().findByUsername( user.getUsername() );

        if ( existingUser.isPresent() ) {
            return new ResponseEntity( errorResponse( "User already exists." ), HttpStatus.CONFLICT );
        }
        try {
            service.getRepository().save( user );
            return new ResponseEntity( successResponse( user.getUsername() + " successfully created" ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * PUT request for User. Used to update existing user by converting JSON
     * RequestBody to new User object
     *
     * @param user
     *            RequestBody JSON object
     * @return ResponseEntity indicating either success if the object is saved
     *         successfully or an error if not
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PutMapping ( BASE_PATH + "/users" )
    public ResponseEntity updateUser ( @RequestBody final User user ) {
        // Encrypt the password
        // user.setPassword( passwordEncoder.encode( user.getPassword() ) );

        final var existingUser = service.getRepository().findByUsername( user.getUsername() );

        if ( !existingUser.isPresent() ) {
            return new ResponseEntity( errorResponse( "User does not exist" ), HttpStatus.NOT_FOUND );
        }
        try {
            existingUser.get().setRoles( user.getRoles() );
            user.setPassword( existingUser.get().getPassword() );
            // existingUser.get().setPassword( user.getPassword() );
            service.getRepository().save( user );
            return new ResponseEntity( successResponse( user.getUsername() + " successfully updated" ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * DELETE request for User. Used to remove the given user from the database
     *
     * @param username
     *            username of the user to delete
     * @return ResponseEntity indicating success if the user is successfully
     *         deleted or an error if not
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @DeleteMapping ( BASE_PATH + "/users/{username}" )
    public ResponseEntity deleteUser ( @PathVariable ( "username" ) final String username ) {
        final var user = service.getRepository().findByUsername( username );

        if ( !user.isPresent() ) {
            return new ResponseEntity( errorResponse( "No user found." ), HttpStatus.NOT_FOUND );
        }
        try {
            service.getRepository().delete( user.get() );
            return new ResponseEntity( successResponse( user.get().getUsername() + " successfully deleted" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

}
