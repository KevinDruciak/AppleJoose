import okhttp3.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import model.Article;
import model.Statistics;
import model.User;
import org.junit.Assert;
import java.sql.SQLException;

public class APITests {

    static OkHttpClient client;

    static String URI = "jdbc:sqlite:AppleJuiceTESTS.db";
    static Connection conn;
    static Statement st;

    @BeforeClass
    public static void beforeClassTests() throws SQLException {
        client = new OkHttpClient();
        URI = "jdbc:sqlite:./AppleJuiceTESTS.db";
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

//        sql = "CREATE TABLE IF NOT EXISTS Articles (id INTEGER PRIMARY KEY, " +
//                "url VARCHAR(1000), title VARCHAR(100), newsSource VARCHAR(100), " +
//                "biasRating INTEGER, topic VARCHAR(50), timeOnArticle DOUBLE, " +
//                "numWords INTEGER, timesVisited INTEGER);";
//        st.execute(sql);
//
//        sql = "CREATE TABLE IF NOT EXISTS Users (userID INTEGER PRIMARY KEY, " +
//                "userName VARCHAR(50), userStats VARCHAR(100), userHistory VARCHAR(100))";
//        st.execute(sql);
//
//        sql = "CREATE TABLE IF NOT EXISTS Statistics (id INTEGER, biasRating INTEGER, " +
//                "biasNAME VARCHAR(100), favNewsSources VARCHAR(100), favTopic VARCHAR(50), " +
//                "recentArticles VARCHAR(1000), execSummary VARCHAR(1000), " +
//                "userID INTEGER NOT NULL, FOREIGN KEY(userID) REFERENCES Users(userID) " +
//                "ON UPDATE CASCADE ON DELETE CASCADE)";
//        st.execute(sql);
        Server.main(new String[]{"Server Running"});
    }

    @Test
    public void test() {
        Assert.assertTrue(1 == 1);
    }


}
