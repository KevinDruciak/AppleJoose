package persistence;

import exception.DaoException;
import model.Article;
import java.util.List;

public interface ArticleDao {
    /**
     * Adds article to database
     * Returns url of article if successful
     * Throws DaoException if no connection can be established with database
     */
    String add(Article article) throws DaoException;

    /**
     * Lists all articles in database
     * Returns list of articles if successful
     * Throws DaoException if no connection can be established with database
     */
    List<Article> listAll();

    /**
     * Deletes article from database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(String url) throws DaoException;

    /**
     * Updates article in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(Article article) throws DaoException;
}
