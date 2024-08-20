package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * Represents the landing page or the initial page of the application when
     * ran.
     *
     * @param model
     *            underlying UI model
     * @return the contents of the page
     */
    @GetMapping ( { "/", "/landingpage", "/landingpage.html" } )
    public String showLandingPage ( final Model model ) {
        return "landingpage";
    }

    /**
     * Represents the userIndex HTML page that shows the user's role.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( "/userindex" )
    public String userIndex ( final Model model ) {
        return "userindex";
    }

    /**
     * orders HTML page with all user orders
     *
     * @param model
     *            underlying UI model
     * @return contents
     */
    @GetMapping ( "/orders" )
    public String orders ( final Model model ) {
        return "orders";
    }

    /**
     * Represents the index HTML page.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( "/index" )
    public String index ( final Model model ) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication != null && authentication.isAuthenticated() ) {
            if ( authentication.getAuthorities().stream()
                    .anyMatch( auth -> auth.getAuthority().equals( "ROLE_ADMIN" ) ) ) {
                return "adminindex";
            }
            return authentication.getAuthorities().stream()
                    .anyMatch( auth -> auth.getAuthority().equals( "ROLE_STAFF" ) ) ? "index" : "userindex";
        }
        return "redirect:/landingpage";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( { "/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /addrecipe, the RecipeController will return
     * /src/main/resources/templates/addrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( { "/addrecipe", "/addrecipe.html" } )
    public String addRecipe2Page ( final Model model ) {
        return "addrecipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( { "/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * On a GET request to /ingredient, the IngredientController will return
     * /src/main/resources/templates/ingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @GetMapping ( { "/ingredient", "/ingredient.html" } )
    public String addIngredientPage ( final Model model ) {
        return "ingredient";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    /**
     * Maps to page for authorizing staff members
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @GetMapping ( { "/authorize", "/authorize.html" } )
    public String authorizeUsers ( final Model model ) {
        return "authorize";
    }

    /**
     * Maps to the sign-up page for user registration
     *
     * @param model
     *            underlying UI model
     *
     * @return contents of the page
     */
    @GetMapping ( { "/signup", "signup.html" } )
    public String signUpPage ( final Model model ) {
        return "signup";
    }

    /**
     * Maps to page for viewing and fulfilling orders
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @PreAuthorize ( "hasRole('ROLE_STAFF')" )
    @GetMapping ( { "/viewfulfill", "/viewfulfill.html" } )
    public String viewFulfillOrders ( final Model model ) {
        return "viewfulfill";
    }

}
