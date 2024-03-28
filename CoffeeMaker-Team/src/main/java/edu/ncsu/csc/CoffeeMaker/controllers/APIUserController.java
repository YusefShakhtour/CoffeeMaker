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

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController {

    @Autowired
    private UserService userService;

    @GetMapping ( BASE_PATH + "/users/{name}" )
    public ResponseEntity getUser ( @PathVariable ( "name" ) final String name ) {
        final User user = userService.findByName( name );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        if ( null != userService.findByName( user.getName() ) ) {
            return new ResponseEntity( errorResponse( "User with the name " + user.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( userService.findAll().size() < 3 ) {
            userService.save( user );
            return new ResponseEntity( successResponse( user.getName() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "Insufficient space for user" + user.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

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

    @DeleteMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity deleteUser ( @PathVariable final String name ) {
        final User user = userService.findByName( name );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No user found for name " + name ), HttpStatus.NOT_FOUND );
        }
        userService.delete( user );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

}
