import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

import org.apache.bcel.generic.INSTANCEOF;

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
                "userName VARCHAR(50))";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Statistics (id INTEGER, biasRating INTEGER, " +
                "biasNAME VARCHAR(100), favNewsSource VARCHAR(100), favTopic VARCHAR(50), " +
                "recentArticles VARCHAR(1000), execSummary VARCHAR(1000), " +
                "userID INTEGER NOT NULL, FOREIGN KEY(userID) REFERENCES Users(userID) " +
                "ON UPDATE CASCADE ON DELETE CASCADE)";
        st.execute(sql);

        /*
        //CODE USED FOR POLITICAL API TESTING

        URL urlObj = null;
        try {
            urlObj = new URL("https://www.breitbart.com/politics/2020/10/20/fbi-doj-concur-hunter-biden-laptop-emails-not-russian-disinformation/");
        } catch (MalformedURLException e) {
        }
        String text = null;
        try {
            text = ArticleExtractor.INSTANCE.getText(urlObj);
        } catch (BoilerpipeProcessingException e) {
        }
        System.out.print(text);
        try {
            int i = Server.politicalBiasAPICall(text);
            System.out.print(i);
        } catch (IOException i) {
            i.printStackTrace();
        }
        */

        Server.main(new String[]{"Server Running"});

        conn.close();
    }
}
