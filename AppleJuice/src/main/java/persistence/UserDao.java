package persistence;

import exception.DaoException;
import model.User;

import java.util.List;

public interface UserDao {
    /**
     * Adds user to database.
     * @param user the User to add
     * @return userID if successful
     * @throws DaoException if no connection can be established with database
     */
    int add(User user) throws DaoException;

    /**
     * Lists all users in database.
     * @return list of users if successful
     * @throws DaoException if no connection can be established with database
     */
    List<User> listAll();

    /**
     * Deletes users from database based on userID and userName.
     * @param userID the userID of the user to delete
     * @return If sql query is successfully executed, true is returned
     * @throws DaoException if no connection can be established with database
     */
    boolean delete(int userID) throws DaoException;

    /**
     * Updates user in database.
     * @param user the User to update
     * @return If sql query is successfully executed, true is returned
     * @throws DaoException if no connection can be established with database
     */
    boolean update(User user) throws DaoException;

    /**
     * Find user in database.
     * @param username the username of the User to find
     * @return return found User
     * @throws DaoException if no connection can be established with database
     */
    User find(String username) throws DaoException;
}
