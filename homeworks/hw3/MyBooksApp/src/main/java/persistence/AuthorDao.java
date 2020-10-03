package persistence;

import exception.DaoException;
import model.Author;

import java.util.List;

public interface AuthorDao {

    /**
     * Adds author to database
     * Returns authorId if successful
     * Throws DaoException if no connection can be established with database
     */
    int add(Author author) throws DaoException;

    /**
     * Lists all authors in database
     * Returns list of authors if successful
     * Throws DaoException if no connection can be established with database
     */
    List<Author> listAll();

    /**
     * Deletes authors from database and all corresponding books
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(Author author) throws DaoException;

    /**
     * Updates author in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(Author author) throws DaoException;
}