package edu.virginia.psyc.pi.configuration;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 10:50 PM
 * This is the general configuration regarding access to the pages within
 * the web application.
 */
@Configuration
@EnableWebMvcSecurity  // The enables the CRF Token in LimeLeaf forms.
@EnableWebSecurity
@Order(0) // so that it is used before the default one in Spring Boot
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);


    @Autowired
    private ParticipantRepository participantRepository;

    /**
     * Checks database for user details
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                List<ParticipantDAO> participants = participantRepository.findByEmail(username);
                if(participants.size() > 0) {
                    LOG.info("Participant Found:" + participants.get(0));
                    return participants.get(0);
                } else return null;
            }
        }).passwordEncoder(new StandardPasswordEncoder());

        /**
        auth
                .inMemoryAuthentication()
                    .withUser("user")  // #1
                    .password("password")
                    .roles("USER")
                .and()
                    .withUser("admin") // #2
                    .password("password")
                    .roles("ADMIN", "USER");
           **/
    }

    @Override
    /**
     * Restrict access to admin endpoints to users with admin roles,
     * and restrict access to user detail endpoints to participants.
     */
    protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(
                            "/css/**",
                            "/js/**",
                            "/grepfrut/**",
                            "/img/**",
                            "/newParticipant/**"
                    ).permitAll()
                    .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").hasRole("USER")
                    .and()
                .formLogin()
                    .loginPage("/login")
                .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }
   /**
    http
                .authorizeRequests()
                .antMatchers("/**").permitAll()

                .antMatchers("/user/**").hasRole("USER");
    }
**/
    /**
     * Poke a hole completely through to allow posting to the data endpoint
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