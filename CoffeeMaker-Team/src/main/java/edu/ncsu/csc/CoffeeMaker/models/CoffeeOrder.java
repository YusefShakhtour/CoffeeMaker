package edu.ncsu.csc.CoffeeMaker.models;

import java.time.LocalDateTime;
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
    private Long          id;

    /** total cost of order */
    @Min ( 0 )
    private Integer       total;

    /** field indicating whether the order has been fulfilled */
    private boolean       fulfilled;

    /** Time of order construction in UTC */
    private LocalDateTime timestamp;

    /** list of recipes in the order */
    @ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Recipe>  recipes;

    /**
     * creates an order object using recipe list
     *
     * @param recipes
     *            list of recipes in order
     */
    public CoffeeOrder ( final List<Recipe> recipes ) {
        setFulfilled( false );
        setRecipes( recipes );
        setOrderTotal();
        setTimeStamp( LocalDateTime.now() );
    }

    /**
     * creates an order object using recipe list
     *
     * @param recipes
     *            list of recipes in order
     * @param fulfilled
     *            order fulfilled boolean
     * @param total
     *            total of recipes
     * @param stamp
     *            the time stamp of creation
     */
    public CoffeeOrder ( final List<Recipe> recipes, final boolean fulfilled, final Integer total,
            final LocalDateTime stamp ) {
        this.recipes = recipes;
        this.fulfilled = fulfilled;
        this.total = total;
        this.timestamp = stamp;
    }

    /**
     * edits current order to become like param order
     *
     * @param order
     *            the order to change to
     */
    public void editOrder ( final CoffeeOrder order ) {
        setFulfilled( order.getFulfilled() );
        setRecipes( order.getRecipes() );
        setOrderTotal();
    }

    /**
     * Gets order total by returning summation of recipe prices.
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
     * Get the time stamp of order creation
     *
     * @return time of creation
     */
    public LocalDateTime getTimeStamp () {
        return this.timestamp;
    }

    /**
     * Set the time stamp of order creation
     *
     * @param stamp
     *            The date of creation
     * @return time of creation
     */
    public void setTimeStamp ( final LocalDateTime stamp ) {
        this.timestamp = stamp;
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
        return Objects.hash( fulfilled, id, total, recipes );
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
        return fulfilled == other.fulfilled && Objects.equals( id, other.id ) && Objects.equals( total, other.total )
                && Objects.equals( recipes, other.recipes );
    }

    @Override
    public String toString () {

        return "Order [id=" + id + ", total=" + total + ", fulfilled=" + fulfilled + ", recipes=" + recipes.toString()
                + "]";
    }

}
