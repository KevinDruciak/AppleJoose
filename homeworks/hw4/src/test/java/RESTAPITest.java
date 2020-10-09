import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Author;
import okhttp3.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.sql.Connection;


import static org.junit.Assert.*;

public class RESTAPITest {

    static OkHttpClient client;
    static Gson gson;

    static String URI;
    static Connection conn;
    static Statement st;

    @BeforeClass
    public static void beforeClassTests() throws SQLException {
        client = new OkHttpClient();
        gson = new Gson();

        URI = "jdbc:sqlite:./MyBooksApp.db";
        conn = DriverManager.getConnection(URI);
        st = conn.createStatement();
    }

    @Before
    public void beforeTests() throws SQLException {
        String sql = "DROP TABLE IF EXISTS Authors";
        st.execute(sql);
        sql = "DROP TABLE IF EXISTS Books";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Authors (id INTEGER PRIMARY KEY, name VARCHAR(100) NOT NULL UNIQUE," +
                " numOfBooks INTEGER, nationality VARCHAR(30));";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Books (id INTEGER PRIMARY KEY, title VARCHAR(200) NOT NULL," +
                " isbn VARCHAR(14) NOT NULL UNIQUE, publisher VARCHAR(14), year INTEGER," +
                " authorId INTEGER, FOREIGN KEY(authorId) REFERENCES Authors(id)" +
                " ON UPDATE CASCADE ON DELETE CASCADE);";
        st.execute(sql);
    }

    @Test
    public void testListAuthors() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:7000/authors")
                .build();
        Response response = client.newCall(request).execute();
        String resBody = response.body().string();
        Author[] authors = gson.fromJson(resBody, Author[].class);
        // loop through authors and do extra assertions
        assertEquals(200, response.code());
    }

    @Test
    public void testAddAuthor() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("name", "Sadegh Hedayat")
                .add("numOfBooks", "26")
                .add("nationality", "Iranian")
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:7000/addauthor")
                .post(postBody)
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());

        postBody = new FormBody.Builder()
                .add("name", "Ice Cream")
                .add("numOfBooks", "3")
                .add("nationality", "Iceland")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/addauthor")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());
    }

    @Test
    public void testAddBook() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("name", "Sadegh Hedayat")
                .add("numOfBooks", "26")
                .add("nationality", "Iranian")
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:7000/addauthor")
                .post(postBody)
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());

        postBody = new FormBody.Builder()
                .add("title", "Iceberg")
                .add("isbn", "123-000045")
                .add("publisher", "JHU")
                .add("year", "2020")
                .add("authorId", "1")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/addbook")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/books")
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());
    }
}