import com.google.gson.Gson;
// import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import exception.DaoException;
import model.Author;
import model.Book;
import org.sql2o.Sql2o;
import persistence.Sql2oAuthorDao;
import persistence.Sql2oBookDao;
import spark.ModelAndView;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;
import spark.*;
import java.net.*;
import java.sql.*;
import spark.template.velocity.VelocityTemplateEngine;

public class Application {

    final static int PORT = 7000;
    private static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        return PORT;
    }

    private static Sql2o getSql2o() {
        final String URI = System.getenv("DATABASE_URL");
        final String USERNAME = "";
        final String PASSWORD = "";
        return new Sql2o(URI, USERNAME, PASSWORD);
    }

    public static void main(String[] args)  {
        // set port number
        port(getHerokuAssignedPort());
        workWithDatabase();

        Sql2o sql2o = getSql2o();

        staticFiles.location("/public");

        post("/", (req, res) -> {
            String username = req.queryParams("username");
            String color = req.queryParams("color");
            res.cookie("username", username);
            res.cookie("color", color);
            res.redirect("/");
            return null;
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            //Checks cookies to ensure user has logged in
            if (req.cookie("username") != null)
                model.put("username", req.cookie("username"));
            if(req.cookie("color") != null)
                model.put("color", req.cookie("color"));
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/index.vm");
        }, new VelocityTemplateEngine());

        /**
         * Get call for authors page which displays list of authors in database
         */
        get("/authors", (req, res) -> {
            if(req.cookie("username") == null){
                res.redirect("/");
            }
            Map<String, Object> model = new HashMap<>();
            model.put("authors", new Sql2oAuthorDao(sql2o).listAll());
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/authors.vm");
        }, new VelocityTemplateEngine());

        /**
         * Post call for authors page which displays authors in database
         * but also allows for the addition of a new author
         */
        post("/authors", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            String nationality = req.queryParams("nationality");
            int numOfBooks = Integer.parseInt(req.queryParams("numOfBooks"));
            Author author = new Author(name, numOfBooks, nationality);
            try {
                int id = new Sql2oAuthorDao(sql2o).add(author);
                if (id > 0) {
                    model.put("added", "true");
                    model.put("authors", new Sql2oAuthorDao(sql2o).listAll());
                }
                else {
                    model.put("failedAdd", "true");
                }
            }
            catch (DaoException ex) {
                model.put("failedAdd", "true");
            }
            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/authors.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        /**
         * Get call for add authors to display formatted view model for the page
         */
        get("/addauthor", (req, res) -> {
            if(req.cookie("username") == null){
                res.redirect("/");
            }
            Map<String, Object> model = new HashMap<>();
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/addauthor.vm");
        }, new VelocityTemplateEngine());

        /**
         * Post call for add author which allows user add author to the database
         */
        post("/addauthor", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("name");
            String nationality = req.queryParams("nationality");
            int numOfBooks = Integer.parseInt(req.queryParams("numOfBooks"));
            Author author = new Author(name, numOfBooks, nationality);
            try {
                int id = new Sql2oAuthorDao(sql2o).add(author);
                if (id > 0) {
                    model.put("added", "true");
                }
                else {
                    model.put("failedAdd", "true");
                }
            }
            catch (DaoException ex) {
                model.put("failedAdd", "true");
            }
            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/addauthor.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        /**
         * Post call which allows a user to delete a specific author from database
         */
        post("/delauthor", (req, res) -> {
            String name = req.queryParams("name");
            Author a = new Author(name, 0, "");
            new Sql2oAuthorDao(getSql2o()).delete(a);
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(a.toString());
        });

        /**
         * Get call which displays all the books in the database
         */
        get("/books", (req, res) -> {
            if(req.cookie("username") == null){
                res.redirect("/");
            }
            Map<String, Object> model = new HashMap<>();
            model.put("books", new Sql2oBookDao(sql2o).listAll());
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/books.vm");
        }, new VelocityTemplateEngine());

        /**
         * Post call which displays all books in the database but also
         * allows users to add a book at the bottom of the page
         */
        post("/books", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String title = req.queryParams("title");
            String isbn = req.queryParams("isbn");
            String publisher = req.queryParams("publisher");
            int year = Integer.parseInt(req.queryParams("year"));
            String name = req.queryParams("name");
            int numOfBooks = Integer.parseInt(req.queryParams("numOfBooks"));
            String nationality = req.queryParams("nationality");
            Author a = new Author(name, numOfBooks, nationality);
            try {
                int authorId = new Sql2oAuthorDao(sql2o).add(a);
                Book b = new Book(title, isbn, publisher, year, authorId);
                int id = new Sql2oBookDao(sql2o).add(b);
                if (id > 0) {
                    model.put("added", "true");
                    model.put("books", new Sql2oBookDao(sql2o).listAll());
                }
                else {
                    model.put("failedAdd", "true");
                }
            }
            catch (DaoException ex) {
                model.put("failedAdd", "true");
            }

            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/books.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        /**
         * Get call which displays view information for the addbook page
         */
        get("/addbook", (req, res) -> {
            if(req.cookie("username") == null){
                res.redirect("/");
            }
            Map<String, Object> model = new HashMap<>();
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/addbook.vm");
        }, new VelocityTemplateEngine());

        /**
         * Post call which allows users to add a new book to the database along with author
         * information
         */
        post("/addbook", (req, res) -> {

            Map<String, Object> model = new HashMap<>();
            String title = req.queryParams("title");
            String isbn = req.queryParams("isbn");
            String publisher = req.queryParams("publisher");
            int year = Integer.parseInt(req.queryParams("year"));
            String name = req.queryParams("name");
            int numOfBooks = Integer.parseInt(req.queryParams("numOfBooks"));
            String nationality = req.queryParams("nationality");
            Author a = new Author(name, numOfBooks, nationality);
            try {
                int authorId = new Sql2oAuthorDao(sql2o).add(a);
                Book b = new Book(title, isbn, publisher, year, authorId);
                int id = new Sql2oBookDao(sql2o).add(b);
                if (id > 0) {
                    model.put("added", "true");
                }
                else {
                    model.put("failedAdd", "true");
                }
            }
            catch (DaoException ex) {
                model.put("failedAdd", "true");
            }

            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/addbook.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        /**
         * Post call to allow for users to delete books from the database
         */
        post("/delbook", (req, res) -> {
            String isbn = req.queryParams("isbn");
            Book b = new Book("", isbn, "", 0, 0);
            new Sql2oBookDao(getSql2o()).delete(b);
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(b.toString());
        });
    }

    public static void workWithDatabase() {
        try (Connection conn = getConnection()) {
            String sql;

            sql = "CREATE TABLE IF NOT EXISTS Authors (id serial PRIMARY KEY, name VARCHAR(100) NOT NULL UNIQUE," +
                        " numOfBooks INTEGER, nationality VARCHAR(30));";
            Statement st = conn.createStatement();
            st.execute(sql);

            sql = "INSERT INTO Authors(name, numOfBooks, nationality) VALUES ('Leo Tolstoy', 12, 'Russian');";
            st.execute(sql);

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        String databaseUrl = System.getenv("DATABASE_URL");

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}

