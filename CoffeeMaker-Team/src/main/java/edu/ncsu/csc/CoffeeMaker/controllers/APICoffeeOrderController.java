package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

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

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for CoffeeOrders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Geigh Neill
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICoffeeOrderController extends APIController {

    /**
     * CoffeeOrderService object, to be autowired in by Spring to allow for
     * manipulating the CoffeeOrder model
     */
    @Autowired
    private CoffeeOrderService orderService;

    /**
     * REST API method to provide GET access to a specific order, as indicated
     * by the path variable provided (the id of the order desired)
     *
     * @param name
     *            user name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity getOrder ( @PathVariable ( "id" ) final Long id ) {
        final CoffeeOrder order = orderService.findOrder( id );
        return null == order
                ? new ResponseEntity( errorResponse( "No order found with id " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( order, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to a specific order, as indicated
     * by the path variable provided (the id of the order desired)
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<CoffeeOrder> getOrders () {
        return orderService.findAll();
    }

    /**
     * REST API method to provide POST access to the CoffeeOrder model. This is
     * used to create a new CoffeeOrder by automatically converting the JSON
     * RequestBody provided to a CoffeeOrder object. Invalid JSON will fail.
     *
     * @param user
     *            The valid user to be saved.
     * @return ResponseEntity indicating success if the user could be saved, or
     *         an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final CoffeeOrder order ) {
        if ( null != order.getId() && null != orderService.findOrder( order.getId() ) ) {
            return new ResponseEntity( errorResponse( "Order with same id " + order.getId() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        orderService.save( order );
        return new ResponseEntity( successResponse( order.getId() + " successfully created" ), HttpStatus.OK );

    }

    /**
     * REST API method to provide PUT access to the CoffeeOrder model. This is
     * used to update a user by automatically converting the JSON RequestBody
     * provided to a CoffeeOrder object. Invalid JSON will fail.
     *
     * @param user
     *            The valid order to be saved.
     * @return ResponseEntity indicating success if the order could be saved, or
     *         an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity updateOrder ( @PathVariable final Long id, @RequestBody final CoffeeOrder order ) {

        final CoffeeOrder o = orderService.findOrder( id );

        if ( null == o ) {
            return new ResponseEntity( errorResponse( id + " not found." ), HttpStatus.NOT_FOUND );
        }

        try {
            o.editOrder( order );
            orderService.save( o );
            return new ResponseEntity( successResponse( "order #" + id + " was updated to " + o.toString() ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * REST API method to allow deleting an CoffeeOrder from CoffeeMaker by
     * making a DELETE request to the API endpoint and indicating the
     * CoffeeOrder to delete (as a path variable)
     *
     * @param id
     *            The id of the order to delete
     * @return Success if the order could be deleted; an error if the order does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity deleteOrder ( @PathVariable final Long id ) {
        final CoffeeOrder order = orderService.findOrder( id );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found with id " + id ), HttpStatus.NOT_FOUND );
        }
        orderService.delete( order );

        return new ResponseEntity( successResponse( id + " was deleted successfully" ), HttpStatus.OK );
    }

}
