import com.google.gson.Gson;
import exception.DaoException;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import model.Article;
import model.User;
import model.Statistics;
import model.UserReadings;
import okhttp3.*;
import java.util.Date;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import persistence.Sql2oArticleDao;
import persistence.Sql2oUserDao;
import persistence.Sql2oStatisticsDao;
import persistence.Sql2oUserReadingsDao;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;


import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Server {

    private static Sql2o getSql2o() {
        // set on foreign keys
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setPragma(SQLiteConfig.Pragma.FOREIGN_KEYS, "ON");

        // create data source
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:AppleJuice.db");

        return new Sql2o(ds);
    }

    public static String extractText(String url) {
        URL urlObj = null;

        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String text = null;
        try {
            text = ArticleExtractor.INSTANCE.getText(urlObj);
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static int politicalBiasAPICall(String text) throws IOException {
        int biasRating = 100;

        OkHttpClient httpclient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("API", "gAAAAABeVpQJKRM5BqPX91XW2AKfz8pJosk182maAweJcm5ORAkkBFj__d2feG4H5KIeOKFyhUVSY_uGImiaSBCwy2L6nWxx4g==")
                .add("Text", text)
                .build();
        Request postRequest = new Request.Builder()
                .url("https://api.thebipartisanpress.com/api/endpoints/beta/robert")
                .post(body)
                .build();

        try {
            Response response = httpclient.newCall(postRequest).execute();
            biasRating = (int) Math.round(Double.parseDouble(response.body().string()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return biasRating;
    }

    public static int countWords(String s){
        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    public static void main(String[] args) {

        // set port number
        final int PORT_NUM = 7000;
        port(PORT_NUM);

        Sql2o sql2o = getSql2o();

        staticFiles.location("/public");

        post("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String username = req.queryParams("username");
            String password = req.queryParams("password");
            res.cookie("username", username);
            res.cookie("password", password);

            //TEST, inserting users to database
            User user = new User(username);
            try {
                Sql2oUserDao userDao = new Sql2oUserDao(sql2o);
                if (!(userDao.find(user) > 0)) {
                    int id = userDao.add(user);
                    //TODO: Change back to commented version; for now temp data to show how it works
//                    Statistics userStats = new Statistics(0, "Minimal Bias",
//                            "N/A", "N/A", "N/A", id);
                    Statistics userStats = new Statistics(-3, "Moderate Liberal Bias",
                            "New York Times", "Economy", "You have" +
                            " Moderate Liberal Bias. Your favorite news source is New York Times." +
                            " Your favorite topic to read about is Economy", id);
                    int idStats = new Sql2oStatisticsDao(sql2o).add(userStats);

                    model.put("addedNewUser", "true");
                }
                else {
                    model.put("existingUser", "true");
                }
            }
            catch (DaoException e) {
                model.put("failed", "true");
            }

            res.redirect("/");
            return null;
        });

        // root route; check that a user is logged in
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));
                model.put("password", req.cookie("password"));

                String username = req.cookie("username");
                User temp = new User(username);
                try {
                    int userID = new Sql2oUserDao(sql2o).find(temp);

                    //TODO FIX
                    if (userID > 0) {
                        model.put("added", "true");
                        model.put("biasRating", new Sql2oStatisticsDao(sql2o).getBias(userID));
                        model.put("biasName", new Sql2oStatisticsDao(sql2o).getBiasName(userID));
                        model.put("favNews", new Sql2oStatisticsDao(sql2o).getFavNews(userID));
                        model.put("favTopic", new Sql2oStatisticsDao(sql2o).getFavTopic(userID));
                        model.put("execSummary", new Sql2oStatisticsDao(sql2o).getExecSummary(userID));
                    }
                    else {
                        model.put("failedFind", "true");
                    }
                }
                catch (DaoException ex) {
                    model.put("failedFind", "true");
                }

            }
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/index.vm");
        }, new VelocityTemplateEngine());

        // users route; return list of users as JSON
        get("/users", (req, res) -> {
            Sql2oUserDao sql2oUser = new Sql2oUserDao(getSql2o());
            String results = new Gson().toJson(sql2oUser.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

        //adduser route; add a new user
        post("/adduser", (req, res) -> {
            String userName = req.queryParams("userName");
            User u = new User(userName);
            new Sql2oUserDao(getSql2o()).add(u);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(u.toString());
        });

        //deluser route; delete an article
        post("/deluser", (req, res) -> {
            int userID = Integer.parseInt(req.queryParams("userID"));
            new Sql2oUserDao(getSql2o()).delete(userID);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(userID);
        });

        //articles route; return list of articles as JSON
        get("/articles", (req, res) -> {
            Sql2oArticleDao article = new Sql2oArticleDao(getSql2o());
            String results = new Gson().toJson(article.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

        //addarticle route; add a new article
        post("/addarticle", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));
                model.put("password", req.cookie("password"));

                String username = req.cookie("username");
                User temp = new User(username);
                try {
                    int userID = new Sql2oUserDao(sql2o).find(temp);

                    if (userID > 0) {
                        String url = req.queryParams("url"); //chrome.history api call

                        String articleExtract = extractText(url); //extracts article text as well as title and other info
                        String[] parsedText = articleExtract.split("\\r?\\n");
                        /*
                        Get title from extracted text which is almost always first line
                        */
                        String title = parsedText[0];

                        /*
                        Get news source from extracted text, searches for the common ([New source name]) pattern
                        of news articles
                        */
                        String newsSource = null;
                        for (String line: parsedText) {
                            if (line.charAt(0) == '(') {
                                newsSource = line.substring(1, line.indexOf(')'));
                            }
                        }
                        if (newsSource == null) {
                            newsSource = "News Source could not be identified";
                        }

                        /*
                        Get bias rating for article by making call to Political Bias API
                        API provided by BiPartisanPress.com team and all credit goes to them.
                        */
                        int biasRating = politicalBiasAPICall(articleExtract);

                        //TODO: REPLACE TEMPORARY MANUAL INFO WITH API CALLS
                        String topic = req.queryParams("topic");
                        double timeOnArticle = Double.parseDouble(req.queryParams("timeOnArticle"));
                        int numWords = countWords(articleExtract); //use countWords method to get numWords from Extracted text
                        int timesVisited = Integer.parseInt(req.queryParams("timesVisited"));
                        int currentDate = (int) (new Date().getTime())/ 1000;

                        Article article = new Article(url, title, newsSource, biasRating, topic,
                                timeOnArticle, numWords, timesVisited);
                        new Sql2oArticleDao(getSql2o()).add(article);
                        UserReadings userReading = new UserReadings(userID, article.getID(), currentDate, 0);
                        new Sql2oUserReadingsDao(getSql2o()).add(userReading);
                    }
                    else {
                        model.put("failedFind", "true");
                    }
                }
                catch (DaoException ex) {
                    model.put("failedFind", "true");
                }

            }
            res.status(201);
            res.type("application/json");
            //return new Gson().toJson(article.toString());
            return null;
        });

        //delarticle route; delete an article
        post("/delarticle", (req, res) -> {
            String url = req.queryParams("url");
            new Sql2oArticleDao(getSql2o()).delete("url");
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(url);
        });

        //stats route; return all user stats
        get("/stats", (req, res) -> {
            Sql2oStatisticsDao sql2oStatsDao = new Sql2oStatisticsDao(getSql2o());
            String results = new Gson().toJson(sql2oStatsDao.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

        //TODO: Update addstats for automatic updates
        //addstats route; add stats to database
//        post("/addstats", (req, res) -> {
//            int biasRating = Integer.parseInt(req.queryParams("biasRating"));
//            String biasName = req.queryParams("biasName");
//            String favNewsSource = req.queryParams("favNewsSource");
//            String favTopic = req.queryParams("favTopic");
//            String execSummary = req.queryParams("execSummary");
//            int userID = Integer.parseInt(req.queryParams("userID"));
//
//            Statistics stats = new Statistics(biasRating, biasName, favNewsSource, favTopic, execSummary, userID);
//            new Sql2oStatisticsDao(getSql2o()).add(stats);
//            res.status(201);
//            res.type("application/json");
//            return new Gson().toJson(stats.toString());
//        });

        //delstats route; delete a user's stats
        post("/delstats", (req, res) -> {
            int userID = Integer.parseInt(req.queryParams("userID"));
            new Sql2oStatisticsDao(getSql2o()).delete(userID);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(userID);
        });
    }
}
