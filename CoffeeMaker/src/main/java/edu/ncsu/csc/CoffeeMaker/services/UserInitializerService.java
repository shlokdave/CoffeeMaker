package edu.ncsu.csc.CoffeeMaker.services;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * UserInitializerService is used to initialize the database with an admin user
 *
 * @author Craig Abell
 */
@Service
public class UserInitializerService {

    /** The repository to use */
    private final UserRepository  userRepository;
    /** The password encoder to use */
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new UserInitializerService with the provided repository and
     *
     * @param userRepository
     *            the repository to use
     * @param passwordEncoder
     *            the password encoder to use
     */
    @Autowired
    public UserInitializerService ( UserRepository userRepository, PasswordEncoder passwordEncoder ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initializes the database with an admin user if one does not already
     * exist.
     */
    @PostConstruct
    public void initializeAdmin () {
        final String adminUsername = "admin";
        final Optional<User> existingAdmin = userRepository.findByUsername( adminUsername );
        if ( !existingAdmin.isPresent() ) {
            final User admin = new User( adminUsername, passwordEncoder.encode( "password" ) );
            admin.addRole( "ADMIN" );
            admin.addRole( "STAFF" );
            userRepository.save( admin );
        }
    }

    /**
     * Initializes the database with an staff user if one does not already
     * exist.
     */
    @PostConstruct
    public void initializeStaff () {
        final String staffUsername = "staff";
        final Optional<User> existingStaff = userRepository.findByUsername( staffUsername );
        if ( !existingStaff.isPresent() ) {
            final User staff = new User( staffUsername, passwordEncoder.encode( "password" ) );
            staff.addRole( "STAFF" );
            userRepository.save( staff );
        }
    }

    /**
     * Initializes the database with a guest user if one does not already exist.
     */
    @PostConstruct
    public void initializeGuest () {
        final String guestUesrname = "guest";
        final Optional<User> existingUser = userRepository.findByUsername( guestUesrname );
        if ( !existingUser.isPresent() ) {
            final User user = new User( guestUesrname, passwordEncoder.encode( "password" ) );
            user.addRole( "USER" );
            userRepository.save( user );
        }
    }
}
