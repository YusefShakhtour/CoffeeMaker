package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

    /** User for the order */
    @ManyToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private User         user;

    /** total cost of order */
    @Min ( 0 )
    private Integer      total;

    /** field indicating whether the order has been fulfilled */
    private boolean      fulfilled;

    /** list of recipes in the order */
    @ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Recipe> recipes;

    /**
     * creates an order object
     *
     */
    public CoffeeOrder () {
        setUser( new User() );
        setFulfilled( false );
    }

    /**
     * creates an order object using recipe list
     *
     * @param recipes
     *            list of recipes in order
     */
    public CoffeeOrder ( final List<Recipe> recipes ) {
        setUser( new User() );
        setFulfilled( false );
        setRecipes( recipes );
        setOrderTotal();
    }

    /**
     * creates an order object using info such as name and beverages ordered.
     *
     * @param user
     *            the order's assigned user
     * @param recipes
     *            list of recipes in order
     */
    public CoffeeOrder ( final User user, final List<Recipe> recipes ) {
        setUser( user );
        setFulfilled( false );
        setRecipes( recipes );
        setOrderTotal();
    }

    /**
     * edits current order to become like param order
     *
     * @param order
     *            the order to change to
     */
    public void editOrder ( final CoffeeOrder order ) {
        setUser( order.getUser() );
        setFulfilled( order.getFulfilled() );
        setRecipes( order.getRecipes() );
        setOrderTotal();
    }

    // public void addRecipe ( final Recipe r ) {
    // if ( null == r ) {
    // return;
    // }
    // this.recipes.add( r );
    // this.total += r.getPrice();
    // }

    /**
     * Gets order total by returning summation of recipe prices.
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
    public User getUser () {
        return user;
    }

    /**
     * set name for the order
     *
     * @param name
     *            name
     */
    public void setUser ( final User user ) {
        this.user = user;
    }

    /**
     * get the recipe used for the order
     *
     * @return recipes in order
     */
    public List<Recipe> getRecipes () {
        return this.recipes;
    }

    /**
     * set the recipe used for the order
     *
     * @param recipes
     *            list of recipes in order
     */
    public void setRecipes ( final List<Recipe> recipes ) {
        this.recipes = recipes;

    }

    @Override
    public int hashCode () {
        return Objects.hash( fulfilled, id, user, total, recipes );
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
        return fulfilled == other.fulfilled && Objects.equals( id, other.id ) && Objects.equals( user, other.user )
                && Objects.equals( total, other.total ) && Objects.equals( recipes, other.recipes );
    }

    @Override
    public String toString () {

        return "Order [id=" + id + ", user=" + user.toString() + ", total=" + total + ", fulfilled=" + fulfilled
                + ", recipes=" + recipes.toString() + "]";
    }

}
