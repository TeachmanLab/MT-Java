package edu.virginia.psyc.pi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 10:50 PM
 * This is the general configuration regarding access to the pages within
 * the web application.
 */
@Configuration
@EnableWebSecurity
@Order(0) // so that it is used before the default one in Spring Boot
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Presently just allows for two users, will add details here to look up
     * credentials in the database.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                    .withUser("user")  // #1
                    .password("password")
                    .roles("USER")
                .and()
                    .withUser("admin") // #2
                    .password("password")
                    .roles("ADMIN", "USER");
    }

    @Override
    /**
     * Restruct access to admin endpoints to users with admin roles,
     * and restrict access to user detail endpoints to participants.
     */
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER");
    }

    /**
     * Poke a whole completely through to allow posting to the data endpoint
     * as we don't have security settings built into the PiPlayer and I'm not
     * certain their really need to be.
     * @param webSecurity
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {

        webSecurity
                .ignoring()
                        // All of Spring Security will ignore the requests
                .antMatchers("/resources/**")
                .antMatchers(HttpMethod.POST, "/data");
    }



}