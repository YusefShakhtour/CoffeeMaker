package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;

/**
 * CoffeeOrderRepository is used to provide CRUD operations for the Order model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Geigh Neill
 *
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
