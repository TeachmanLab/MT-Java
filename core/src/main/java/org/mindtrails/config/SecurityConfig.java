package org.mindtrails.config;

import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Resticts who can log into the system, connecting user authentication to the
 * Participant table of the database.  Allows anyone to view the public facing parts
 * of a study, including the public/* pages and js/** and css/** directories.
 * Access to any path under admin/** requires the user be marked as an admin in the
 * database.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private ParticipantRepository participantRepository;

    /**
     * Checks database for user details
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(username -> {
                Participant participant = participantRepository.findByEmail(username);
                if (participant != null) {
                    LOG.info("Participant Found:" + participant);
                    return participant;
                } else return null;
        }).passwordEncoder(new StandardPasswordEncoder());
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().hasRole("ADMIN")
//                    .anyRequest().permitAll()  // disables API Security if swtiched with line above
                    .and()
                    .httpBasic();
        }
    }

    @Configuration
    @Order(2)
    public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter{

        /**
         * Poke a hole completely through to allow posting to the data endpoint
         * as we don't have security settings built into the PiPlayer and I'm not
         * certain their really need to be.
         * @param web
         * @throws Exception
         */
        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/bower/**", "/css/**", "/js/**", "/images/**")
                    .antMatchers(HttpMethod.POST, "/data");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable() //HTTP with Disable CSRF
                    .authorizeRequests() //Authorize Request Configuration
                    .antMatchers("/",
                            "/error",
                            "/public/**",
                            "/account/create",
                            "/resetPass",
                            "/resetPassStep2/**",
                            "/changePassword/**").permitAll()
                    .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").hasRole("USER")
                    .anyRequest().authenticated()
                    .and() //Login Form configuration for all others
                    .formLogin()
                        .defaultSuccessUrl("/session")
                        .loginPage("/login")
                        .permitAll()
                        .and()
                    .logout()
                        .permitAll();
        }
    }
}