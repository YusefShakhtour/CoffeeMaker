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

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.UserType;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class UserTest {

    @Autowired
    private UserService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    void testCreateUser () {

        final User u1 = new User( "User01", "password", UserType.CUSTOMER );
        service.encodeUser( u1 );

        final User u2 = new User( "User02", "password", UserType.CUSTOMER );
        service.encodeUser( u2 );

        final List<User> users = service.findAll();

        Assertions.assertEquals( 2, users.size(), "Creating two recipes should result in two users in the database" );

        Assertions.assertEquals( u1, users.get( 0 ), "The retrieved user should match the created one" );
    }

    @Test
    void testEditUser () {

        final User u1 = new User();
        service.encodeUser( u1 );

        final User u2 = new User( "User02", "password", UserType.CUSTOMER );
        service.encodeUser( u2 );

        final User u3 = new User( "User01", "password", UserType.CUSTOMER );

        u1.editUser( u3 );

        service.encodeUser( u1 );
        final List<User> users = service.findAll();

        Assertions.assertEquals( 2, users.size() );

        Assertions.assertEquals( "User01", users.get( 0 ).getName() );

    }

    @Test
    void testToString () {

        final User u1 = new User();

        Assertions.assertEquals( "User [id=null, name=, pass=, userType=ANONYMOUS]", u1.toString() );

    }

}
