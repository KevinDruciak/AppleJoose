import com.google.gson.Gson;
import exception.DaoException;
import model.Article;
import model.User;
import model.Statistics;
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
                int id = new Sql2oUserDao(sql2o).add(user);
                if (id > 0) {
                    model.put("added", "true");
                }
                else {
                    model.put("failedAdd", "true");
                }
            }
            catch (DaoException e) {
                model.put("failedAdd", "true");
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
            //TODO: REPLACE TEMPORARY MANUAL INFO WITH API CALLS
            String url = req.queryParams("url");
            String title = req.queryParams("title");
            String newsSource = req.queryParams("newsSource");
            int biasRating = Integer.parseInt(req.queryParams("biasRating"));
            String topic = req.queryParams("topic");
            double timeOnArticle = Double.parseDouble(req.queryParams("timeOnArticle"));
            int numWords = Integer.parseInt(req.queryParams("numWords"));
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

        //addstats route; add stats to database
        post("/addstats", (req, res) -> {
            int biasRating = Integer.parseInt(req.queryParams("biasRating"));
            String biasName = req.queryParams("biasName");
            String favNewsSource = req.queryParams("favNewsSource");
            String favTopic = req.queryParams("favTopic");
            String execSummary = req.queryParams("execSummary");
            int userID = Integer.parseInt(req.queryParams("userID"));

            Statistics stats = new Statistics(biasRating, biasName, favNewsSource, favTopic, execSummary, userID);
            new Sql2oStatisticsDao(getSql2o()).add(stats);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(stats.toString());
        });

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
