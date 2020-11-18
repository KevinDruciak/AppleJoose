import model.User;
import model.Article;
import model.Statistics;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class DBPersistenceTest {
    static String URI;
    static Connection conn;
    static Statement st;

    @BeforeClass
    public static void beforeClassTests() throws SQLException {
        URI = "jdbc:sqlite:AppleJuice.db";
        conn = DriverManager.getConnection(URI);
        st = conn.createStatement();

        String sql = "DROP TABLE IF EXISTS Articles";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Users";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS UserReadings";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Statistics";
        st.execute(sql);
    }

    @AfterClass
    public static void afterClassTests() throws SQLException {
        String sql = "DROP TABLE IF EXISTS Articles";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Users";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Statistics";
        st.execute(sql);
    }

    @Before
    public void beforeEachTest() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Articles (id INTEGER PRIMARY KEY, " +
                "url VARCHAR(1000), title VARCHAR(100), newsSource VARCHAR(100), " +
                "biasRating INTEGER, topic VARCHAR(50), timeOnArticle DOUBLE, " +
                "numWords INTEGER, timesVisited INTEGER);";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Users (userID INTEGER PRIMARY KEY, " +
                "userName VARCHAR(50), userStats VARCHAR(100), userHistory VARCHAR(100))";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Statistics (id INTEGER, biasRating INTEGER, " +
                "biasNAME VARCHAR(100), favNewsSources VARCHAR(100), favTopic VARCHAR(50), " +
                "recentArticles VARCHAR(1000), execSummary VARCHAR(1000), " +
                "userID INTEGER NOT NULL, FOREIGN KEY(userID) REFERENCES Users(userID) " +
                "ON UPDATE CASCADE ON DELETE CASCADE)";
        st.execute(sql);
    }

    @Test
    public void testInsertNewUser() throws SQLException {
        User u = new User("Joao Kawase", "password");
        String sql = "INSERT INTO Users(userName) VALUES (?);";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, u.getUserName());
        assertFalse(pst.execute());
        sql = "SELECT * FROM Users WHERE userName=\"Joao Kawase\"";
        ResultSet rs = st.executeQuery(sql);
        assertTrue(rs.next());
    }

}
