Recipe website that allows users to find and share meal recipes.  Users can sign up for an account, sign in and build recipes with ingredients and instructions.  A search feature provides users the ability to find recipes by name.  Each recipe has a name, category, image, list of ingredients with measurements, list of instructions, preparation time and cook time.  Users can favorite their recipes and the user profile page features a list of the recipes they have favorited.  This website features a REST API for adding recipes to a favorites list using authentication.

Application uses server mode - in order to run, start the server first with the command line:

java -cp h2*jar org.h2.tools.Server

Once the browser window opens, add the following to the JDBC URL field: jdbc:h2:tcp://localhost/./data/recipe-site

The user name and password fields are blank.

Test user login credentials:

username: user1
password: password

username: user2
password: password