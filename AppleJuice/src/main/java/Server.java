import com.google.gson.Gson;
import de.l3s.boilerpipe.sax.InputSourceable;
import exception.DaoException;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLFetcher;

import model.Article;
import model.User;
import model.Statistics;
import model.UserReadings;
import okhttp3.*;

import java.util.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import persistence.Sql2oArticleDao;
import persistence.Sql2oUserDao;
import persistence.Sql2oStatisticsDao;
import persistence.Sql2oUserReadingsDao;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

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
        TextDocument doc = null;
        String text = null;

        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            InputSource is = HTMLFetcher.fetch(urlObj).toInputSource();
            BoilerpipeSAXInput in = new BoilerpipeSAXInput(is);
            doc = in.getTextDocument();
        } catch (IOException | SAXException | BoilerpipeProcessingException err) {
            err.printStackTrace();
        }

        try {
            text = ArticleExtractor.INSTANCE.getText(urlObj);
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
        }
        // append title to text
        text = doc.getTitle() + '\n' + text;
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
                if (userDao.find(user.getUserName()) == null) {
                    int id = userDao.add(user);
                    //TODO: Change back to commented version; for now temp data to show how it works
//                    Statistics userStats = new Statistics(0, "Minimal Bias",
//                            "N/A", "N/A", "N/A", id);
                    Statistics userStats = new Statistics(0, "Neutral Bias",
                            "New York Times", "Economy", "You have" +
                            " Neutral. Your favorite news source is New York Times." +
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
                try {
                    User u = new Sql2oUserDao(sql2o).find(username);
                    List<UserReadings> uReadings = new Sql2oUserReadingsDao(sql2o).getMostRecentUserReadings(u.getUserID(), 5);
                    List<Article> articles = new ArrayList<>();

                    for(UserReadings ur : uReadings) {
                        articles.add(new Sql2oArticleDao(sql2o).find(ur.getArticleid()));
                    }
                    Statistics stats = new Sql2oStatisticsDao(sql2o).find(u.getUserID());

                    if (u.getUserID() > 0) {

                        model.put("added", "true");
                        model.put("biasRating", stats.getBiasRating());
                        model.put("biasName", stats.getBiasName());
                        model.put("favNews", stats.getFavNewsSource());
                        model.put("favTopic", stats.getFavTopic());
                        model.put("execSummary", stats.getExecSummary());
                        model.put("Articles", articles);

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
            res.type("text/html");
            res.status(200);
            return results;
        });

        //addArticle route;
        get("/addarticle", (req, res) -> {
            if(req.cookie("username") == null){
                res.redirect("/");
            }

            Map<String, Object> model = new HashMap<String, Object>();
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/addarticle.vm");
        }, new VelocityTemplateEngine());

        //addarticle route; add a new article
        post("/addarticle", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));

                String username = req.cookie("username");
                User temp = new User(username);
                try {
                    User u = new Sql2oUserDao(sql2o).find(username);

              
              String url = req.queryParams("url"); //chrome.history api call

              String articleExtract = extractText(url); //extracts article text as well as title and other info
              String[] parsedText = articleExtract.split("\\r?\\n");

              /*
              Get title from extracted text which is almost always first line
              */
              String title = parsedText[0];

              /*
              Get news source from extracted url host name
              */
              String newsSource = new URL(url).getHost();
              newsSource = newsSource.replace("www.", "");
              if (newsSource == null) {
                  newsSource = "News Source could not be identified";
              }

              int biasRating = politicalBiasAPICall(articleExtract);

              //TODO: REPLACE TEMPORARY MANUAL INFO WITH API CALLS
              String topic = req.queryParams("topic");
              double timeOnArticle = 0;
              int numWords = countWords(articleExtract); //use countWords method to get numWords from Extracted text
              int timesVisited = 0;
              int currentDate = (int) (new Date().getTime())/ 1000;
                  
              Article article = new Article(url, title, newsSource, biasRating, topic,
                                            timeOnArticle, numWords, timesVisited);
              new Sql2oArticleDao(getSql2o()).add(article);
                        
              UserReadings userReading = new UserReadings(u.getUserID(), article.getArticleID(), currentDate, 0);
              new Sql2oUserReadingsDao(getSql2o()).add(userReading);
                    
              }
                catch (DaoException ex) {
                    model.put("failedFind", "true");
              }
            } else {
                  model.put("failedFind", "true");
            }
             
            res.status(201);
            res.type("text/html");
            res.redirect("/");
            return new ModelAndView(model, "public/templates/addarticle.vm");
        });

        //delarticle route; delete an article
        post("/delarticle", (req, res) -> {
            String url = req.queryParams("url");
            new Sql2oArticleDao(getSql2o()).delete("url");
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(url);
        });

        //stats route; return all user stats
        get("/stats", (req, res) -> {
            Sql2oStatisticsDao sql2oStatsDao = new Sql2oStatisticsDao(getSql2o());
            String results = new Gson().toJson(sql2oStatsDao.listAll());
            res.type("text/html");
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
