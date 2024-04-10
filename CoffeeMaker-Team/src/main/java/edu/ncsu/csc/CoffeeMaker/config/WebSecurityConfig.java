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

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        // http.csrf().disable().authorizeRequests().antMatchers(
        // "/api/v1/login", "/api/v1/users", "api/v1/current" )
        // .permitAll().anyRequest().authenticated().and().formLogin().loginPage(
        // "/login" ).permitAll().and()
        // .logout().permitAll().and().sessionManagement()
        // .sessionCreationPolicy( SessionCreationPolicy.IF_REQUIRED ) // or
        // // as
        // // needed
        // .sessionFixation().migrateSession() // or change to another
        // // strategy
        // .and().rememberMe() // Enable remember me functionality
        // .tokenValiditySeconds( 604800 ) // Set remember me token
        // // validity (e.g., 7 days)
        // .rememberMeCookieName( "remember-me-cookie" ) // Set custom
        // // remember me
        // // cookie name
        // .and().httpBasic(); // Add HTTP Basic authentication if needed

        // http.csrf().disable().authorizeRequests().antMatchers(
        // "/api/v1/login", "/api/v1/users" ).permitAll()
        // .anyRequest().authenticated().and().formLogin().loginPage( "/login"
        // ).permitAll().and().logout()
        // .permitAll();
    }

    @Autowired
    public void configureGlobal ( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userService ).passwordEncoder( passwordEncoder() );
    }

}
