package edu.ncsu.csc.CoffeeMaker.unit;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private RecipeService    recipeservice;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 500 ) );
        ivt.addIngredient( new Ingredient( "Milk", 500 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 500 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 500 ) );

        inventoryService.save( ivt );
        recipeservice.deleteAll();

    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 480, i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 495, i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 499, i.getIngredients().get( 3 ).getAmount() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( new Ingredient( "Chocolate", 2 ) );
        ivt.addIngredient( new Ingredient( "Milk", 3 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 7 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 5 ) );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 502, ivt.getIngredients().get( 0 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for chocolate" );
        Assertions.assertEquals( 503, ivt.getIngredients().get( 1 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, ivt.getIngredients().get( 2 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 505, ivt.getIngredients().get( 3 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values coffee" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( new Ingredient( "Chocolate", 2 ) );
            ivt.addIngredient( new Ingredient( "Milk", 3 ) );
            ivt.addIngredient( new Ingredient( "Sugar", 7 ) );
            ivt.addIngredient( new Ingredient( "Coffee", -5 ) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 502, ivt.getIngredients().get( 0 ).getAmount(),
                    "Adding to the inventory should result in correctly-updated values for chocolate" );
            Assertions.assertEquals( 503, ivt.getIngredients().get( 1 ).getAmount(),
                    "Adding to the inventory should result in correctly-updated values for milk" );
            Assertions.assertEquals( 507, ivt.getIngredients().get( 2 ).getAmount(),
                    "Adding to the inventory should result in correctly-updated values sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( new Ingredient( "Chocolate", 2 ) );
            ivt.addIngredient( new Ingredient( "Milk", -3 ) );
            ivt.addIngredient( new Ingredient( "Sugar", 7 ) );
            ivt.addIngredient( new Ingredient( "Coffee", 5 ) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 502, ivt.getIngredients().get( 0 ).getAmount(),
                    "Adding to the inventory should result in correctly-updated values for chocolate" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( new Ingredient( "Chocolate", 2 ) );
            ivt.addIngredient( new Ingredient( "Milk", 3 ) );
            ivt.addIngredient( new Ingredient( "Sugar", -7 ) );
            ivt.addIngredient( new Ingredient( "Coffee", 5 ) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 502, ivt.getIngredients().get( 0 ).getAmount(),
                    "Adding to the inventory should result in correctly-updated values for chocolate" );
            Assertions.assertEquals( 503, ivt.getIngredients().get( 1 ).getAmount(),
                    "Adding to the inventory should result in correctly-updated values for milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( new Ingredient( "Chocolate", -2 ) );
            ivt.addIngredient( new Ingredient( "Milk", 3 ) );
            ivt.addIngredient( new Ingredient( "Sugar", 7 ) );
            ivt.addIngredient( new Ingredient( "Coffee", 5 ) );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get( 0 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 1 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 2 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get( 3 ).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        }
    }

    @Test
    @Transactional
    public void testToString () {
        Inventory ivt = inventoryService.getInventory();

        // This should be in the order of which the ingredients were added to
        // the inventory.
        Assertions.assertEquals( "Chocolate: 500\nMilk: 500\nSugar: 500\nCoffee: 500\n", ivt.toString() );

        ivt.addIngredient( new Ingredient( "Chocolate", 2 ) );
        ivt.addIngredient( new Ingredient( "Milk", 3 ) );
        ivt.addIngredient( new Ingredient( "Sugar", 7 ) );
        ivt.addIngredient( new Ingredient( "Coffee", 5 ) );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 502, ivt.getIngredients().get( 0 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for chocolate" );
        Assertions.assertEquals( 503, ivt.getIngredients().get( 1 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, ivt.getIngredients().get( 2 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 505, ivt.getIngredients().get( 3 ).getAmount(),
                "Adding to the inventory should result in correctly-updated values coffee" );

        Assertions.assertEquals( "Chocolate: 502\nMilk: 503\nSugar: 507\nCoffee: 505\n", ivt.toString() );

    }

    @Test
    @Transactional
    public void testSetID () {
        final Inventory ivt = inventoryService.getInventory();

        final long id = 10000;
        ivt.setId( id );

        Assertions.assertEquals( 10000, ivt.getId() );

    }

    @Test
    @Transactional
    public void testInvalidIngredients () {
        final Integer num = 500;
        final Inventory ivt = new Inventory();

        ivt.addIngredient( new Ingredient( "Chocolate", num ) );
        ivt.addIngredient( new Ingredient( "Coffee", num ) );
        ivt.addIngredient( new Ingredient( "Milk", num ) );
        ivt.addIngredient( new Ingredient( "Sugar", num ) );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 500 ) );
        r1.addIngredient( new Ingredient( "Milk", 500 ) );
        r1.addIngredient( new Ingredient( "Sugar", 500 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 500 ) );
        recipeservice.save( r1 );

        Assertions.assertTrue( ivt.enoughIngredients( r1 ) );

        r1.getIngredients().get( 3 ).setAmount( 501 );

        Assertions.assertFalse( ivt.enoughIngredients( r1 ) );

        Assertions.assertFalse( ivt.useIngredients( r1 ) );

        r1.getIngredients().get( 3 ).setAmount( 500 );

        Assertions.assertTrue( ivt.enoughIngredients( r1 ) );

        r1.getIngredients().get( 1 ).setAmount( 501 );

        Assertions.assertFalse( ivt.enoughIngredients( r1 ) );

        Assertions.assertFalse( ivt.useIngredients( r1 ) );

        r1.getIngredients().get( 1 ).setAmount( 500 );

        Assertions.assertTrue( ivt.enoughIngredients( r1 ) );

        r1.getIngredients().get( 2 ).setAmount( 501 );

        Assertions.assertFalse( ivt.enoughIngredients( r1 ) );

        Assertions.assertFalse( ivt.useIngredients( r1 ) );

        r1.getIngredients().get( 2 ).setAmount( 500 );

        Assertions.assertTrue( ivt.enoughIngredients( r1 ) );

        r1.getIngredients().get( 0 ).setAmount( 501 );

        Assertions.assertFalse( ivt.enoughIngredients( r1 ) );

        Assertions.assertFalse( ivt.useIngredients( r1 ) );

        r1.getIngredients().get( 0 ).setAmount( 500 );

        Assertions.assertTrue( ivt.enoughIngredients( r1 ) );

    }

}
