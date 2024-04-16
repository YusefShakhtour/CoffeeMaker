package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.UserType;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APIUserControllerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService           service;

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
    public void ensureUser () throws Exception {
        service.deleteAll();

        final User u1 = new User( "User01", "password", UserType.CUSTOMER );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u1 ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testUserAPI () throws Exception {

        service.deleteAll();

        final User u1 = new User( "User01", "password", UserType.CUSTOMER );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u1 ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testGetUser () throws Exception {

        service.deleteAll();

        final User newUser = new User( "TestUser", "password", UserType.CUSTOMER );
        service.save( newUser );

        // Perform a GET request to retrieve the user
        mvc.perform( get( "/api/v1/users/{name}", "TestUser" ) ).andExpect( status().isOk() ).andReturn();
    }

    @Test
    @Transactional
    public void testDuplicateUser () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no users in the CoffeeMaker" );
        final String name = "User01";
        final User u1 = new User( "User01", "password", UserType.CUSTOMER );

        service.save( u1 );

        final User u2 = new User( name, "password", UserType.CUSTOMER );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one user in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testUpdateUser () throws Exception {

        service.deleteAll();

        final User u1 = new User( "User01", "password", UserType.CUSTOMER );

        service.save( u1 );

        Assertions.assertEquals( 1, (int) service.count() );
        Assertions.assertEquals( "User01", service.findAll().get( 0 ).getName() );
        Assertions.assertEquals( "password", service.findAll().get( 0 ).getPassword() );
        Assertions.assertEquals( UserType.CUSTOMER, service.findAll().get( 0 ).getUserType() );
        Assertions.assertEquals( u1, service.findAll().get( 0 ) );

        final User updatedUser = new User( "User02", "drowssap", UserType.BARISTA );

        mvc.perform( put( String.format( "/api/v1/users/%s", u1.getName() ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updatedUser ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );
        Assertions.assertEquals( "User01", service.findAll().get( 0 ).getName() );
        // Assertions.assertEquals( "drowssap", service.findAll().get( 0
        // ).getPassword() );
        // Assertions.assertEquals( UserType.BARISTA, service.findAll().get( 0
        // ).getUserType() );

        mvc.perform( put( String.format( "/api/v1/users/%s", "wrong name" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updatedUser ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one user in the CoffeeMaker" );

    }

    @Test
    @Transactional
    public void testDeleteUserAPI () throws Exception {

        service.deleteAll();

        final User u1 = new User( "User01", "password", UserType.CUSTOMER );
        service.save( u1 );
        final User u2 = new User( "User02", "password", UserType.CUSTOMER );
        service.save( u2 );
        final User u3 = new User( "User03", "password", UserType.CUSTOMER );
        service.save( u3 );

        Assertions.assertEquals( 3, service.findAll().size(), "There should only be three users in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/users/%s", "User01" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 2, service.findAll().size(), "There should only be two users in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/users/%s", "User02" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u2 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only be one user in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/users/%s", "User03" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u3 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.findAll().size(), "There should only be no users in the CoffeeMaker" );

        mvc.perform( delete( String.format( "/api/v1/users/%s", "User03" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u3 ) ) ).andExpect( status().is4xxClientError() );

    }

}
