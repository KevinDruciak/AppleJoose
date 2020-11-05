package persistence;

import exception.DaoException;
import model.User;

import java.util.List;

public interface UserDao {
    /**
     * Adds user to database
     * Returns userID if successful
     * Throws DaoException if no connection can be established with database
     */
    int add(User user) throws DaoException;

    /**
     * Lists all users in database
     * Returns list of users if successful
     * Throws DaoException if no connection can be established with database
     */
    List<User> listAll();

    /**
     * Deletes users from database based on userID and userName
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(int userID) throws DaoException;

    /**
     * Updates user in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(User user) throws DaoException;

    /**
     * find user in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    int find(User user) throws DaoException;
}
