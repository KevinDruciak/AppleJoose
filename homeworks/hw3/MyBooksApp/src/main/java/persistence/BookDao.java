package persistence;

import exception.DaoException;
import model.Book;

import java.util.List;

public interface BookDao {
    /**
     * Adds book to corresponding author already in database
     * Returns authorId if successful
     * Throws DaoException if no connection can be established with database
    */
    int add(Book book) throws DaoException;

    /**
     * Lists all books in database
     * Returns list of books if successful
     * Throws DaoException if no connection can be established with database
     */
    List<Book> listAll() throws DaoException;

    /**
     * Deletes book from database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean delete(Book book) throws DaoException;

    /**
     * Updates book in database
     * If sql query is successfully executed, true is returned
     * If something goes wrong with query execution, DaoException is thrown.
     */
    boolean update(Book book) throws DaoException;
}
