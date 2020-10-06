import java.sql.*;

public class Main {
    final static String URI = "jdbc:sqlite:AppleJuice.db";
    static Connection conn;
    static Statement st;
    public static void main(String[] args) throws SQLException {
        conn = DriverManager.getConnection(URI);
        st = conn.createStatement();

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

        Server.main(new String[]{"Server Running"});

        conn.close();
    }
}
