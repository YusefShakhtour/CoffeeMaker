package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe name */
    private String                 name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                price;

    /** List of ingredients to recipe **/
    @ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

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
     * Edits recipe
     *
     * @param newRecipe
     *            recipe details to edit
     * @return true if successful
     */
    public boolean editRecipe ( final Recipe newRecipe ) {
        final int newPrice = newRecipe.getPrice();
        if ( newPrice < 0 ) {
            throw new IllegalArgumentException( "Price cannot be negative" );
        }
        setPrice( newPrice );

        final List<Ingredient> newIngredients = newRecipe.getIngredients();
        for ( final Ingredient ingredient : newIngredients ) {
            if ( ingredient.getAmount() < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }
        }

        final Map<String, Ingredient> newIngredientsMap = new HashMap<>();
        for ( final Ingredient ingr : newIngredients ) {
            newIngredientsMap.put( ingr.getIngredient(), ingr );
        }

        final Iterator<Ingredient> it = ingredients.iterator();
        while ( it.hasNext() ) {
            final Ingredient oldIngr = it.next();
            final Ingredient newIngr = newIngredientsMap.get( oldIngr.getIngredient() );
            if ( newIngr == null ) {
                it.remove();
            }
            else {
                oldIngr.setAmount( newIngr.getAmount() );
                newIngredientsMap.remove( oldIngr.getIngredient() );
            }
        }

        for ( final Ingredient newIngr : newIngredientsMap.values() ) {
            addIngredient( newIngr );
        }

        // System.out.println( "Updated Ingredients: " + getIngredients() );

        return true;
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
