package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
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
import edu.ncsu.csc.CoffeeMaker.services.OrderService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class OrderTest {

    @Autowired
    private OrderService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    void testCreateOrder () {
        final List<Recipe> recipes = new ArrayList<>();
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", -3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        recipes.add( r1 );

        final Order o1 = new Order( "Order 1", recipes );
        service.save( o1 );

        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 40 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        recipes.add( r2 );

        final Order o2 = new Order( "Order 2", recipes );
        service.save( o2 );

        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(), "Creating two orders should result in two orders in the database" );
        Assertions.assertEquals( 2, orders.get( 0 ).getName(), "Order 1" );
        Assertions.assertEquals( 2, orders.get( 1 ).getName(), "Order 2" );

    }

    @Test
    @Transactional
    void test () {
        final List<Recipe> recipes = new ArrayList<>();
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", -3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        recipes.add( r1 );

        final Order o1 = new Order( "Order 1", recipes );
        service.save( o1 );

        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 40 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        recipes.add( r2 );

        final Order o2 = new Order( "Order 2", recipes );
        service.save( o2 );

        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(), "Creating two orders should result in two orders in the database" );

    }

}
