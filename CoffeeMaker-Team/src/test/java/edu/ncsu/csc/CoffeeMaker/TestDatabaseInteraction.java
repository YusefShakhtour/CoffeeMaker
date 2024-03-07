package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {

        final Recipe r = new Recipe();

        /* set fields here */
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Milk", 3 ) );
        r.addIngredient( new Ingredient( "Sugar", 5 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r.setName( "Recipe" );
        r.setPrice( 4 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        /* Test all of the fields! You can also us assertAll. */
        assertAll( () -> assertEquals( r.getName(), dbRecipe.getName() ),
                () -> assertEquals( r.getIngredients().get( 3 ), dbRecipe.getIngredients().get( 3 ) ),
                () -> assertEquals( r.getIngredients().get( 0 ), dbRecipe.getIngredients().get( 0 ) ),
                () -> assertEquals( r.getId(), dbRecipe.getId() ),
                () -> assertEquals( r.getIngredients().get( 1 ), dbRecipe.getIngredients().get( 1 ) ),
                () -> assertEquals( r.getPrice(), dbRecipe.getPrice() ),
                () -> assertEquals( r.getIngredients().get( 2 ), dbRecipe.getIngredients().get( 2 ) ) );

        final Recipe recipeTest = recipeService.findByName( "Recipe" );
        assertTrue( recipeTest.equals( dbRecipe ) );

        dbRecipe.setPrice( 15 );
        // TODO edit units being used
        r.editUnits( r.getIngredients().get( 2 ), 12 );

        recipeService.save( dbRecipe );

        final List<Recipe> testRecipes = recipeService.findAll();

        assertEquals( 1, testRecipes.size() );

        /* Test all of the fields! You can also us assertAll. */
        assertAll( () -> assertEquals( 12, dbRecipe.getIngredients().get( 2 ).getAmount() ),
                () -> assertEquals( 15, dbRecipe.getPrice() ) );

    }
}
