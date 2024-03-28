package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.enums.UserType;

public class User extends DomainObject {

    @Id
    @GeneratedValue
    private Long     id;

    private String   name;

    private String   pass;

    private UserType userType;

    public User ( final String name, final String pass, final UserType userType ) {
        setName( name );
        setPassword( pass );
        setUserType( userType );
    }

    @Override
    public Serializable getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public String getPassword () {
        return pass;
    }

    public void setPassword ( final String pass ) {
        this.pass = pass;
    }

    public UserType getUserType () {
        return userType;
    }

    public void setUserType ( final UserType userType ) {
        this.userType = userType;
    }

    public void editUser ( final User user ) {
        setName( user.getName() );
        setPassword( user.getPassword() );
        setUserType( user.getUserType() );
    }

}
