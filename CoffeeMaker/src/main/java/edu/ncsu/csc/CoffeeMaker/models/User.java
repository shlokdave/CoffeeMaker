package edu.ncsu.csc.CoffeeMaker.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * User entity class for the CoffeeMaker application.
 * @author Craig Abell
 */
@Entity
@Table ( name = "users" )
public class User {
    /** User id */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long        id;

    /** Username of the user */
    @Column ( nullable = false, unique = true )
    private String      username;

    /** Password of the user */
    @Column ( nullable = false )
    private String      password;

    /** The set of roles for the user */
    @ElementCollection ( fetch = FetchType.EAGER )
    @CollectionTable ( name = "user_roles", joinColumns = @JoinColumn ( name = "user_id" ) )
    @Column ( name = "role" )
    private Set<String> roles = new HashSet<>();

    /**
     * Default constructor for the User entity class.
     */
    public User () {
    }

    /**
     * Creates a new user with the given username and password.
     * @param username the username of the user
     * @param password the password of the user
     */
    public User ( String username, String password ) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the id of the user.
     * @return the id of the user
     */
    public Long getId () {
        return id;
    }

    /**
     * Set the id of the user.
     * @param id the id of the user
     */
    public void setId ( Long id ) {
        this.id = id;
    }

    /**
     * Get the username of the user.
     * @return the username of the user
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set the username of the user.
     * @param username the username of the user
     */
    public void setUsername ( String username ) {
        this.username = username;
    }

    /**
     * Get the password of the user.
     * @return the password of the user
     */
    public String getPassword () {
        return password;
    }

    /**
     * Set the password of the user.
     * @param password the password of the user
     */
    public void setPassword ( String password ) {
        this.password = password;
    }

    /**
     * Get the roles of the user.
     * @return the roles of the user
     */
    public Set<String> getRoles () {
        return roles;
    }

    /**
     * Set the roles of the user.
     * @param roles the roles of the user
     */
    public void setRoles ( Set<String> roles ) {
        this.roles = roles;
    }

    /**
     * Add a role to the user.
     * @param role the role to add
     */
    public void addRole ( String role ) {
        this.roles.add( role );
    }

    /**
     * Remove a role from the user.
     * @param role the role to remove
     */
    public void removeRole ( String role ) {
        this.roles.remove( role );
    }
}
