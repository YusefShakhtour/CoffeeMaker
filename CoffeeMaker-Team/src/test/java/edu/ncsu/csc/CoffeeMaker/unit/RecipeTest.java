package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.addIngredient( new Ingredient( "Coffee", -12 ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, 15 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, -5 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", -3 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", -3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", -1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe( name, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", -1 ) );
        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 15 );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 3 ) );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 15 );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 40 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );

        service.save( r1 );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 40 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r2 );
        final String name3 = "Latte";
        final Recipe r3 = new Recipe( name3, 40 );
        r3.addIngredient( new Ingredient( "Coffee", 1 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 1 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 40 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 40 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );

        service.save( r1 );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 40 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r2 );
        final String name3 = "Latte";
        final Recipe r3 = new Recipe( name3, 40 );
        r3.addIngredient( new Ingredient( "Coffee", 1 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 1 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testDeletePersistence () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 40 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );

        service.save( r1 );
        final String name2 = "Mocha";
        final Recipe r2 = new Recipe( name2, 40 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        service.save( r2 );
        final String name3 = "Latte";
        final Recipe r3 = new Recipe( name3, 40 );
        r3.addIngredient( new Ingredient( "Coffee", 1 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 1 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        // The user will be shown a list of all recipes in the system.
        final List<Recipe> recipeList = service.findAll();
        assertEquals( 3, recipeList.size() );

        // The user may select one or more recipes that they want to delete.
        final Recipe d1 = recipeList.get( 0 );
        final Recipe d2 = recipeList.get( 2 );
        assertTrue( d1.equals( r1 ) );
        assertTrue( d2.equals( r3 ) );

        // The user selects the option to delete the recipes.
        service.delete( d1 );
        service.delete( d2 );
        assertEquals( 1, service.count() );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 40 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );

        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, retrieved.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 1, retrieved.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 1, retrieved.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 0, retrieved.getIngredients().get( 3 ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    // @Test
    // @Transactional
    // public void testUpdateRecipe () {
    // final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
    // service.save( r1 );
    // final Recipe r2 = new Recipe();
    // service.save( r2 );
    //
    // // r2.updateRecipe( r1 );
    //
    // Assertions.assertEquals( 50, (int) r2.getPrice() );
    // Assertions.assertEquals( 3, r2.getIngredients().get( 0 ).getAmount() );
    // Assertions.assertEquals( 1, r2.getIngredients().get( 1 ).getAmount() );
    // Assertions.assertEquals( 1, r2.getIngredients().get( 2 ).getAmount() );
    // Assertions.assertEquals( 0, r2.getIngredients().get( 3 ).getAmount() );
    //
    // Assertions.assertEquals( 2, service.count(), "Updating a recipe shouldn't
    // duplicate it" );
    //
    // }

    /**
     * Tests the to string method for recipe
     */
    @Test
    @Transactional
    public void testToString () {
        final String name1 = "Coffee";
        final Recipe r1 = new Recipe( name1, 40 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );

        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipes in the database" );

        Assertions.assertEquals( "Coffee", r1.toString() );
    }

    /**
     * Tests the hashcode method for Recipe
     */
    @Test
    void testHashCode () {
        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final Recipe r2 = new Recipe( "Coffee", 50 );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final Recipe r3 = new Recipe( "Coffee 2", 50 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 1 ) );
        r3.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r4 = new Recipe( "Coffee 3", 100 );
        r4.addIngredient( new Ingredient( "Coffee", 3 ) );
        r4.addIngredient( new Ingredient( "Milk", 1 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r5 = new Recipe( "Coffee 4", 50 );
        r5.addIngredient( new Ingredient( "Coffee", 100 ) );
        r5.addIngredient( new Ingredient( "Milk", 1 ) );
        r5.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r6 = new Recipe( "Coffee 5", 50 );
        r6.addIngredient( new Ingredient( "Coffee", 3 ) );
        r6.addIngredient( new Ingredient( "Milk", 100 ) );
        r6.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r7 = new Recipe( "Coffee 6", 50 );
        r7.addIngredient( new Ingredient( "Coffee", 3 ) );
        r7.addIngredient( new Ingredient( "Milk", 1 ) );
        r7.addIngredient( new Ingredient( "Sugar", 100 ) );

        final Recipe r8 = new Recipe( "Coffee 7", 50 );
        r8.addIngredient( new Ingredient( "Coffee", 3 ) );
        r8.addIngredient( new Ingredient( "Milk", 1 ) );
        r8.addIngredient( new Ingredient( "Sugar", 1 ) );
        r8.addIngredient( new Ingredient( "Chocolate", 100 ) );

        // final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // final Recipe r3 = createRecipe( "Coffee 2", 50, 3, 1, 1, 0 );
        // final Recipe r4 = createRecipe( "Coffee 3", 100, 3, 1, 1, 0 );
        // final Recipe r5 = createRecipe( "Coffee 4", 50, 100, 1, 1, 0 );
        // final Recipe r6 = createRecipe( "Coffee 5", 50, 3, 100, 1, 0 );
        // final Recipe r7 = createRecipe( "Coffee 6", 50, 3, 1, 100, 0 );
        // final Recipe r8 = createRecipe( "Coffee 7", 50, 3, 1, 1, 100 );

        assertEquals( r1.hashCode(), r2.hashCode() );

        assertNotEquals( r1.hashCode(), r3.hashCode() );
        assertNotEquals( r1.hashCode(), r4.hashCode() );
        assertNotEquals( r1.hashCode(), r5.hashCode() );
        assertNotEquals( r1.hashCode(), r6.hashCode() );
        assertNotEquals( r1.hashCode(), r7.hashCode() );
        assertNotEquals( r1.hashCode(), r8.hashCode() );
    }

    /**
     * Tests the equals object for Recipe
     */
    @Test
    @Transactional
    void testEqualsObject () {

        final Recipe r1 = new Recipe( "Coffee", 50 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final Recipe r2 = new Recipe( "Coffee", 50 );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final Recipe r3 = new Recipe( "Coffee 2", 50 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 1 ) );
        r3.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r4 = new Recipe( "Coffee 3", 100 );
        r4.addIngredient( new Ingredient( "Coffee", 3 ) );
        r4.addIngredient( new Ingredient( "Milk", 1 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r5 = new Recipe( "Coffee 4", 50 );
        r5.addIngredient( new Ingredient( "Coffee", 100 ) );
        r5.addIngredient( new Ingredient( "Milk", 1 ) );
        r5.addIngredient( new Ingredient( "Sugar", 1 ) );

        final Recipe r6 = null;

        final Recipe r7 = new Recipe( null, 50 );
        r7.addIngredient( new Ingredient( "Coffee", 3 ) );
        r7.addIngredient( new Ingredient( "Milk", 1 ) );
        r7.addIngredient( new Ingredient( "Sugar", 100 ) );

        final Object r8 = new Object();

        // final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // final Recipe r3 = createRecipe( "Coffee 2", 50, 3, 1, 1, 0 );
        // final Recipe r4 = createRecipe( "Coffee 3", 100, 3, 1, 1, 0 );
        // final Recipe r5 = createRecipe( "Coffee 4", 50, 100, 1, 1, 0 );
        // final Recipe r6 = null;
        // final Recipe r7 = createRecipe( null, 50, 3, 1, 100, 0 );
        // final Object r8 = new Object();

        assertTrue( r1.equals( r2 ) );
        assertTrue( r2.equals( r1 ) );

        assertFalse( r1.equals( r3 ) );
        assertFalse( r1.equals( r4 ) );
        assertFalse( r1.equals( r5 ) );
        assertFalse( r1.equals( r6 ) );
        assertFalse( r1.equals( r7 ) );
        assertFalse( r1.equals( r8 ) );
    }

}
