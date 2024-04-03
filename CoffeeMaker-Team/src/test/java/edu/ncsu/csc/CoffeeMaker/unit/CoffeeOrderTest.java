package edu.ncsu.csc.CoffeeMaker.unit;

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
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.UserType;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CoffeeOrderTest {

    @Autowired
    private CoffeeOrderService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    void testCreateOrder () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final User user1 = new User( "Andrew", "pass", UserType.CUSTOMER );

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
        final List<Recipe> recipes2 = List.of( r1, r2 );

        final CoffeeOrder o1 = new CoffeeOrder();
        o1.setUser( user1 );
        o1.setRecipes( recipes );
        service.save( o1 );

        final CoffeeOrder o2 = new CoffeeOrder();
        o2.setUser( user1 );
        o2.setRecipes( recipes2 );
        service.save( o2 );

    }

    /**
     * Tests the different types of constructors
     */
    @Test
    @Transactional
    public void testConstructors () {
        final CoffeeOrder o1 = new CoffeeOrder();

        Assertions.assertEquals( o1.getUser().getName(), "" );
        Assertions.assertEquals( o1.getUser().getPassword(), "" );
        Assertions.assertEquals( o1.getUser().getUserType(), UserType.ANONYMOUS );
        Assertions.assertFalse( o1.getFulfilled() );

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
        Assertions.assertEquals( o2.getUser().getName(), "" );
        Assertions.assertEquals( o2.getUser().getPassword(), "" );
        Assertions.assertEquals( o2.getUser().getUserType(), UserType.ANONYMOUS );
        Assertions.assertFalse( o2.getFulfilled() );
        Assertions.assertEquals( o2.getRecipes().size(), 2 );
        Assertions.assertEquals( o2.getRecipes().get( 0 ), r1 );
        Assertions.assertEquals( o2.getRecipes().get( 1 ), r2 );
        Assertions.assertEquals( o2.getTotal(), 30 );

        final User user1 = new User( "Andrew", "pass", UserType.CUSTOMER );
        final CoffeeOrder o3 = new CoffeeOrder( user1, recipes );
        Assertions.assertEquals( o3.getUser().getName(), "Andrew" );
        Assertions.assertEquals( o3.getUser().getPassword(), "pass" );
        Assertions.assertEquals( o3.getUser().getUserType(), UserType.CUSTOMER );
        Assertions.assertFalse( o3.getFulfilled() );
        Assertions.assertEquals( o3.getRecipes().size(), 2 );
        Assertions.assertEquals( o3.getRecipes().get( 0 ), r1 );
        Assertions.assertEquals( o3.getRecipes().get( 1 ), r2 );
        Assertions.assertEquals( o3.getTotal(), 30 );

    }

    /**
     * Tests the to string method for recipe
     */
    @Test
    @Transactional
    public void testToString () {

    }

    /**
     * Tests the hashcode method for CoffeeOrder
     */
    @Test
    void testHashCode () {

    }

    /**
     * Tests the equals object for CoffeeOrder
     */
    @Test
    @Transactional
    void testEqualsObject () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final User user1 = new User( "Andrew", "pass", UserType.CUSTOMER );

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
        final List<Recipe> recipes2 = List.of( r1, r2 );

        final CoffeeOrder o1 = new CoffeeOrder();
        o1.setUser( user1 );
        o1.setRecipes( recipes );
        service.save( o1 );

        final CoffeeOrder o2 = new CoffeeOrder();
        o2.setUser( user1 );
        o2.setRecipes( recipes2 );
        service.save( o2 );

        final CoffeeOrder nullOrder = null;
        Assertions.assertFalse( o2.equals( nullOrder ) );
        Assertions.assertTrue( o2.equals( o2 ) );
        Assertions.assertFalse( o2.equals( o1 ) );

    }

}
