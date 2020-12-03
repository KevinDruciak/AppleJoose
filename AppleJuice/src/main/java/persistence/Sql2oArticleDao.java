package persistence;

import exception.DaoException;
import model.Article;
import model.Statistics;
import model.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class Sql2oArticleDao implements ArticleDao {
    private final Sql2o sql2o;

    public Sql2oArticleDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public String add(Article article) throws DaoException {
        try (Connection con = sql2o.open()) { //beginTransaction
            String query = "INSERT INTO Articles (url, title, newsSource, " +
                    "biasRating, topic, numWords)" +
                    "VALUES (:url, :title, :newsSource, :biasRating, " +
                    ":topic, :numWords)";
            int id = (int) con.createQuery(query, true)
                    .addParameter("url", article.getUrl())
                    .addParameter("title", article.getTitle())
                    .addParameter("newsSource", article.getNewsSource())
                    .addParameter("biasRating", (int) article.getBiasRating())
                    .addParameter("topic", article.getTopic())
                    .addParameter("numWords", (int) article.getNumWords())
                    .executeUpdate().getKey();
            article.setID(id);
            //con.commit();
            return article.getUrl();
        } catch (Sql2oException ex) {
            System.out.println(ex.toString());
            throw new DaoException();
        }
    }

    @Override
    public List<Article> listAll() throws DaoException {
        String sql = "SELECT * FROM Articles";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Article.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public boolean delete(String url) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM Articles WHERE url = :url";
            con.createQuery(sql)
                    .addParameter("url", url)
                    .executeUpdate();
        } catch (Sql2oException e) {
            throw new DaoException();
        }
        return true;
    }

    @Override
    public boolean update(Article article) throws DaoException {
        /*
        try (Connection con = sql2o.open()) {
            String sql = "UPDATE Articles " +
                    "SET timeOnArticle = :timeOnArticle, " +
                    "timesVisited = :timesVisited " +
                    "WHERE url = :url";
            con.createQuery(sql)
                    .addParameter("timeOnArticle", article.getTimeOnArticle())
                    .addParameter("timesVisited", article.getTimesVisited())
                    .addParameter("url", article.getUrl())
                    .executeUpdate();

            return true;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }*/
        return true;
    }

    @Override
    public List<Article> find(int articleID) throws DaoException {
        String sql = "SELECT * FROM Articles WHERE articleID = :articleID";
        System.out.println("query about to be made");
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("articleID", articleID)
                    .executeAndFetch(Article.class);
        }
        catch (Sql2oException ex) {
            System.out.println(ex.toString());
            throw new DaoException();
        }
    }
}
