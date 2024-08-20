# CoffeeMaker

*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)

## Extra Credit

### Additional User Role
- Added a new role `ADMIN` which is the only role that can manage the roles of other users, through the `Authorize Staff Users` page.
- The `ADMIN` role is also the only one that can add new types of ingredients to the system.
- This functionality is removed from the staff user, but the staff user is otherwise the same.
- Default admin account with username `admin` and password `password`.
- Default staff account with username `staff` and password `password`.

### Security Audit
- Security audit and documented changes are available at https://github.ncsu.edu/engr-csc326-spring2024/csc326-TP-204-5/wiki/Security-Audit 


### Privacy Policy wiki





## API Endpoints

### Coffee
POST localhost:8080/api/v1/makecofee/{name}

### Recipes
GET localhost:8080/api/v1/recipes

GET localhost:8080/api/v1/recipes/{name}

POST localhost:8080/api/v1/recipes

DELETE localhost:8080/api/v1/recipes/{name}

### Ingredients
GET localhost:8080/api/v1/ingredients

GET localhost:8080/api/v1/ingredients/{id}

POST localhost:8080/api/v1/ingredients

DELETE localhost:8080/api/v1/ingredients/{id}

### Inventory

GET localhost:8080/api/v1/inventory

PUT localhost:8080/api/v1/inventory

POST localhost:8080/api/v1/inventory
