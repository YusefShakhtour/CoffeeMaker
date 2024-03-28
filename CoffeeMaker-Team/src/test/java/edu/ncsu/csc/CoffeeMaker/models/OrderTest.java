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

        final Order o1 = new Order();

        o1.setName( "Order 1" );
        o1.setPrice( 10 );
        service.save( o1 );

        final Order o2 = new Order();

        o2.setName( "Order 1" );
        o2.setPrice( 10 );
        service.save( o2 );

        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(), "Creating two orders should result in two orders in the database" );

    }

    @Test
    @Transactional
    void testCreateOrder () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );

        final Order o1 = new Order();

        o1.setName( "Order 1" );
        o1.setPrice( 10 );
        service.save( o1 );

        final Order o2 = new Order();

        o2.setName( "Order 1" );
        o2.setPrice( 10 );
        service.save( o2 );

        final List<Order> orders = service.findAll();
        Assertions.assertEquals( 2, orders.size(), "Creating two orders should result in two orders in the database" );

    }

}
