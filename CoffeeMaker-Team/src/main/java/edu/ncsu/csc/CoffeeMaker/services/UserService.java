package edu.ncsu.csc.CoffeeMaker.services;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * The UserService is used to handle CRUD operations on the User model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single User by name.
 *
 * @author Zack Martin
 *
 */
@Component
@Transactional
public class UserService extends Service<User, Long> implements UserDetailsService {

    /** User repository */
    @Autowired
    private UserRepository        userRepository;

    /** Hashing encoder */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Return user repo
     *
     * @return user repo
     */
    @Override
    protected JpaRepository<User, Long> getRepository () {
        return userRepository;
    }

    /**
     * Find a user with the provided name
     *
     * @param name
     *            Name of the user to find
     * @return found user, null if none
     */
    public User findByName ( final String name ) {
        return userRepository.findByName( name );
    }

    /**
     * Encrypts user password and saves the user to the database
     *
     * @param user
     *            user to encode
     * @return database save
     */
    public User encodeUser ( final User user ) {
        user.setPassword( passwordEncoder.encode( user.getPassword() ) );
        return userRepository.save( user );
    }

    /**
     * Loads user based on username
     *
     * @param name
     *            username
     * @return user details
     */
    @Override
    public UserDetails loadUserByUsername ( final String name ) {
        try {
            final User user = userRepository.findByName( name );
            return new org.springframework.security.core.userdetails.User( user.getName(), user.getPassword(),
                    getAuthorities( user ) );
        }
        catch ( final Exception ex ) {
            throw new UsernameNotFoundException( "User not found with username: " + name );
        }

    }

    /**
     * Get's user permissions
     *
     * @param user
     *            user
     * @return user permissions
     */
    private Collection< ? extends GrantedAuthority> getAuthorities ( final User user ) {
        return Collections.singletonList( new SimpleGrantedAuthority( "ROLE_" + user.getUserType().name() ) );
    }

    /**
     * Authenticates user by comparing passwords
     *
     * @param user
     *            user to compare
     * @return true if authenticated, otherwise false
     */
    public boolean authenticate ( final String username, final String password ) {
        final User user = userRepository.findByName( username );
        final boolean match = passwordEncoder.matches( password, user.getPassword() );
        System.out.println( "Passwords match:" + match );
        if ( user != null && passwordEncoder.matches( password, user.getPassword() ) ) {
            return true;
        }
        return false;
    }

}
