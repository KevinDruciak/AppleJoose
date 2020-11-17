import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import exception.DaoException;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import model.Article;
import model.User;
import model.Statistics;
import okhttp3.*;

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

            //TEST, inserting users to database
            User user = new User(username, null);
            try {
                Sql2oUserDao userDao = new Sql2oUserDao(sql2o);
                if (userDao.find(user) > 0) {
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userDao.getPassword(userDao.find(user)));
                    //if (userDao.getPassword(userDao.find(user)).equals(BCrypt.withDefaults().hashToString(12, password.toCharArray()))) {
                    if (result.verified) {
                        model.put("existinguser", "true");
                        model.put("username", username);
                        res.cookie("username", username); //set this only if success

                    }
                }
                else {
                    model.put("existingUser", "true");
                }
            }
            catch (DaoException e) {
                model.put("failed", "true");
            }

            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/index.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        // root route; check that a user is logged in
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));
                //model.put("password", req.cookie("password"));

                String username = req.cookie("username");
                User temp = new User(username, null); //changed constructor
                try {
                    //int userID = new Sql2oUserDao(sql2o).find(temp);
                    int userID = new Sql2oUserDao(sql2o).findNAME(username);
                    System.out.println(userID);
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
                    System.out.println("FAILED TRY /");
                }

            }
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/index.vm");
        }, new VelocityTemplateEngine());

        //signup page
        get("/signup", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            res.status(200);
            res.type("text/html");
            return new ModelAndView(model, "public/templates/signup.vm");
        }, new VelocityTemplateEngine());

        post("/signup", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String username = req.queryParams("username");
            String password = req.queryParams("password");
            String confirmPW = req.queryParams("confirmPW");

            User user2 = new User(username, null); //changed constrcutor
            try {
                Sql2oUserDao userDao = new Sql2oUserDao(sql2o);
                if (userDao.find(user2) > 0) {
                    model.put("userExists", "true");
//                    res.status(201);
//                    res.type("text/html");
//                    ModelAndView mdl = new ModelAndView(model, "public/templates/signup.vm");
//                    return new VelocityTemplateEngine().render(mdl);
                }
                else {

                    model.put("userExists", "false");
                    model.put("added", "true");
                    String bcryptHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                    User user = new User(username, bcryptHash);
                    //userDao.add(user);
                    int userID = new Sql2oUserDao(getSql2o()).add(user);

                    Statistics stats = new Statistics(0, "Neutral Bias",
                            "N/A", "N/A", "You have" +
                            " Neutral Bias. Your favorite news source is N/A." +
                            " Your favorite topic to read about is N/A", userID);
                    int idStats = new Sql2oStatisticsDao(sql2o).add(stats);

                    //Sql2oArticleDao artDao = new Sql2oArticleDao(sql2o);
                    //Article art = new Article(username, bcryptHash, username, 123, username, 123, 123, 123);
                    //artDao.add(art);
                }
            }
            catch (DaoException e) {
                model.put("failed", "true");
            }

//            if (userExists) {
//                res.status(201);
//                res.type("text/html");
//                ModelAndView mdl = new ModelAndView(model, "public/templates/signup.vm");
//                return new VelocityTemplateEngine().render(mdl);
//            }
//
//            String bcryptHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/signup.vm");
            return new VelocityTemplateEngine().render(mdl);
        });


        // users route; return list of users as JSON
        get("/users", (req, res) -> {
            Sql2oUserDao userDao = new Sql2oUserDao(sql2o);
            String results = new Gson().toJson(userDao.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

        //adduser route; add a new user
        post("/adduser", (req, res) -> {
            String userName = req.queryParams("userName");
            User u = new User(userName, null); //changed construftor
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
            //TODO: REPLACE TEMPORARY MANUAL INFO WITH API CALLS
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

            String topic = req.queryParams("topic");
            double timeOnArticle = Double.parseDouble(req.queryParams("timeOnArticle"));
            int numWords = countWords(articleExtract); //use countWords method to get numWords from Extracted text
            int timesVisited = Integer.parseInt(req.queryParams("timesVisited"));

            Article article = new Article(url, title, newsSource, biasRating, topic,
                    timeOnArticle, numWords, timesVisited);
            new Sql2oArticleDao(getSql2o()).add(article);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(article.toString());
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
