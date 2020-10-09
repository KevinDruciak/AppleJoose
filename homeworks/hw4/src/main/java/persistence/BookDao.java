package persistence;

import exception.DaoException;

import model.Book;
import model.Author;

import java.util.List;

public interface BookDao {
    int add(Book book, Author author) throws DaoException;
    List<Book> listAll() throws DaoException;
    boolean update(Book book) throws DaoException;
    boolean delete(Book book) throws DaoException;
}