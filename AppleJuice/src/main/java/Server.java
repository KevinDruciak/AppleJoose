import at.favre.lib.crypto.bcrypt.BCrypt;
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

    public static Statistics extractFromStatsList(List<Statistics> objList) {
        if (objList.size() > 1) {
            System.out.println("DUPLICATE Statistics");
        } else {
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
                if (userDao.findID(user) > 0) {
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userDao.getPassword(userDao.find(user)));
                    //if (userDao.getPassword(userDao.find(user)).equals(BCrypt.withDefaults().hashToString(12, password.toCharArray()))) {
                    if (result.verified) {
                        model.put("existinguser", "true");
                        model.put("username", username);
                        res.cookie("username", username); //set this only if success

                        User temp = new User(username, null); //changed constructor
                        int userID = new Sql2oUserDao(sql2o).find(temp);
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
//            return new ModelAndView(model, "public/templates/index.vm");
//        }, new VelocityTemplateEngine());
            ModelAndView mdl = new ModelAndView(model, "public/templates/index.vm");
            return new VelocityTemplateEngine().render(mdl);
        });

        // root route; check that a user is logged in
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
//            if (req.cookie("username") != null) {
//                model.put("username", req.cookie("username"));
//                //model.put("password", req.cookie("password"));
//
//                String username = req.cookie("username");
//                User temp = new User(username, null); //changed constructor
//                try {
//                    int userID = new Sql2oUserDao(sql2o).find(temp);
//                    System.out.println(userID + "find");
//                    if (userID > 0) {
//                        model.put("added", "true");
//                        model.put("biasRating", new Sql2oStatisticsDao(sql2o).getBias(userID));
//                        model.put("biasName", new Sql2oStatisticsDao(sql2o).getBiasName(userID));
//                        model.put("favNews", new Sql2oStatisticsDao(sql2o).getFavNews(userID));
//                        model.put("favTopic", new Sql2oStatisticsDao(sql2o).getFavTopic(userID));
//                        model.put("execSummary", new Sql2oStatisticsDao(sql2o).getExecSummary(userID));
//                    }
//                    else {
//                        model.put("failedFind", "true");
//                    }
//                }
//                catch (DaoException ex) {
//                    model.put("failedFind", "true");
//                    System.out.println("FAILED TRY /");
//                }
//            }
            res.status(200);
            res.type("text/html");
//            return new ModelAndView(model, "public/templates/index.vm");
//        }, new VelocityTemplateEngine());
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

            User user2 = new User(username, null); //changed constrcutor
            try {
                Sql2oUserDao userDao = new Sql2oUserDao(sql2o);
                if (userDao.findID(user2) > 0) {
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

//             User u = new User(userName, null); //changed construftor
//             new Sql2oUserDao(getSql2o()).add(u);
            User u = new User(userName);
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
                    String newsSource = new URL(url).getHost();
                    newsSource = newsSource.replace("www.", "");
                    newsSource = newsSource.replace(".com", "");
                    if (newsSource == null) {
                        newsSource = "News Source could not be identified";
                    }

                    int biasRating = politicalBiasAPICall(articleExtract);
                    System.out.println("API call passed");
                    String topic = req.queryParams("topic");
                    int numWords = countWords(articleExtract); //use countWords method to get numWords from Extracted text
                    long currentDate = (new Date().getTime())/ 1000;


                    Sql2oArticleDao articleDao = new Sql2oArticleDao(sql2o);
                    Article article = new Article(url, title, newsSource, biasRating, topic, numWords);
                    articleDao.add(article);
                    System.out.println(article.toString());
                    System.out.println("new article added to DB");
                    UserReadings userReading = new UserReadings(u.getUserID(), article.getArticleID(), currentDate);
                    new Sql2oUserReadingsDao(sql2o).add(userReading);
                    System.out.println("new UReading added to DB");
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
                    System.out.print("catch statement");
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
//            new Sql2oStatisticsDao(sql2o).add(stats);
//            res.status(201);
//            res.type("application/json");
//            return new Gson().toJson(stats.toString());
//        });

        //delstats route; delete a user's stats
        post("/delstats", (req, res) -> {
            int userID = Integer.parseInt(req.queryParams("userID"));
            new Sql2oStatisticsDao(sql2o).delete(userID);
            res.status(201);
            res.type("application/json");
            return new Gson().toJson(userID);
        });
    }
}
