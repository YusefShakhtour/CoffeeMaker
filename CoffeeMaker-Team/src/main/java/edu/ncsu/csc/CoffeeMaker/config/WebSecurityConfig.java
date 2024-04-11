package edu.ncsu.csc.CoffeeMaker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Web Securify Configuration class
 *
 * @author Zack Martin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** User service */
    @Autowired
    private UserService userService;

    /**
     * Returns hashing encoder
     *
     * @return hashing encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * Web security configuration
     *
     * @param http
     *            http
     * @throws Exception
     *             exception
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers( "/api/v1/login", "/api/v1/users" ).permitAll()
                .anyRequest().authenticated().and().formLogin().loginPage( "/login.html" ).permitAll()
                .defaultSuccessUrl( "/index" ).and().logout().permitAll();
    }

    /**
     * Global configuration for hashing encoder
     *
     * @param auth
     *            authentication builder
     * @throws Exception
     *             exception
     */
    @Autowired
    public void configureGlobal ( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userService ).passwordEncoder( passwordEncoder() );
    }

}
