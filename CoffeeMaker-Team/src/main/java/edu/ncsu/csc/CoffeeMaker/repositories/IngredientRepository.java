package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * InventoryRepository is used to provide CRUD operations for the Ingredient
 * model.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
