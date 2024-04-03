package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

class OrderTest {

    private OrderService service;

    @BeforeEach
    public void setup () {
        orderService.deleteAll();
    }

    @Test
    @Transactional
    void testCreateOrder () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final Recipe r1 = new Recipe( "Coffee", 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", -3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Order o1 = new Order( "Order 1", 10, false, r1 );
        service.save( o1 );

        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 40 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final Order o2 = new Order( "Order 2", 5, false, r2 );
        service.save( o2 );

        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(), "Creating two orders should result in two orders in the database" );

    }

    @Test
    @Transactional
    void testCreateOrder () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final Order o1 = new Order( "Order 1", 10 );

        o1.setName( "Order 1" );
        o1.setPayment( 10 );
        service.save( o1 );
        Assertions.assertEquals( o1.getName(), "Order 2" );
        Assertions.assertEquals( o1.getPayment(), 10 );

        final Order o2 = new Order();

        o2.setName( "Order 1" );
        o2.setPayment( 5 );
        service.save( o2 );
        Assertions.assertEquals( o2.getName(), "Order 2" );
        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(), "Creating two orders should result in two orders in the database" );

    }

}
