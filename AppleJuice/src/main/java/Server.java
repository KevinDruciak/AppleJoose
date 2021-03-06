import at.favre.lib.crypto.bcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import java.net.*;
import java.sql.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Server {

    static boolean LOCAL = true; //set FALSE if deploying/running on heroku, TRUE if testing locally
    static Connection conn;
    static Statement st;

    final static int PORT = 7000;

    private static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        return PORT;
    }

    private static Connection getConnection() throws URISyntaxException, SQLException, ClassNotFoundException {
        String databaseUrl = System.getenv("DATABASE_URL");

        URI dbUri = new URI(databaseUrl);
        Class.forName("org.postgresql.Driver");
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort()
                + dbUri.getPath() + "?sslmode=require";
        System.out.println(dbUrl);
        return DriverManager.getConnection(dbUrl, username, password);
    }

    private static void workWithDatabase() {
        try (Connection conn = getConnection()) {
            String sql;
            Statement st = conn.createStatement();

            sql = "CREATE TABLE IF NOT EXISTS Articles (articleID serial PRIMARY KEY, " +
                    "url VARCHAR(1000) UNIQUE, title VARCHAR(1000), newsSource VARCHAR(1000), " +
                    "biasRating INTEGER, topic VARCHAR(50), " +
                    "numWords INTEGER);";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS Users (userID serial PRIMARY KEY, " +
                    "userName VARCHAR(50) UNIQUE, userPassword VARCHAR(64), userStatsID INTEGER UNIQUE);";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS Statistics (id serial PRIMARY KEY, biasRating INT, " +
                    "biasName TEXT, favNewsSource TEXT, favTopic TEXT, " +
                    "execSummary VARCHAR(1000), " +
                    "userID INTEGER NOT NULL UNIQUE, FOREIGN KEY(userID) REFERENCES Users(userID) " +
                    "ON UPDATE CASCADE ON DELETE CASCADE);";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS UserReadings (articleID INTEGER NOT NULL, " +
                    "userID INTEGER NOT NULL, dateRead BIGINT NOT NULL, readingID serial, " +
                    "FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE)";
            st.execute(sql);

        } catch (SQLException | URISyntaxException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Sql2o getSql2o() throws URISyntaxException {
        /*
        Get Heroku url for web app
         */
        String databaseUrl = System.getenv("DATABASE_URL");

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return new Sql2o(dbUrl, username, password);
    }

    private static Sql2o getSql2oLOCAL() {
        // set on foreign keys
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setPragma(SQLiteConfig.Pragma.FOREIGN_KEYS, "ON");

        // create data source
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:AppleJuice.db");

        return new Sql2o(ds);
    }

    static ArrayList<Float> dailyAvgBias = new ArrayList<>();
    static ArrayList<String> dailyAvgDates = new ArrayList<>();
    //    static int tempDate = 0;
    static Map<Integer, ArrayList<Integer>> userDailyAvgBias = new HashMap<>();
    static Map<Integer, ArrayList<String>> userDailyAvgDates = new HashMap<>();

    private static void updateDailyAvgBias(Sql2o stats) {
        try {
            Sql2oStatisticsDao sql2oStats = new Sql2oStatisticsDao(stats);

            if (sql2oStats.listBIAS().size() == 0) {
                return;
            }

            //DAILY AVG BIAS/DATES FOR ALL USERS
            float dailyAvg = sql2oStats.avgBIAS(sql2oStats.listBIAS());
            dailyAvgBias.add(dailyAvg);

            Date date = new Date();
            SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
            form.setTimeZone(TimeZone.getTimeZone("EST"));
            dailyAvgDates.add(form.format(date));

            //DAILY AVG BIAS/DATES FOR SPECIFIC USERS
            int statsSIZE = sql2oStats.statsSize();
            for (int i = 1; i <= statsSIZE; i++) {
                if (userDailyAvgBias.get(i) == null) {
                    ArrayList<Integer> tempAvgBias = new ArrayList<>();
                    ArrayList<String> tempAvgDates = new ArrayList<>();
                    tempAvgBias.add(sql2oStats.getBiasRatingUser(i));
                    tempAvgDates.add(form.format(date));
                    userDailyAvgBias.put(i, tempAvgBias);
                    userDailyAvgDates.put(i, tempAvgDates);
                }
                else {
                    userDailyAvgBias.get(i).add(sql2oStats.getBiasRatingUser(i));
                    userDailyAvgDates.get(i).add(form.format(date));
                }
            }

        } catch (NullPointerException e) {
            //do nothing
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws URISyntaxException, SQLException {
        Sql2o sql2o;
        if (!LOCAL) {
            port(getHerokuAssignedPort());
            workWithDatabase();
            sql2o = getSql2o();
        }
        else {
            port(PORT);

            conn = DriverManager.getConnection("jdbc:sqlite:AppleJuice.db");
            st = conn.createStatement();
            String sql;

            //COMMENT THIS OUT IF YOU WANT TO HAVE PERSISTENCE FOR TESTING
            sql = "DROP TABLE IF EXISTS Articles";
            st.execute(sql);
            sql = "DROP TABLE IF EXISTS Users";
            st.execute(sql);
            sql = "DROP TABLE IF EXISTS Statistics";
            st.execute(sql);
            sql = "DROP TABLE IF EXISTS UserReadings";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS Articles (articleID INTEGER PRIMARY KEY, " +
                    "url VARCHAR(1000) UNIQUE, title VARCHAR(100), newsSource VARCHAR(100), " +
                    "biasRating INTEGER, topic VARCHAR(50), " +
                    "numWords INTEGER);";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS Users (userID INTEGER PRIMARY KEY, " +
                    "userName VARCHAR(50) UNIQUE, userPassword VARCHAR(1000), userStatsID INTEGER);";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS Statistics (id INTEGER PRIMARY KEY, biasRating INTEGER, " +
                    "biasNAME VARCHAR(100), favNewsSource VARCHAR(100), favTopic VARCHAR(50), " +
                    "execSummary VARCHAR(1000), " +
                    "userID INTEGER NOT NULL UNIQUE, FOREIGN KEY(userID) REFERENCES Users(userID) " +
                    "ON UPDATE CASCADE ON DELETE CASCADE);";
            st.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS UserReadings (userID INTEGER NOT NULL, " +
                    "articleID INTEGER NOT NULL, dateRead INTEGER NOT NULL, readingID INTEGER, " +
                    "FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE)";
            st.execute(sql);

//            sql = "INSERT INTO Users(userID, userName, userPassword, userStatsID)" +
//                    " VALUES (1, 'Politics', 'adminPassword', 1);";
//            st.execute(sql);
//
//            sql = "INSERT INTO Statistics (id, biasRating, biasName,favNewsSource," +
//                    " favTopic, execSummary, userID) VALUES (1, 0, 'Minimal Bias'," +
//                    " 'CNN', 'Politics', 'ADMIN SUMMARY', 1);";
//            st.execute(sql);

            sql2o = getSql2oLOCAL();
        }

        staticFiles.location("/public");

        //Daily average bias rating runnable task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateDailyAvgBias(sql2o);
            }
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
//        scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);

        post("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String username = req.queryParams("username");
            String password = req.queryParams("password");

            try {
                Sql2oUserDao userDao = new Sql2oUserDao(sql2o);
                if (userDao.find(username) != null) {
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userDao.find(username).getUserPassword());
                    if (result.verified /*userDao.find(username).getUserPassword().equals(BCrypt.withDefaults().hashToString(12, password.toCharArray()))*/) {

                        //if (userDao.find(username).getUserPassword().equals(password)) {
                            model.put("existinguser", "true");
                            model.put("username", username);
                            res.cookie("username", username);
                            res.cookie("password", password); //set this only if success

                            int userID = (new Sql2oUserDao(sql2o).find(username)).getUserID();
                            List<Statistics> statsList = new Sql2oStatisticsDao(sql2o).find(userID);
                            Statistics stats = extractFromStatsList(statsList);

                            // Get 5 most recent user readings and retrieve articles by ID
                            List<UserReadings> readings = new Sql2oUserReadingsDao(sql2o).getAllUserReadings(userID);
                            List<Article> articles = new ArrayList<>();

                            if (readings != null) {
                                for (UserReadings read : readings) {
                                    List<Article> list = new Sql2oArticleDao(sql2o).find(read.getArticleID());
                                    Article article = extractFromArtsList(list);
                                    articles.add(article);
                                }
                            }

                            // Put articles in most recent first order
                            Collections.reverse(articles);

                            if (userID > 0) {
                                model.put("added", "true");
                                model.put("biasRating", stats.getBiasRating());
                                model.put("biasName", stats.getBiasName());
                                model.put("favNews", stats.getFavNewsSource());
                                model.put("favTopic", stats.getFavTopic());
                                model.put("execSummary", stats.getExecSummary());
                                model.put("Articles", articles);

                            } else {
                                model.put("failedFind", "true");
                            }
                    } else {
                        model.put("incorrectPassword", "true");
                    }
                }
                else {
                    model.put("noUserFound", "true");
                }
            } catch (DaoException e) {
                System.out.println("could not add new user stats");
                model.put("invalidLogin", "true");
            }

            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/index.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        // root route; check that a user is logged in
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            if(req.cookie("username") != null){

                String username = req.cookie("username");
                model.put("username", username);
                model.put("existinguser", true);
                int userID = (new Sql2oUserDao(sql2o).find(username)).getUserID();
                List<Statistics> statsList = new Sql2oStatisticsDao(sql2o).find(userID);
                Statistics stats = extractFromStatsList(statsList);
                // Get 5 most recent user readings and retrieve articles by ID
                List<UserReadings> readings = new Sql2oUserReadingsDao(sql2o).getAllUserReadings(userID);
                List<Article> articles = new ArrayList<>();

                if (readings != null) {
                    for (UserReadings read : readings) {
                        List<Article> list  = new Sql2oArticleDao(sql2o).find(read.getArticleID());
                        Article article = extractFromArtsList(list);
                        articles.add(article);
                    }
                }

                // Put articles in most recent first order
                Collections.reverse(articles);

                if (userID > 0) {
                    model.put("added", "true");
                    model.put("biasRating", stats.getBiasRating());
                    model.put("biasName", stats.getBiasName());
                    model.put("favNews", stats.getFavNewsSource());
                    model.put("favTopic", stats.getFavTopic());
                    model.put("execSummary", stats.getExecSummary());
                    model.put("Articles", articles);

                } else {
                    model.put("failedFind", "true");
                }

            }
            else{

                model.put("existinguser", false);

            }

            res.status(200);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/index.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

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

            try {
                Sql2oUserDao userDao = new Sql2oUserDao(sql2o);

                if (userDao.find(username) != null) {
                    model.put("userExists", "true");
                } else {
                    System.out.println("no user found, creating new one");
                    model.put("userExists", "false");
                    model.put("added", "true");
                    String bcryptHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                    User user = new User(username, bcryptHash);
                    int userID = new Sql2oUserDao(sql2o).add(user);

                    Statistics stats = new Statistics(0, "Neutral Bias",
                            "N/A", "N/A", "You have" +
                            " Neutral Bias. Your favorite news source is N/A." +
                            " Your favorite topic to read about is N/A", userID);
                    int idStats = new Sql2oStatisticsDao(sql2o).add(stats);

                    model.put("existinguser", "true");
                    model.put("username", username);
                    res.cookie("username", username);
                    //res.cookie("password", password); //set this only if success
                    res.redirect("/");
                }
            }
            catch (DaoException e) {
                model.put("failed", "true");
            }

            res.status(201);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/signup.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        // root route; check that a user is logged in
        get("/statsBIAS", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            if(req.cookie("username") != null){
                String username = req.cookie("username");
                model.put("username", username);
                model.put("existinguser", true);

                int userID = (new Sql2oUserDao(sql2o).find(username)).getUserID();
                List<Statistics> statsList = new Sql2oStatisticsDao(sql2o).find(userID);
                Statistics stats = extractFromStatsList(statsList);

                if (userID > 0) {
                    model.put("added", "true");
                    model.put("biasRating", stats.getBiasRating());
                    model.put("biasName", stats.getBiasName());

                    ArrayList<Integer> biasList = new Sql2oStatisticsDao(sql2o).listBIAS();
//                    model.put("minBias", biasList.get(0));
//                    model.put("maxBias", biasList.get(biasList.size() - 1));
//                    model.put("avgBias", new Sql2oStatisticsDao(sql2o).avgBIAS(biasList));
//                    model.put("dailyAvgBias", dailyAvgBias);
//                    model.put("dailyAvgDates", dailyAvgDates);

                    model.put("avgBias", new Sql2oStatisticsDao(sql2o).avgBIAS(biasList));
                    model.put("dailyAvgBias", userDailyAvgBias.get(userID));
                    model.put("dailyAvgDates", userDailyAvgDates.get(userID));

                } else {
                    model.put("failedFind", "true");
                }
            }
            else{
                model.put("existinguser", false);
                res.redirect("/");
            }

            res.status(200);
            res.type("text/html");
            ModelAndView mdl = new ModelAndView(model, "public/templates/statsBIAS.vm");
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
            String password = req.queryParams("password");
            User u = new User(userName, password);

            new Sql2oUserDao(sql2o).add(u);

            res.status(201);
            res.type("application/json");
            return new Gson().toJson(u.toString());
        });

        //deluser route; delete an article
        post("/deluser", (req, res) -> {
            int userID = Integer.parseInt(req.queryParams("userID"));
            new Sql2oUserDao(sql2o).delete(userID);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(userID);
        });

        //articles route; return list of articles as JSON
        get("/articles", (req, res) -> {
            Sql2oArticleDao article = new Sql2oArticleDao(sql2o);
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
            System.out.println("Article add started");
            if (req.cookie("username") != null) {
                model.put("username", req.cookie("username"));

                String username = req.cookie("username");
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
                    String newsSource = "";
                    Object obj = new JSONParser().parse(new FileReader("./src/main/resources/sources.json"));
                    JSONObject jo = (JSONObject) obj;
                    Iterator it = jo.keySet().iterator();
                    while(it.hasNext()){
                        String key = (String) it.next();
                        if(url.startsWith(key)){
                            newsSource = (String) jo.get(key);
                            break;
                        }
                    }

                    int biasRating = politicalBiasAPICall(articleExtract);
                    System.out.println("API call passed");
                    String topic = req.queryParams("topic").toUpperCase();
                    if (topic.equals("NA")) {
                        if(url.contains("politics")){
                            topic = "Politics";
                        }
                        else if(url.contains("business")){
                            topic = "Business";
                        }
                        else if(url.contains("opinion")){
                            topic = "Opinion";
                        }
                        else if(url.contains("sports")){
                            topic = "Sport";
                        }
                        else if(url.contains("health")){
                            topic = "Health";
                        }
                        else if(url.contains("entertainment")){
                            topic = "Entertainment";
                        }
                        else {
                            //If no matches to above categories within URL, use API to extract custom topic
                            topic = articleTopicExtractionAPICall(articleExtract);
                        }
                    } else {
                        topic = topic.substring(0, 1) + topic.substring(1).toLowerCase();
                    }
                    int numWords = countWords(articleExtract); //use countWords method to get numWords from Extracted text
                    long currentDate = (new Date().getTime())/ 1000;

                    Sql2oArticleDao articleDao = new Sql2oArticleDao(sql2o);
                    Article article = new Article(url, title, newsSource, biasRating, topic, numWords);
                    try {
                        articleDao.add(article);
                        System.out.println(article.toString());
                        System.out.println("new article added to DB");
                    } catch (DaoException de) {
                        List<Article> artList = articleDao.find(url);
                        article = extractFromArtsList(artList);
                    }

                    Sql2oUserReadingsDao userReadingsDao = new Sql2oUserReadingsDao(sql2o);
                    if (userReadingsDao.find(article.getArticleID(), u.getUserID()).size() == 0) {
                        UserReadings userReading = new UserReadings(article.getArticleID(), u.getUserID(), currentDate);
                        userReadingsDao.add(userReading);
                        System.out.println("new UReading added to DB");
                    } else {
                        List<UserReadings> urList = userReadingsDao.find(article.getArticleID(), u.getUserID());
                        UserReadings ur = extractFromURList(urList);
                        if (SameDay(currentDate, ur.getDateRead())) {
                            ur.setDateRead(currentDate);
                            userReadingsDao.update(ur);
                        } else {
                            UserReadings userReading = new UserReadings(article.getArticleID(), u.getUserID(), currentDate);
                            userReadingsDao.add(userReading);
                            System.out.println("new UReading added to DB");
                        }
                    }

                    List<Statistics> statsList = new Sql2oStatisticsDao(sql2o).find(u.getUserID());
                    Statistics stats = extractFromStatsList(statsList);
                    System.out.println("user stats found");
                    List<UserReadings> ur = new Sql2oUserReadingsDao(sql2o).getAllUserReadings(u.getUserID());
                    System.out.println("user readings retrieved");
                    List<Article> arts = new ArrayList<>();
                    for(UserReadings r : ur) {
                        System.out.println(r.getArticleID());
                        List<Article> artsList = articleDao.find(r.getArticleID());
                        Article art = extractFromArtsList(artsList);
                        System.out.println(art.toString());
                        arts.add(art);
                        System.out.println("article added");
                    }

                    new Sql2oStatisticsDao(sql2o).update(stats.getID(), arts);
                    System.out.println("stats updated");
                } catch (DaoException ex) {
                    System.out.print(ex);
                    model.put("failedFind", "true");
                }
            } else {
                System.out.println("NO USER!!!");
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
            new Sql2oArticleDao(sql2o).delete("url");
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(url);
        });

        //stats route; return all user stats
        get("/stats", (req, res) -> {
            String username = req.cookie("username");
            User u = new Sql2oUserDao(sql2o).find(username);
            Sql2oStatisticsDao sql2oStatsDao = new Sql2oStatisticsDao(sql2o);

            String results = new Gson().toJson(sql2oStatsDao.find(u.getUserID()));
            res.type("text/html");
            res.status(200);
            return results;
        });

        //favTopic route; displays fav Topic stats
        get("/favTopic", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Sql2oArticleDao sql2oArticleDao = new Sql2oArticleDao(sql2o);
            Sql2oUserReadingsDao sql2oUserReadingsDao = new Sql2oUserReadingsDao(sql2o);

            Map<String, Integer> data = new HashMap<>();

            String username = req.cookie("username");
            User user = new Sql2oUserDao(sql2o).find(username);
            List<UserReadings> readings = sql2oUserReadingsDao.getAllUserReadings(user.getUserID());
            for (UserReadings reading : readings) {
                Article temp = sql2oArticleDao.find(reading.getArticleID()).get(0);
                Integer freq = data.get(temp.getTopic());
                freq = (freq == null) ? 1 : ++freq;
                if (!temp.getTopic().equals("error")) {
                    data.put(temp.getTopic(), freq);
                }
            }

            model.put("data", data);
            model.put("type", "news");

            res.type("text/html");
            res.status(200);
            return new ModelAndView(model, "public/templates/statsTOPICS.vm");
        }, new VelocityTemplateEngine());

        //favNews route; displays fav News stats
        get("/favNews", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Sql2oArticleDao sql2oArticleDao = new Sql2oArticleDao(sql2o);
            Sql2oUserReadingsDao sql2oUserReadingsDao = new Sql2oUserReadingsDao(sql2o);

            Map<String, Integer> data = new HashMap<>();

            String username = req.cookie("username");
            User user = new Sql2oUserDao(sql2o).find(username);
            List<UserReadings> readings = sql2oUserReadingsDao.getAllUserReadings(user.getUserID());
            for (UserReadings reading : readings) {
                Article temp = sql2oArticleDao.find(reading.getArticleID()).get(0);
                Integer freq = data.get(temp.getNewsSource());
                freq = (freq == null) ? 1 : ++freq;
                data.put(temp.getNewsSource(), freq);
            }

            model.put("data", data);
            model.put("type", "news");

            res.type("text/html");
            res.status(200);
            return new ModelAndView(model, "public/templates/statsNEWS.vm");
        }, new VelocityTemplateEngine());

        //favNews route; displays fav News stats
        get("/genstats", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            if(req.cookie("username") != null) {
                Sql2oStatisticsDao sql2oStatsDao = new Sql2oStatisticsDao(sql2o);

                Map<String, Integer> news = new HashMap<>();
                List<User> users = new Sql2oUserDao(sql2o).listAll();
                for (User user : users) {
                    Integer freq = news.get(sql2oStatsDao.find(user.getUserID()).get(0).getFavNewsSource());
                    freq = (freq == null) ? 1 : ++freq;
                    if (!sql2oStatsDao.find(user.getUserID()).get(0).getFavNewsSource().equals("N/A")) {
                        news.put(sql2oStatsDao.find(user.getUserID()).get(0).getFavNewsSource(), freq);
                    }
                }
                model.put("news", news);

                Map<String, Integer> topics = new HashMap<>();
                for (User user : users) {
                    Integer freq = topics.get(sql2oStatsDao.find(user.getUserID()).get(0).getFavTopic());
                    freq = (freq == null) ? 1 : ++freq;
                    if (!sql2oStatsDao.find(user.getUserID()).get(0).getFavTopic().equals("N/A") && !sql2oStatsDao.find(user.getUserID()).get(0).getFavTopic().equals("error")) {
                        topics.put(sql2oStatsDao.find(user.getUserID()).get(0).getFavTopic(), freq);
                    }
                }
                model.put("topics", topics);
                model.put("dailyAvgBias", dailyAvgBias);
                model.put("dailyAvgDates", dailyAvgDates);
            } else {
                model.put("existinguser", false);
                res.redirect("/");
            }

            res.type("text/html");
            res.status(200);
            return new ModelAndView(model, "public/templates/genstats.vm");
        }, new VelocityTemplateEngine());

        //delstats route; delete a user's stats
        post("/delstats", (req, res) -> {
            int userID = Integer.parseInt(req.queryParams("userID"));
            new Sql2oStatisticsDao(sql2o).delete(userID);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(userID);
        });
    }



    //HELPER METHODS

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

    public static Statistics extractFromStatsList(List<Statistics> objList) {
        if (objList.size() > 1) {
            System.out.println("DUPLICATE Statistics");
        } else if (!objList.isEmpty()) {
            return objList.get(0);
        }

        return null;
    }

    public static Article extractFromArtsList(List<Article> objList) {
        if (objList.size() > 1) {
            System.out.println("DUPLICATE Articles");
        } else {
            return objList.get(0);
        }

        return null;
    }

    public static User extractFromUserList(List<User> objList) {
        if (objList.size() > 1) {
            System.out.println("DUPLICATE Users");
        } else {
            return objList.get(0);
        }

        return null;
    }

    public static UserReadings extractFromURList(List<UserReadings> objList) {
        if (objList.size() > 1) {
            System.out.println("DUPLICATE UserReadings");
        } else {
            return objList.get(0);
        }

        return null;
    }

    public static String articleTopicExtractionAPICall(String text) throws IOException {
        StringBuilder topic = new StringBuilder();
        OkHttpClient httpclient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("key", "aa112fd283b4a1c4dcde3040bb2a15bb")
                .add("lang", "en")
                .add("tt", "c")
                .add("txt", text)
                .build();
        Request postRequest = new Request.Builder()
                .url("https://api.meaningcloud.com/topics-2.0")
                .post(body)
                .build();
        try {
            Response response = httpclient.newCall(postRequest).execute();
            Gson g = new Gson();
            Object temp = g.fromJson(response.body().string(), Object.class);
            String json = temp.toString();
            int index = json.indexOf("form=") + 5;
            if (index == 4) {
               return "error";
            }
            while (json.charAt(index) != ',') {
                topic.append(Character.toString(json.charAt(index)));
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topic.toString();
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

    public static boolean SameDay(long currentDate, long dateRead) {
        long secondsInDay = 24*60*60;
        if (currentDate - dateRead > secondsInDay) {
            return false;
        }
        return true;
    }
}