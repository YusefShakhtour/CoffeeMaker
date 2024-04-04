package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
        if ( null != userService.findByName( user.getName() ) ) {
            return new ResponseEntity( errorResponse( "User with the name " + user.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        userService.save( user );
        return new ResponseEntity( successResponse( user.getName() + " successfully created" ), HttpStatus.OK );

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
            userService.save( u );
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
