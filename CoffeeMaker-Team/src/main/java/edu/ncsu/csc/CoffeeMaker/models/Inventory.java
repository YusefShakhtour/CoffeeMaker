package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** List of ingredients to recipe **/
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Constructor for Inventory
     */
    public Inventory () {
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean match = false;
        for ( final Ingredient recipeI : r.getIngredients() ) {
            for ( final Ingredient i : getIngredients() ) {
                if ( recipeI.getIngredient().equals( i.getIngredient() ) ) {
                    match = true;
                    if ( i.getAmount() < recipeI.getAmount() ) {
                        return false;
                    }
                }
            }
            if ( !match ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( final Ingredient recipeI : r.getIngredients() ) {
                for ( final Ingredient i : getIngredients() ) {
                    if ( recipeI.getIngredient().equals( i.getIngredient() ) ) {
                        i.setAmount( i.getAmount() - recipeI.getAmount() );
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ing
     *            ingredient to store in inventory
     *
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final Ingredient ing ) {
        if ( ing.getAmount() < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        for ( final Ingredient i : getIngredients() ) {
            if ( i.getIngredient().equals( ing.getIngredient() ) ) {
                i.setAmount( i.getAmount() + ing.getAmount() );
                return true;
            }
        }

        ingredients.add( ing );
        return true;
    }

    /**
     * Gets the current list of ingredients
     *
     * @return ingredients the list to return
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient i : getIngredients() ) {
            buf.append( i.getIngredient().toString() );
            buf.append( ": " );
            buf.append( i.getAmount() );
            buf.append( "\n" );
        }

        return buf.toString();
    }

}
