import com.google.gson.Gson;
import model.Article;
import model.User;
import model.Statistics;
import okhttp3.OkHttpClient;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import persistence.Sql2oArticleDao;
import persistence.Sql2oUserDao;
import persistence.Sql2oStatisticsDao;


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

        // root route; show a simple message!
        get("/", (req, res) -> "Welcome to Apple Juice");

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

        //delarticle route; delete an article
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

        get("/stats", (req, res) -> {
            Sql2oStatisticsDao sql2oStatsDao = new Sql2oStatisticsDao(getSql2o());
            String results = new Gson().toJson(sql2oStatsDao.listAll());
            res.type("application/json");
            res.status(200);
            return results;
        });

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

        //delarticle route; delete an article
        post("/delstats", (req, res) -> {
            int userID = Integer.parseInt(req.queryParams("userID"));
            new Sql2oStatisticsDao(getSql2o()).delete(userID);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(userID);
        });
    }
}
