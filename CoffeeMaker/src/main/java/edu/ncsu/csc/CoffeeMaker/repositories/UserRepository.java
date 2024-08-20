package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ncsu.csc.CoffeeMaker.models.User;

/**
 * UserRepository is used to provide CRUD operations for the User model.
 * 
 * @author Craig Abell
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a User object with the provided username. Spring will generate code
     * @param username the username of the user
     * @return Found user, null if none.
     */
    Optional<User> findByUsername ( String username );
}
