package persistence;

import exception.DaoException;
import model.User;

import java.util.List;

public interface UserDao {

    int add(User user) throws DaoException;

    List<User> listAll();

    boolean delete(int userID) throws DaoException;

    boolean update(User user) throws DaoException;

}
