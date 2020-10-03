import com.google.gson.Gson;
import model.Author;
import model.Book;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import persistence.Sql2oAuthorDao;
import persistence.Sql2oBookDao;

import static spark.Spark.*;

public class Server {

    private static Sql2o getSql2o() {
        // set on foreign keys
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setPragma(SQLiteConfig.Pragma.FOREIGN_KEYS, "ON");

        // create data source
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:MyBooksApp.db");

        return new Sql2o(ds);
    }

    public static void main(String[] args)  {
        // set port number
        final int PORT_NUM = 7000;
        port(PORT_NUM);

        // root route; show a simple message!
        get("/", (req, res) -> "Welcome to MyBooksApp");

        // authors route; return list of authors as JSON
        get("/authors", (req, res) -> {
            Sql2oAuthorDao sql2oAuthor = new Sql2oAuthorDao(getSql2o());
            String results = new Gson().toJson(sql2oAuthor.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

        //addauthor route; add a new author
        post("/addauthor", (req, res) -> {
            String name = req.queryParams("name");
            int numOfBooks = Integer.parseInt(req.queryParams("numOfBooks"));
            String nationality = req.queryParams("nationality");
            Author a = new Author(name, numOfBooks, nationality);
            new Sql2oAuthorDao(getSql2o()).add(a);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(a.toString());
        });

        post("/delauthor", (req, res) -> {
            String name = req.queryParams("name");
            new Sql2oAuthorDao(getSql2o()).delete(name);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(name);
        });

        //books route; return list of books as JSON
        get("/books", (req, res) -> {
            Sql2oBookDao sql2oBook = new Sql2oBookDao(getSql2o());
            String results = new Gson().toJson(sql2oBook.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

        //addbook route; add a new author
        post("/addbook", (req, res) -> {
            String title = req.queryParams("title");
            String isbn = req.queryParams("isbn");
            String publisher = req.queryParams("publisher");
            int year = Integer.parseInt(req.queryParams("year"));
            int authorId = Integer.parseInt(req.queryParams("authorId"));
            Book b = new Book(title, isbn, publisher, year, authorId);
            new Sql2oBookDao(getSql2o()).add(b);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(b.toString());
        });

        post("/delbook", (req, res) -> {
            int isbn = Integer.parseInt(req.queryParams("isbn"));
            new Sql2oBookDao(getSql2o()).delete(isbn);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(isbn);
        });

    }
}
