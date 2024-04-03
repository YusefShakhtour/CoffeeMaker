package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;

/**
 * This is a persistence class that stores various orders to the database and
 * all the information corresponding to that order such as the name for the
 * order, payment info, whether it is fulfilled or not, and a unique identifier
 * for the order.
 *
 * @author Kush Faldu, Geigh Neill
 */
@Entity
public class CoffeeOrder extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long         id;

    /** name for the order */
    private String       name;

    /** total cost of order */
    @Min ( 0 )
    private Integer      total;

    /** field indicating whether the order has been fulfilled */
    private boolean      fulfilled;

    /** list of recipes in the order */
    @ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Recipe> recipes;

    /**
     * creates an order object using recipe list
     *
     * @param recipe
     *            recipe
     */
    public CoffeeOrder ( final List<Recipe> recipes ) {
        setId( id );
        setName( null );
        setFulfilled( false );
        setRecipes( recipes );
        setOrderTotal();
    }

    /**
     * creates an order object using info such as name,id,payment,
     *
     * @param name
     *            name
     * @param payment
     *            payment
     * @param recipe
     *            recipe
     */
    public CoffeeOrder ( final String name, final List<Recipe> recipes ) {
        setId( id );
        setName( name );
        setFulfilled( false );
        setRecipes( recipes );
    }

    /**
     * creates an order object using info such as name,id,payment,
     *
     * @param id
     *            id
     * @param name
     *            name
     * @param payment
     *            payment
     * @param recipe
     *            recipe
     * @param fulfilled
     *            fulfilled
     */
    public CoffeeOrder ( final String name, final boolean fulfilled, final List<Recipe> recipes ) {
        setId( id );
        setName( name );
        setFulfilled( fulfilled );
        setRecipes( recipes );
    }

    public void editOrder ( final CoffeeOrder order ) {
        setName( order.getName() );
        setFulfilled( order.getFulfilled() );
        setRecipes( order.getRecipes() );
        setOrderTotal();
    }

    public void addRecipe ( final Recipe r ) {
        if ( null == r ) {
            return;
        }
        this.recipes.add( r );
        this.total += r.getPrice();
    }

    /**
     * Gets order total by returning summation of recipe prices
     *
     * @return total price of the order
     */
    public void setOrderTotal () {
        if ( this.recipes.isEmpty() ) {
            this.total = 0;
        }
        int sum = 0;
        for ( final Recipe r : recipes ) {
            sum += r.getPrice();
        }
        this.total = sum;
    }

    /**
     * Returns order total
     *
     * @return total price of the order
     */
    public Integer getTotal () {
        return this.total;
    }

    /**
     * set the status of order
     *
     * @param fulfilled2
     *            status
     */
    public void setFulfilled ( final boolean fulfilled2 ) {
        this.fulfilled = fulfilled2;

    }

    /**
     * return the status of the order. true if fulfilled; otherwise, false
     *
     * @return true if fulfilled;otherwise, false
     */
    public boolean getFulfilled () {
        return fulfilled;
    }

    /**
     * get order id
     *
     * @return id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the generated id
     *
     * @param id
     *            id
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * get name for the order
     *
     * @return name for the order
     */
    public String getName () {
        return name;
    }

    /**
     * set name for the order
     *
     * @param name
     *            name
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * get the recipe used for the order
     *
     * @return recipe
     */
    public List<Recipe> getRecipes () {
        return this.recipes;
    }

    /**
     * set the recipe used for the order
     *
     * @param recipe2
     *            recipe
     */
    private void setRecipes ( final List<Recipe> recipes ) {
        this.recipes = recipes;

    }

    @Override
    public int hashCode () {
        return Objects.hash( fulfilled, id, name, recipes );
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
        final CoffeeOrder other = (CoffeeOrder) obj;
        return fulfilled == other.fulfilled && Objects.equals( id, other.id ) && Objects.equals( name, other.name )
                && Objects.equals( recipes, other.recipes );
    }

    @Override
    public String toString () {
        return "CoffeeOrder [id=" + id + ", name=" + name + ", fulfilled=" + fulfilled + ", recipe=" + recipes + "]";
    }

}
