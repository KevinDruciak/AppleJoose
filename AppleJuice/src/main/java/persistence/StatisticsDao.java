package persistence;

import exception.DaoException;
import model.Statistics;
import java.util.List;

public interface StatisticsDao {
    /**
     * Adds statistics to database
     * Returns userID if successful
     * Throws DaoException if no connection can be established with database
     */
    int add(Statistics stats) throws DaoException;

    /**
     * Lists all statistics in database
     * Returns list of statistics if successful
     * Throws DaoException if no connection can be established with database
     */
    List<Statistics> listAll();

    /**
     * Deletes statistic from database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(int userID) throws DaoException;

    /**
     * Updates statistics in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(Statistics stats) throws DaoException;

    /**
     * finds statistics in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    Statistics find(int userID) throws DaoException;
}
