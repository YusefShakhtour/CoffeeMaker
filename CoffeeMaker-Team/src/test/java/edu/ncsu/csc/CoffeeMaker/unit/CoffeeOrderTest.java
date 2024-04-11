package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CoffeeOrderTest {

    @Autowired
    private CoffeeOrderService service;

    @Autowired
    private RecipeService      rservice;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    void testCreateOrder () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        rservice.save( r1 );
        rservice.save( r2 );

        final List<Recipe> recipes = List.of( r1, r2 );
        final List<Recipe> recipes2 = List.of( r1, r2 );

        final CoffeeOrder o1 = new CoffeeOrder( recipes );
        service.save( o1 );

        final CoffeeOrder o2 = new CoffeeOrder( recipes2 );
        service.save( o2 );

    }

    /**
     * Tests the different types of constructors
     */
    @Test
    @Transactional
    public void testConstructors () {

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        final CoffeeOrder o2 = new CoffeeOrder( recipes );
        Assertions.assertFalse( o2.getFulfilled() );
        Assertions.assertEquals( o2.getRecipes().size(), 2 );
        Assertions.assertEquals( o2.getRecipes().get( 0 ), r1 );
        Assertions.assertEquals( o2.getRecipes().get( 1 ), r2 );
        Assertions.assertEquals( o2.getTotal(), 30 );

        final CoffeeOrder o3 = new CoffeeOrder( recipes, true, 30, LocalDateTime.now() );
        Assertions.assertTrue( o3.getFulfilled() );
        Assertions.assertEquals( o3.getRecipes().size(), 2 );
        Assertions.assertEquals( o3.getRecipes().get( 0 ), r1 );
        Assertions.assertEquals( o3.getRecipes().get( 1 ), r2 );
        Assertions.assertEquals( o3.getTotal(), 30 );
        Assertions.assertEquals( LocalDateTime.now().getDayOfMonth(), o3.getTimeStamp().getDayOfMonth() );

    }

    /**
     * Tests the edit method for order
     */
    @Test
    @Transactional
    public void testEditOrder () {
        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );

        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        final List<Recipe> recipes = List.of( r1, r2 );
        final List<Recipe> recipes2 = List.of( r1 );

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        final CoffeeOrder o2 = new CoffeeOrder( recipes2 );

        Assertions.assertFalse( o1.getFulfilled() );
        Assertions.assertEquals( o1.getRecipes().size(), 2 );
        Assertions.assertEquals( o1.getRecipes().get( 0 ), r1 );
        Assertions.assertEquals( o1.getRecipes().get( 1 ), r2 );
        Assertions.assertEquals( o1.getTotal(), 30 );

        o1.editOrder( o2 );

        Assertions.assertFalse( o1.getFulfilled() );
        Assertions.assertEquals( o1.getRecipes().size(), 1 );
        Assertions.assertEquals( o1.getRecipes().get( 0 ), r1 );
        Assertions.assertEquals( o1.getTotal(), 15 );
        Assertions.assertEquals( o1.getTimeStamp(), o1.getTimeStamp() );
    }

    /**
     * Tests the to string method for recipe
     */
    @Test
    @Transactional
    public void testToString () {

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        assertEquals( "Order [id=null, total=30, fulfilled=false, recipes=[Coffee, Mocha]]", o1.toString() );
    }

    /**
     * Tests the hashcode method for CoffeeOrder
     */
    @Test
    void testHashCode () {

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );

        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        final List<Recipe> recipes = List.of( r1, r2 );
        final List<Recipe> recipes2 = List.of( r2, r1 );

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        final CoffeeOrder o2 = new CoffeeOrder( recipes2 );

        assertNotEquals( o1.hashCode(), o2.hashCode() );
        assertEquals( o1.hashCode(), o1.hashCode() );
        assertEquals( o2.hashCode(), o2.hashCode() );
    }

    /**
     * Tests the equals object for CoffeeOrder
     */
    @Test
    @Transactional
    void testEqualsObject () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );

        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );

        rservice.save( r1 );
        rservice.save( r2 );

        final List<Recipe> recipes = List.of( r1, r2 );
        final List<Recipe> recipes2 = List.of( r1, r2 );

        final CoffeeOrder o1 = new CoffeeOrder( recipes );
        service.save( o1 );

        final CoffeeOrder o2 = new CoffeeOrder( recipes2 );
        service.save( o2 );

        final CoffeeOrder nullOrder = null;
        Assertions.assertFalse( o2.equals( nullOrder ) );
        Assertions.assertTrue( o2.equals( o2 ) );
        Assertions.assertFalse( o2.equals( o1 ) );

    }

}
