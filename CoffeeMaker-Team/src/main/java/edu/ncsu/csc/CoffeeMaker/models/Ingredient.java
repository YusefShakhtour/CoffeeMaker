package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient to be used in recipes.
 */
@Entity
public class Ingredient extends DomainObject {

    /** Ingredient ID **/
    @Id
    @GeneratedValue
    private Long   id;

    /** Ingredient Name **/
    private String ingredient;

    /** Amount of the ingredient **/
    @Min ( 0 )
    private int    amount;

    /**
     * Constructor to construct an ingredient
     *
     * @param ingredient
     *            String name
     * @param amount
     *            amount of units stored in this particular item
     */
    public Ingredient ( final String ingredient, final int amount ) {
        super();
        this.ingredient = ingredient;
        this.amount = amount;
    }

    /**
     * Constructor to construct an ingredient
     */
    public Ingredient () {
        setId( id );
        setIngredient( ingredient );
        setAmount( amount );
    }

    @Override
    public Long getId () {
        return id;
    }

    /**
     * Get the ingredient name
     *
     * @return ingredient String
     */
    public String getIngredient () {
        return ingredient;
    }

    /**
     * Return the amount stored in the ingredient
     *
     * @return amount amount in int
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Set the amount stored in ingredient
     *
     * @param amount
     *            amount stored in ingredient
     */
    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

    /**
     * Set the ingredient name
     *
     * @param ingredient
     *            String
     */
    public void setIngredient ( final String ingredient ) {
        this.ingredient = ingredient;
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

    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
    }

}
