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
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.UserType;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UserTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup () {
        userService.deleteAll();
    }

    @Test
    @Transactional
    public void testAddUser () {

        final User u1 = new User( "Barista", "123", UserType.BARISTA );
        userService.save( u1 );
        final User u2 = new User( "Customer", "456", UserType.CUSTOMER );
        userService.save( u2 );

        final List<User> users = userService.findAll();
        Assertions.assertEquals( 2, users.size(), "Creating two users should result in two users in the database" );

        Assertions.assertEquals( u1, users.get( 0 ), "The retrieved user should match the created one" );
    }
}
