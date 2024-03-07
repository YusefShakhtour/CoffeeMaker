package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService service;

    /**
     * REST API method to provide GET access to a specific ingredient, as
     * indicated by the path variable provided (the id of the ingredient
     * desired)
     *
     * @param id
     *            ingredient id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "ingredients/id/{id}" )
    public ResponseEntity getIngredient ( @PathVariable final Long id ) {

        final Ingredient ingr = service.findById( id );

        if ( null == ingr ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( ingr, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is
     * used to create a new Ingredient by automatically converting the JSON
     * RequestBody provided to a Ingredient object. Invalid JSON will fail.
     *
     * @param ingr
     *            The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Ingredient could be
     *         saved to the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingr ) {
        if ( ingr.getAmount() <= 0 ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with the id " + ingr.getId() + " must have positive amount" ),
                    HttpStatus.CONFLICT );
        }
        for ( final Ingredient i : service.findAll() ) {
            if ( i.getIngredient().toLowerCase().trim().equals( ingr.getIngredient().toLowerCase().trim() ) ) {
                return new ResponseEntity(
                        errorResponse( "Ingredient with the name " + ingr.getIngredient() + " already exists" ),
                        HttpStatus.CONFLICT );

            }
        }
        service.save( ingr );
        return new ResponseEntity( successResponse( ingr.getId() + " successfully created" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow deleting a Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the Ingredient to delete (as a path variable)
     *
     * @param id
     *            The id of the Ingredient to delete
     * @return Success if the Ingredient could be deleted; an error if the
     *         Ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/id/{id}" )
    public ResponseEntity deleteIngredient ( @PathVariable final Long id ) {
        final Ingredient ingr = service.findById( id );
        if ( null == ingr ) {
            return new ResponseEntity( errorResponse( "No ingredient found for id " + id ), HttpStatus.NOT_FOUND );
        }
        service.delete( ingr );

        return new ResponseEntity( successResponse( id + " was deleted successfully" ), HttpStatus.OK );
    }

}
