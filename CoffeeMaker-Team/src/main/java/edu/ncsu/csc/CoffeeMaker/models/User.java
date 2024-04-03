package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.enums.UserType;

/**
 * User for the coffee maker. User is tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support.
 *
 * @author Zack Martin
 */
@Entity
public class User extends DomainObject {

    /** User id */
    @Id
    @GeneratedValue
    private Long     id;

    /** Name */
    private String   name;

    /** Password */
    private String   pass;

    /** User type */
    private UserType userType;

    /**
     * User constructor
     *
     * @param name
     *            name
     * @param pass
     *            password
     * @param userType
     *            user type
     */
    public User ( final String name, final String pass, final UserType userType ) {
        setName( name );
        setPassword( pass );
        setUserType( userType );
    }

    /**
     * User constructor for an anonymous user
     */
    public User () {
        this( "", "", UserType.ANONYMOUS );
    }

    @Override
    public Long getId () {
        return id;
    }

    /**
     * Returns name
     *
     * @return name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets name to new name
     *
     * @param name
     *            new name
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns password
     *
     * @return password
     */
    public String getPassword () {
        return pass;
    }

    /**
     * Sets password to new password
     *
     * @param pass
     *            new password
     */
    public void setPassword ( final String pass ) {
        this.pass = pass;
    }

    /**
     * Returns user type
     *
     * @return user type
     */
    public UserType getUserType () {
        return userType;
    }

    /**
     * Sets users type to new user type
     *
     * @param userType
     *            new user type
     */
    public void setUserType ( final UserType userType ) {
        this.userType = userType;
    }

    /**
     * Edits user with new info based on user parameter
     *
     * @param user
     *            user parameter with new user info
     */
    public void editUser ( final User user ) {
        setName( user.getName() );
        setPassword( user.getPassword() );
        setUserType( user.getUserType() );
    }

    @Override
    public int hashCode () {
        return Objects.hash( id, name, pass, userType );
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
        final User other = (User) obj;
        return Objects.equals( id, other.id ) && Objects.equals( name, other.name )
                && Objects.equals( pass, other.pass ) && userType == other.userType;
    }

    @Override
    public String toString () {
        return "User [id=" + id + ", name=" + name + ", pass=" + pass + ", userType=" + userType + "]";
    }

}
