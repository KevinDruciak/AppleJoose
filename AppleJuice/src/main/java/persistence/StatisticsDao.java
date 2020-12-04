package persistence;

import exception.DaoException;
import model.Statistics;
import model.Article;
import java.util.List;

public interface StatisticsDao {
    /**
     * Adds statistics to database.
     * @param stats the Statistics to add
     * @return userID if successful
     * @throws DaoException if no connection can be established with database
     */
    int add(Statistics stats) throws DaoException;

    /**
     * Lists all statistics in database.
     * @return list of statistics if successful
     * @throws DaoException if no connection can be established with database
     */
    List<Statistics> listAll() throws DaoException;

    /**
     * Deletes statistic from database.
     * @param userID the id of the user
     * @return If sql query is successfully executed, true is returned
     * @throws DaoException if no connection can be established with database
     */
    boolean delete(int userID) throws DaoException;

    /**
     * Updates statistics in database.
     * @param id the id of the User.
     * @param articles article history of User
     * @return If sql query is successfully executed, true is returned
     * @throws DaoException if no connection can be established with database
     */
    boolean update(int id, List<Article> articles) throws DaoException;

    /**
     * Finds statistics in database.
     * @param userID the id of the User whose Statistics are to be found
     * @return the list of Statistics matching userID
     * @throws DaoException if no connection can be established with database
     */
    List<Statistics> find(int userID) throws DaoException;
}
