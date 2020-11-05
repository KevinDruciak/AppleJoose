import okhttp3.*;
import org.junit.*;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import model.Article;
import model.Statistics;
import model.User;

import static org.junit.Assert.*;

import java.sql.SQLException;

public class APITests {

    static OkHttpClient client;

    static String URI = "jdbc:sqlite:AppleJuice.db";
    static Connection conn;
    static Statement st;

    @BeforeClass
    public static void beforeClassTests() throws SQLException {
        client = new OkHttpClient();
        URI = "jdbc:sqlite:./AppleJuice.db";
        conn = DriverManager.getConnection(URI);
        st = conn.createStatement();
    }

    @Before
    public void beforeTests() throws SQLException {
        String sql = "DROP TABLE IF EXISTS Articles";
        st.execute(sql);
        sql = "DROP TABLE IF EXISTS Users";
        st.execute(sql);
        sql = "DROP TABLE IF EXISTS Statistics";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Articles (id INTEGER PRIMARY KEY, " +
                "url VARCHAR(1000), title VARCHAR(100), newsSource VARCHAR(100), " +
                "biasRating INTEGER, topic VARCHAR(50), timeOnArticle DOUBLE, " +
                "numWords INTEGER, timesVisited INTEGER);";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Users (userID INTEGER PRIMARY KEY, " +
                "userName VARCHAR(50))";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Statistics (id INTEGER, biasRating INTEGER, " +
                "biasNAME VARCHAR(100), favNewsSources VARCHAR(100), favTopic VARCHAR(50), " +
                "recentArticles VARCHAR(1000), execSummary VARCHAR(1000), " +
                "userID INTEGER NOT NULL, FOREIGN KEY(userID) REFERENCES Users(userID) " +
                "ON UPDATE CASCADE ON DELETE CASCADE)";
        st.execute(sql);

    }

    @Test
    public void testAddUser() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("userName", "Kevin Druciak")
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:7000/adduser")
                .post(postBody)
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/users")
                .build();
        response = client.newCall(request).execute();
        String resBody = response.body().string();
        System.out.println(resBody);
        assertEquals("[{\"userID\":1,\"userName\":\"Kevin Druciak\"}]", resBody);
    }

    @Test
    public void testAddArticle() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("url", "https://www.cnn.com/2020/11/04/weather/hurricane-eta-wednesday/index.html")
                .add("topic", "weather")
                .add("timeOnArticle", "0")
                .add("timesVisited", "1")
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:7000/addarticle")
                .post(postBody)
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/articles")
                .build();
        response = client.newCall(request).execute();
        String resBody = response.body().string();
        assertEquals("[{\"id\":1,\"url\":\"https://www.cnn.com/2020/11/04/weather/hurricane-eta-wednesday/index.html\"," +
                "\"title\":\"Apple Juice\",\"newsSource\":\"NYT\",\"biasRating\":7," +
                "\"topic\":\"OOSE\",\"timeOnArticle\":0.0,\"numWords\":240,\"timesVisited\"" +
                ":1}]", resBody);
    }

    /*@Test
    public void testAddStatistics() throws IOException {
        RequestBody postBody = new FormBody.Builder()
                .add("userName", "Kevin Druciak")
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:7000/adduser")
                .post(postBody)
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());

        postBody = new FormBody.Builder()
                .add("biasRating", "2")
                .add("biasName", "left")
                .add("favNewsSource", "msn")
                .add("favTopic", "sports")
                //.add("recentArticles", "2")
                .add("execSummary", "summary")
                .add("userID", "1")
                .build();
        request = new Request.Builder()
                .url("http://localhost:7000/addstats")
                .post(postBody)
                .build();
        response = client.newCall(request).execute();
        assertEquals(201, response.code());

        request = new Request.Builder()
                .url("http://localhost:7000/stats")
                .build();
        response = client.newCall(request).execute();
        String resBody = response.body().string();
        assertEquals("[{\"id\":1,\"url\":\"www.applejuice.com\"," +
                "\"title\":\"Apple Juice\",\"newsSource\":\"NYT\",\"biasRating\":7," +
                "\"topic\":\"OOSE\",\"timeOnArticle\":0.0,\"numWords\":240,\"timesVisited\"" +
                ":1}]", resBody);
    }*/
}
