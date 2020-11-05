package persistence;

import exception.DaoException;
import model.Article;
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
        try (Connection con = sql2o.open()) {
            String query = "INSERT IGNORE INTO Articles (id, url, title, newsSource, " +
                    "biasRating, topic, timeOnArticle, numWords, timesVisited)" +
                    "VALUES (NULL, :url, :title, :newsSource, :biasRating, " +
                    ":topic, :timeOnArticle, :numWords, :timesVisited)";
            int id = (int) con.createQuery(query, true)
                    .bind(article)
                    .executeUpdate().getKey();
            article.setID(id);
            return article.getUrl();
        } catch (Sql2oException ex) {
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
        }
    }

    @Override
    public Article find(int id) throws DaoException {
        String sql = "SELECT * FROM Articles WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return (Article) con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(Article.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }
}
