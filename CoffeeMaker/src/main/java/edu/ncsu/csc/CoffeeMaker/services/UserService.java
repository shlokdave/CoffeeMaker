package edu.ncsu.csc.CoffeeMaker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * UserService is used to provide user details to Spring Security.
 *
 * @author Craig Abell
 */
@Service
public class UserService implements UserDetailsService {

    /**
     * The repository to use
     */
    private final UserRepository userRepository;

    /**
     * Creates a new UserService with the provided repository.
     *
     * @param userRepository
     *            the repository to use
     */
    @Autowired
    public UserService ( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their username.
     *
     * @param username
     *            the username of the user
     * @return the user details
     * @throws UsernameNotFoundException
     *             if the user is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found with username: " + username ) );

        return org.springframework.security.core.userdetails.User.builder().username( user.getUsername() )
                .password( user.getPassword() ).roles( user.getRoles().toArray( new String[0] ) ).build();
    }

    /**
     * Gets the repository to use. Requires the user to have the STAFF role.
     *
     * @return the repository
     */
    public UserRepository getRepository () {
        return userRepository;

    }
}
