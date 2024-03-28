package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private Long    id;

    /** name for the order */
    private String  name;

    /** Recipe price */
    @Min ( 0 )
    private Integer payment;

    /** field indicating whether the order has been fulfilled */
    private boolean fulfilled;

    /** recipe used in the order */
    private Recipe  recipe;
    // /** List of ingredients to recipe **/
    // @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // private List<Recipe> recipe;

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
    public Order ( final String name, @Min ( 0 ) final Integer payment, final boolean fulfilled2,
            final Recipe recipe ) {
        setName( name );
        setPayment( payment );
        setFulfilled( fulfilled2 );
        setRecipe( recipe );
    }

    /**
     * set the status of order
     *
     * @param fulfilled2
     *            status
     */
    private void setFulfilled ( final boolean fulfilled2 ) {
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
    private void setName ( final String name ) {
        this.name = name;
    }

    /**
     * get price for the order
     *
     * @return price
     */
    public Integer getPayment () {
        return payment;
    }

    /**
     * set price for the order
     *
     * @param price
     *            price
     */
    private void setPayment ( final Integer payment ) {
        this.payment = payment;
    }

    /**
     * get the recipe used for the order
     *
     * @return recipe
     */
    public Recipe getRecipe () {
        return recipe;
    }

    /**
     * set the recipe used for the order
     *
     * @param recipe2
     *            recipe
     */
    private void setRecipe ( final Recipe recipe2 ) {
        this.recipe = recipe2;

    }

    @Override
    public int hashCode () {
        return Objects.hash( fulfilled, id, name, payment, recipe );
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
        final Order other = (Order) obj;
        return fulfilled == other.fulfilled && Objects.equals( id, other.id ) && Objects.equals( name, other.name )
                && Objects.equals( payment, other.payment ) && Objects.equals( recipe, other.recipe );
    }

    @Override
    public String toString () {
        return "Order [id=" + id + ", name=" + name + ", payment=" + payment + ", fulfilled=" + fulfilled + ", recipe="
                + recipe + "]";
    }

}
