import okhttp3.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import model.Author;
import model.Book;
import org.junit.Assert;

import static org.junit.Assert.*;

public class RESTAPITest {

    static OkHttpClient client;

    static String URI;
    static Connection conn;
    static Statement st;

    @BeforeClass
    public static void beforeClassTests() throws SQLException {
        client = new OkHttpClient();
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

        request = new Request.Builder()
                .url("http://localhost:7000/authors")
                .build();
        response = client.newCall(request).execute();
        String resBody = response.body().string();
        Assert.assertEquals("[{\"id\":1,\"name\":\"Sadegh Hedayat\"," +
                "\"numOfBooks\":26,\"nationality\":\"Iranian\"}]", resBody);

        postBody = new FormBody.Builder()
                .add("name", "Ice Cream")
                .add("numOfBooks", "42")
                .add("nationality", "Iceland")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/addauthor")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/authors")
                .build();
        response = client.newCall(request).execute();
        resBody = response.body().string();
        Assert.assertEquals("[{\"id\":1,\"name\":\"Sadegh Hedayat\"," +
                "\"numOfBooks\":26,\"nationality\":\"Iranian\"}," +
                "{\"id\":2,\"name\":\"Ice Cream\"," +
                "\"numOfBooks\":42,\"nationality\":\"Iceland\"}]", resBody);
    }

    @Test
    public void testDeleteAuthor() throws IOException, InterruptedException {
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
                .add("numOfBooks", "42")
                .add("nationality", "Iceland")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/addauthor")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/authors")
                .build();
        response = client.newCall(request).execute();
        String resBody = response.body().string();
        Assert.assertEquals("[{\"id\":1,\"name\":\"Sadegh Hedayat\"," +
                "\"numOfBooks\":26,\"nationality\":\"Iranian\"}," +
                "{\"id\":2,\"name\":\"Ice Cream\"," +
                "\"numOfBooks\":42,\"nationality\":\"Iceland\"}]", resBody);

        postBody = new FormBody.Builder()
                .add("name", "Sadegh Hedayat")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/delauthor")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/authors")
                .build();
        response = client.newCall(request).execute();
        resBody = response.body().string();
        Assert.assertEquals("[{\"id\":2,\"name\":\"Ice Cream\"," +
                "\"numOfBooks\":42,\"nationality\":\"Iceland\"}]", resBody);

        postBody = new FormBody.Builder()
                .add("name", "Ice Cream")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/delauthor")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/authors")
                .build();
        response = client.newCall(request).execute();
        resBody = response.body().string();
        Assert.assertEquals("[]", resBody);
    }

    @Test
    public void testListBooks() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:7000/books")
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());
    }

    @Test
    public void testAddBook() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("name", "SadeghHedayat")
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
                .add("title", "Desert")
                .add("isbn", "123")
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
        String resBody = response.body().string();
        System.out.println(resBody);
        Assert.assertEquals("[{\"id\":1,\"title\":\"Desert\",\"isbn\":\"123\"," +
                "\"publisher\":\"JHU\",\"year\":2020,\"authorId\":1}]", resBody);
    }

    @Test
    public void testDeleteBook() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("name", "SadeghHedayat")
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
                .add("title", "Desert")
                .add("isbn", "123")
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
        String resBody = response.body().string();
        System.out.println(resBody);
        Assert.assertEquals("[{\"id\":1,\"title\":\"Desert\",\"isbn\":\"123\"," +
                "\"publisher\":\"JHU\",\"year\":2020,\"authorId\":1}]", resBody);

        postBody = new FormBody.Builder()
                .add("isbn", "123")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/delbook")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/books")
                .build();
        response = client.newCall(request).execute();
        resBody = response.body().string();
        System.out.println(resBody);
        Assert.assertEquals("[]", resBody);
    }

}