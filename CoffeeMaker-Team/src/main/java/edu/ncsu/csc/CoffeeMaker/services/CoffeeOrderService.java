package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.CoffeeOrderRepository;

/**
 * The UserService is used to handle CRUD operations on the Order model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Order by its order ID.
 *
 * @author Geigh Neill
 *
 */
@Component
@Transactional
public class CoffeeOrderService extends Service<CoffeeOrder, Long> {

    /**
     * UserRepository, to be autowired in by Spring and provide CRUD operations
     * on User model.
     */
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    @Override
    protected JpaRepository<CoffeeOrder, Long> getRepository () {
        return coffeeOrderRepository;
    }

    /**
     * Find a user with the provided name
     *
     * @param name
     *            Name of the user to find
     * @return found user, null if none
     */
    public CoffeeOrder findOrder ( final Long id ) {
        return coffeeOrderRepository.findById( id ).orElse( null );
    }
}
