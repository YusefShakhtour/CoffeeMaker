package edu.ncsu.csc.CoffeeMaker.models;

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

        final CoffeeOrder o1 = new CoffeeOrder( "CoffeeOrder 1", recipes );
        service.save( o1 );

    }

    @Test
    @Transactional
    void testEditOrder () {

    }

}
