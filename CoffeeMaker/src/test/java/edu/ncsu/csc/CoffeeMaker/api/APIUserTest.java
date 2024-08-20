package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Test class for APIUserController
 *
 * @author Zeeshawn Hasnain
 *
 */
@SpringBootTest
@AutoConfigureMockMvc ( addFilters = false )
@ExtendWith ( SpringExtension.class )
public class APIUserTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PasswordEncoder       passwordEncoder;

    @Autowired
    private UserService           service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.getRepository().deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testPost () throws UnsupportedEncodingException, Exception {
        final User i1 = new User( "admin", "password" );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.getRepository().count() );

    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testGet () throws Exception {
        final User i1 = new User( "admina", passwordEncoder.encode( "password" ) );
        i1.addRole( "STAFF" );
        service.getRepository().save( i1 );

        Assertions.assertEquals( 1, (int) service.getRepository().count() );

        // Test get
        mvc.perform( get( "/api/v1/users/{username}", i1.getUsername() ) ).andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testGet2 () throws Exception {
        final User i1 = new User( "vanilla", passwordEncoder.encode( "password" ) );
        final User i2 = new User( "caramel", passwordEncoder.encode( "password" ) );
        final User i3 = new User( "vanilla2", passwordEncoder.encode( "password" ) );
        i1.addRole( "STAFF" );
        i2.addRole( "STAFF" );
        i3.addRole( "STAFF" );

        service.getRepository().save( i1 );
        service.getRepository().save( i2 );
        service.getRepository().save( i3 );

        Assertions.assertEquals( 3, (int) service.getRepository().count() );

        mvc.perform( get( "/api/v1/users/{username}", i3.getUsername() ) ).andDo( print() )
                .andExpect( status().isOk() );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testGetAll () throws Exception {
        final User i1 = new User( "vanilla", "GUEST" );
        final User i2 = new User( "caramel", passwordEncoder.encode( "password" ) );
        final User i3 = new User( "vanilla1", passwordEncoder.encode( "password" ) );
        i1.addRole( "STAFF" );
        i2.addRole( "STAFF" );
        i3.addRole( "STAFF" );

        service.getRepository().save( i1 );
        service.getRepository().save( i2 );
        service.getRepository().save( i3 );

        Assertions.assertEquals( 3, (int) service.getRepository().count() );

        final String users = mvc.perform( get( "/api/v1/users" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( users.contains( "vanilla" ) );
        Assertions.assertTrue( users.contains( "caramel" ) );
        Assertions.assertFalse( users.contains( "cinnamon" ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    public void testDelete () throws Exception {
        final User i1 = new User( "vanilla", "GUEST" );
        final User i2 = new User( "caramel", passwordEncoder.encode( "password" ) );
        final User i3 = new User( "vanilla1", passwordEncoder.encode( "password" ) );
        i1.addRole( "STAFF" );
        i2.addRole( "STAFF" );
        i3.addRole( "STAFF" );

        service.getRepository().save( i1 );
        service.getRepository().save( i2 );
        service.getRepository().save( i3 );

        Assertions.assertEquals( 3, (int) service.getRepository().count() );

        final String remove = mvc.perform( delete( "/api/v1/users/{username}", i2.getUsername() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 2, (int) service.getRepository().count() );
        Assertions.assertTrue( remove.contains( "caramel" ) );

        final String failure = mvc.perform( delete( "/api/v1/users/{username}", i2.getUsername() ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals( 2, (int) service.getRepository().count() );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = { "ADMIN", "STAFF" } )
    public void testPut () throws Exception {
        final User i1 = new User( "vanilla", "GUEST" );
        final User i2 = new User( "caramel", passwordEncoder.encode( "password" ) );
        final User i3 = new User( "vanilla1", passwordEncoder.encode( "password" ) );
        i1.addRole( "STAFF" );
        i2.addRole( "STAFF" );
        i3.addRole( "STAFF" );

        service.getRepository().save( i1 );
        service.getRepository().save( i2 );
        service.getRepository().save( i3 );

        Assertions.assertEquals( 3, (int) service.getRepository().count() );

        // Changing the roles of i1
        i1.addRole( "CUSTOMER" );
        i1.removeRole( "STAFF" );
        // i1.setPassword( "GUEST1" ); no longer possible for security reasons

        // Update i1
        final String update = mvc
                .perform( put( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( i1 ) ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Get updated i1
        final String getUpdated = mvc.perform( get( "/api/v1/users/{username}", i1.getUsername() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Assert that changes are present
        Assertions.assertEquals( 3, (int) service.getRepository().count() );
        Assertions.assertTrue( getUpdated.contains( "CUSTOMER" ) );

        final User i4 = new User( "chocolate", passwordEncoder.encode( "password" ) );
        final String failure = mvc
                .perform( put( "/api/v1/users/" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( i4 ) ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
    }
}
