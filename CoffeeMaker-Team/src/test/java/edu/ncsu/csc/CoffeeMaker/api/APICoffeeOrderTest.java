package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APICoffeeOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CoffeeOrderService    service;

    @Autowired
    private RecipeService         rservice;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureCoffeeOrder () throws Exception {
        service.deleteAll();

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

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testCreateOrder () throws Exception {

        service.deleteAll();

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

        final CoffeeOrder o1 = new CoffeeOrder( recipes );
        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );
        final CoffeeOrder oDup = service.findAll().get( 0 );

        final String response = mvc
                .perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( oDup ) ) )
                .andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( response.contains( "Order with same id " + oDup.getId() + " already exists" ) );

    }

    @Test
    @Transactional
    public void testGetOrders () throws Exception {

        service.deleteAll();

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

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        mvc.perform( get( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andDo( print() );
        final Long orderId = service.findAll().get( 0 ).getId();
        mvc.perform( get( "/api/v1/orders/{id}", orderId ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andDo( print() );

        mvc.perform( get( "/api/v1/orders/{id}", 0 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() ).andDo( print() );

    }

    @Test
    @Transactional
    public void testUpdateOrder () throws Exception {

        service.deleteAll();

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

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );
        final Long orderId = service.findAll().get( 0 ).getId();

        mvc.perform( put( String.format( "/api/v1/orders/%d", orderId ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        mvc.perform( put( String.format( "/api/v1/orders/%d", 0 ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isNotFound() );

    }

    @Test
    @Transactional
    public void testDeleteOrder () throws Exception {

        service.deleteAll();

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

        final CoffeeOrder o1 = new CoffeeOrder( recipes );

        mvc.perform( post( "/api/v1/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );
        final Long orderId = service.findAll().get( 0 ).getId();

        mvc.perform( delete( String.format( "/api/v1/orders/%d", orderId ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        mvc.perform( delete( String.format( "/api/v1/orders/%d", 0 ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isNotFound() );

    }

}
