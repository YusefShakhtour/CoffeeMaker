package edu.ncsu.csc.CoffeeMaker.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Users.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Zack Martin
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController {

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService userService;

    @PostMapping ( BASE_PATH + "/login" )
    public ResponseEntity<String> loginUser ( @RequestBody final User user ) {
        System.out.println( "HERE" );
        System.out.println( user.toString() );

        final boolean isAuthenticated = userService.authenticate( user.getName(), user.getPassword() );
        if ( isAuthenticated ) {
            final User current = userService.findByName( user.getName() );
            final Cookie cookie = new Cookie( "userId", String.valueOf( current.getId() ) );
            cookie.setHttpOnly( true );
            cookie.setSecure( true );
            cookie.setPath( "/" );
            final String cookieString = cookie.getName() + "=" + cookie.getValue() + "; HttpOnly; Secure; Path="
                    + cookie.getPath();

            final HttpHeaders headers = new HttpHeaders();
            headers.add( HttpHeaders.SET_COOKIE, cookieString );
            return new ResponseEntity( successResponse( current.getUserType().toString() ), headers, HttpStatus.OK );
        }
        else {
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Invalid credentials" );
        }
    }

    @PostMapping ( BASE_PATH + "/logout" )
    public ResponseEntity<String> logout ( final HttpServletRequest request, final HttpServletResponse response ) {
        final Cookie[] cookies = request.getCookies();
        if ( cookies != null ) {
            for ( final Cookie cookie : cookies ) {
                if ( cookie.getName().equals( "userId" ) ) {
                    cookie.setValue( "" );
                    cookie.setPath( "/" );
                    cookie.setMaxAge( 0 );
                    response.addCookie( cookie );
                    break;
                }
            }
        }
        return new ResponseEntity( successResponse( "Logged out successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the User model. This is used to
     * create a new User by automatically converting the JSON RequestBody
     * provided to a User object. Invalid JSON will fail.
     *
     * @param user
     *            The valid user to be saved.
     * @return ResponseEntity indicating success if the user could be saved, or
     *         an error if it could not be
     */

    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        System.out.println( user.toString() );
        if ( null != userService.findByName( user.getName() ) ) {
            return new ResponseEntity( errorResponse( "User with the name " + user.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        userService.encodeUser( user );
        return new ResponseEntity( successResponse( user.getName() + " successfully created" ), HttpStatus.OK );

    }

    /**
     * REST API method to provide GET access to a specific user, as indicated by
     * the path variable provided (the name of the user desired)
     *
     * @param name
     *            user name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/users/{name}" )
    public ResponseEntity getUser ( @PathVariable ( "name" ) final String name ) {
        final User user = userService.findByName( name );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to the currently authenticated user
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/current" )
    public ResponseEntity getCurrent ( final HttpServletRequest request ) {
        System.out.println( "test" );
        // Retrieve cookies from the request
        final Cookie[] cookies = request.getCookies();
        System.out.println( cookies );
        if ( cookies != null ) {
            // Loop through the cookies to find the one with name "userId"
            for ( final Cookie cookie : cookies ) {
                if ( cookie.getName().equals( "userId" ) ) {
                    System.out.println( "Found userId cookie" );
                    // Extract the user ID from the cookie
                    final String userId = cookie.getValue();
                    // Now you have the user ID, you can use it to retrieve the
                    // user from the database
                    final User user = userService.findById( Long.parseLong( userId ) );
                    if ( user != null ) {
                        System.out.println( user.getName() );
                        // Return the user details if found
                        return new ResponseEntity(
                                successResponse( "Current user: " + user.getId() + " " + user.getUserType() ),
                                HttpStatus.OK );
                    }
                    else {
                        // Handle case where user is not found
                        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "User not found" );
                    }
                }
            }
        }
        // Handle case where "userId" cookie is not found
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "User not authenticated" );
    }

    /**
     * REST API method to provide PUT access to the User model. This is used to
     * update a user by automatically converting the JSON RequestBody provided
     * to a User object. Invalid JSON will fail.
     *
     * @param name
     *            The name of the user to delete
     * @param user
     *            The valid user to be saved.
     * @return ResponseEntity indicating success if the user could be saved, or
     *         an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/users/{name}" )
    public ResponseEntity updateUser ( @PathVariable final String name, @RequestBody final User user ) {

        final User u = userService.findByName( name );

        if ( null == u ) {
            return new ResponseEntity( errorResponse( name + " not found." ), HttpStatus.NOT_FOUND );
        }

        try {
            u.editUser( user );
            userService.encodeUser( u );
            return new ResponseEntity( successResponse( name + " user type was updated to " + u.getUserType() ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * REST API method to allow deleting a User from the CoffeeMaker's by making
     * a DELETE request to the API endpoint and indicating the user to delete
     * (as a path variable)
     *
     * @param name
     *            The name of the user to delete
     * @return Success if the user could be deleted; an error if the user does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/users/{name}" )
    public ResponseEntity deleteUser ( @PathVariable final String name ) {
        final User user = userService.findByName( name );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No user found for name " + name ), HttpStatus.NOT_FOUND );
        }
        userService.delete( user );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

}
