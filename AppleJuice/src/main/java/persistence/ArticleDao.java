package persistence;

import exception.DaoException;
import model.Article;
import java.util.List;

public interface ArticleDao {
    /**
     * Adds article to database.
     * @param article the Article to add
     * @return url of article if successful
     * @throws DaoException if no connection can be established with database
     */
    String add(Article article) throws DaoException;

    /**
     * Lists all articles in database.
     * @return list of articles if successful
     * @throws DaoException if no connection can be established with database
     */
    List<Article> listAll();

    /**
     * Deletes article from database.
     * @param url the url of the Article to delete
     * @return If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(String url) throws DaoException;

    /**
     * Updates article in database.
     * @param article the Article object to be updated
     * @return If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(Article article) throws DaoException;

    /**
     * Finds article in database.
     * @param articleID the id of the Article to find
     * @return the list of Articles matching articleID
     */
    List<Article> find(int articleID) throws DaoException;
}
