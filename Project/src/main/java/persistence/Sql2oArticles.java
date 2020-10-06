package persistence;

import exception.DaoException;
import model.Articles;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oArticles implements ArticlesDao{

    private final Sql2o sql2o;

    public Sql2oArticles(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(Articles article) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT INTO Articles (articleID, url, title, rating, timeSpentOnArticle, numWords, timesVisited, topic)" +
                    "VALUES (NULL, :url, :title, :rating, :timeSpentOnArticle, :numWords, :timesVisited, :topic)";
            int id = (int) con.createQuery(query, true)
                    .bind(article)
                    .executeUpdate().getKey();
            article.setID(id);
            return article.getID();
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public List<Articles> listAll() {
        String sql = "SELECT * FROM Articles";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Articles.class);
        }
    }

    @Override
    public boolean delete(int articleID) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "DELETE FROM Articles WHERE articleID = :articleID";
            con.createQuery(sql)
                    .addParameter("articleID", articleID)
                    .executeUpdate();

            return true;
        }
    }

    @Override
    public boolean update(Articles article) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "UPDATE Articles " +
                    "SET title = :title, rating = :rating, " +
                    "timeSpentOnArticle = :timeSpentOnArticle, numWords = :numWords," +
                    "timesVisited = :timesVisited, topic = :topic" +
                    "WHERE url = :url";
            con.createQuery(sql)
                    .addParameter("title", article.getTitle())
                    .addParameter("rating", article.getRating())
                    .addParameter("timeSpentOnArticle", article.getTimeSpentOnArticle())
                    .addParameter("numWords", article.getNumWords())
                    .addParameter("timesVisited", article.getTimesVisited())
                    .addParameter("topic", article.getTopic())
                    .executeUpdate();

            return true;
        }
    }
}
