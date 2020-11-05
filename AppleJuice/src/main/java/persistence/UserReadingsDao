package persistence;

import exception.DaoException;
import model.Article;
import model.Statistics;
import model.UserReadings;

import java.util.List;

public interface UserReadingsDao {
    /**
     * Adds instance of user reading to database
     * Returns userID if successful
     * Throws DaoException if no connection can be established with database
     */
    int add(UserReadings userReading) throws DaoException;

    /**
     * Lists all user readings in database
     * Returns list of user readings if successful
     * Throws DaoException if no connection can be established with database
     */
    List<UserReadings> listAll();

    /**
     * Deletes user readings instance from database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(int readingID) throws DaoException;

    /**
     * Updates user readings instance in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(UserReadings userReadings) throws DaoException;

    /**
     * Finds instance of user readings in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    UserReadings find(int readingID) throws DaoException;

    /**
     * Returns a list of all the articles that a particular user has read
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    List<UserReadings> getAllUserReadings(int userID) throws DaoException;

    /**
     * Returns a list of a specific number of the most recent articles
     *      that a particular user has read
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    List<UserReadings> getMostRecentUserReadings(int userID, int numArticles) throws DaoException;
}