package edu.ncsu.csc.CoffeeMaker.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * WebSecurityConfig sets up the authentication requirements using spring
 * security
 *
 * @author Craig Abell
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity ( prePostEnabled = true )
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The user details service to use
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configures the security settings for the application
     *
     * @param http
     *            the http security object
     * @throws Exception
     *             if there is an error
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.cors().disable().csrf().disable().authorizeRequests()
                .antMatchers( "/", "/landingpage", "/api/**/users", "/signup", "/landingpage.html", "/css/**", "/js/**",
                        "/images/**" )
                .permitAll().anyRequest().authenticated().and().formLogin().permitAll()
                .successHandler( myAuthenticationSuccessHandler() ).and().httpBasic();
    }

    /**
     * Configures the authentication manager to use the user details service
     *
     * @param auth
     *            the authentication manager builder
     * @throws Exception
     *             if there is an error
     */
    @Override
    protected void configure ( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userDetailsService ).passwordEncoder( passwordEncoder() );
    }

    /**
     * Creates a password encoder bean
     *
     * @return the password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * Handles successful authentication
     *
     * @return success handler
     */
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler () {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess ( final HttpServletRequest request, final HttpServletResponse response,
                    final Authentication authentication ) throws IOException, ServletException {
                String redirectUrl = "/userindex";
                if ( authentication.getAuthorities().stream()
                        .anyMatch( a -> a.getAuthority().equals( "ROLE_STAFF" ) ) ) {
                    redirectUrl = "/index";
                }
                response.sendRedirect( request.getContextPath() + redirectUrl );
            }
        };
    }
}
