package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * This is a persistence class that stores various orders to the database and
 * all the information corrosponding to that order such as the name for the
 * order, payment info, whether it is fulfilled or not, and a unique identifier
 * for the order.
 *
 * @author Kush Faldu
 */
@Entity
public class Order extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long               id;

    /** name for the order */
    private String             name;

    /** Recipe price */
    @Min ( 0 )
    private Integer            price;

    /** List of ingredients to recipe **/
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Recipe> recipes;

    /**
     * Construct a recipe
     *
     * @param name
     *            name of recipe
     * @param price
     *            price of recipe
     */
    public Recipe ( final String name, final int price ) {
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Add ingredient to list
     *
     * @param ingredient
     *            the ingredient to add
     */
    public void addIngredient ( final Ingredient ingredient ) {
        ingredients.add( ingredient );
    }

    /**
     * Update units in recipe
     *
     * @param ingredient
     *            ingredient to update
     * @param units
     *            to update to
     */
    public void editUnits ( final Ingredient ingredient, final int units ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).equals( ingredient ) ) {
                ingredients.get( i ).setAmount( units );
            }
        }
    }

    /**
     * Get the ingredients list
     *
     * @return ingredients list
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        System.out.print( name + " with ingredients [" );
        for ( int i = 0; i < ingredients.size() - 1; i++ ) {
            System.out.print( ingredients.get( i ) + ", " );
        }
        System.out.print( ingredients.get( ingredients.size() - 1 ) + "]" );
        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
