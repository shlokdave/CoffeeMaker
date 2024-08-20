package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Test class for APIIngredientController
 *
 * @author Zeeshawn Hasnain
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc ( addFilters = false )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).apply( SecurityMockMvcConfigurers.springSecurity() )
                .build();

        service.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testPost () throws UnsupportedEncodingException, Exception {
        final Ingredient i1 = new Ingredient( "chocolate", 3 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.count() );

    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testGet () throws Exception {
        final Ingredient i1 = new Ingredient( "chocolate", 3 );
        service.save( i1 );

        Assertions.assertEquals( 1, (int) service.count() );

        // Test get
        mvc.perform( get( "/api/v1/ingredients/{id}", i1.getId() ) ).andDo( print() ).andExpect( status().isOk() );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testGet2 () throws Exception {
        final Ingredient i1 = new Ingredient( "vanilla", 2 );
        final Ingredient i2 = new Ingredient( "caramel", 3 );
        final Ingredient i3 = new Ingredient( "vanilla", 4 );

        service.save( i1 );
        service.save( i2 );
        service.save( i3 );

        Assertions.assertEquals( 3, (int) service.count() );

        mvc.perform( get( "/api/v1/ingredients/{id}", i3.getId() ) ).andDo( print() ).andExpect( status().isOk() );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testGetAll () throws Exception {
        final Ingredient i1 = new Ingredient( "vanilla", 2 );
        final Ingredient i2 = new Ingredient( "caramel", 3 );
        final Ingredient i3 = new Ingredient( "cinnamon", 4 );

        service.save( i1 );
        service.save( i2 );
        service.save( i3 );

        Assertions.assertEquals( 3, (int) service.count() );

        final String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( ingredients.contains( "vanilla" ) );
        Assertions.assertTrue( ingredients.contains( "caramel" ) );
        Assertions.assertTrue( ingredients.contains( "cinnamon" ) );
    }

    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "STAFF" )
    public void testDelete () throws Exception {
        final Ingredient i1 = new Ingredient( "vanilla", 2 );
        final Ingredient i2 = new Ingredient( "caramel", 3 );
        final Ingredient i3 = new Ingredient( "vanilla", 4 );

        service.save( i1 );
        service.save( i2 );
        service.save( i3 );

        Assertions.assertEquals( 3, (int) service.count() );

        final String remove = mvc.perform( delete( "/api/v1/ingredients/{id}", i2.getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 2, (int) service.count() );
        Assertions.assertTrue( remove.contains( "caramel" ) );

        final String failure = mvc.perform( delete( "/api/v1/ingredients/{id}", i2.getId() ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals( 2, (int) service.count() );
    }
}
