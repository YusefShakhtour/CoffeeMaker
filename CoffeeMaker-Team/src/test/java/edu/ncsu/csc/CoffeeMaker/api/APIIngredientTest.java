package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

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
    public void ensureIngredient () throws Exception {
        service.deleteAll();

        final Ingredient i = new Ingredient( "Coffee", 10 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {

        service.deleteAll();

        final Ingredient i = new Ingredient( "Coffee", 10 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddDuplicateIngredient () throws Exception {

        /*
         * Tests a ingredient with a duplicate name to make sure it's rejected
         */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );
        final Ingredient i = new Ingredient( "Coffee", 10 );

        service.save( i );

        final Ingredient i2 = new Ingredient( "Coffee", 10 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one ingredient in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddIngredient () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i = new Ingredient( "Coffee", 10 );

        service.save( i );

        Assertions.assertEquals( 1, service.count() );

        final Ingredient i2 = new Ingredient( "Coffee", 0 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.count(), "No new ingredient should be made" );

        final Ingredient i3 = new Ingredient( "Coffee", -2 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.count(), "No new ingredient should be made" );
    }

    @Test
    @Transactional
    public void deleteIngredient () throws Exception {
        service.deleteAll();

        final Ingredient i = new Ingredient( "Coffee", 10 );
        service.save( i );
        final Ingredient i2 = new Ingredient( "Milk", 10 );
        service.save( i2 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 10 );
        service.save( i3 );

        Assertions.assertEquals( 3, service.findAll().size(),
                "There should only be three ingredients in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/ingredients/id/%d", i.getId() ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( i ) ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 2, service.findAll().size(),
                "There should only be two ingredients in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/ingredients/id/%d", i2.getId() ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( i2 ) ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.findAll().size(),
                "There should only be one ingredient in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/ingredients/id/%d", i3.getId() ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( i3 ) ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no ingredients in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/ingredients/id/%d", i3.getId() ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( i3 ) ) )
                .andExpect( status().is4xxClientError() );

    }

    @Test
    @Transactional
    public void getIngredient () throws Exception {
        service.deleteAll();

        final Ingredient i = new Ingredient( "Coffee", 10 );
        service.save( i );
        final Ingredient i2 = new Ingredient( "Milk", 10 );
        service.save( i2 );
        final Ingredient i3 = new Ingredient( "Pumpkin Spice", 10 );
        service.save( i3 );

    }

}
