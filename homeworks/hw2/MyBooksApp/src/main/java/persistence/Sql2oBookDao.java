package persistence;

import exception.DaoException;
import model.Author;
import model.Book;
import java.util.List;
import org.sql2o.*;

public class Sql2oBookDao implements BookDao {

    private final Sql2o sql2o;

    public Sql2oBookDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(Book book) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT INTO Books (id, title, isbn, publisher, year, authorId)" +
                    "VALUES (NULL, :title, :isbn, :publisher, :year, :authorId)";
           int id = (int) con.createQuery(query, true)
                    .bind(book)
                    .executeUpdate().getKey();
           book.setId(id);
           return book.getAuthorId();
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public List<Book> listAll() {
        String sql = "SELECT * FROM Books";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Book.class);
        }
    }

    @Override
    public boolean delete(Book book) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "DELETE FROM Books WHERE isbn = :isbn";
            con.createQuery(sql)
                    .addParameter("isbn", book.getIsbn())
                    .executeUpdate();

            return true;
        }
    }

    @Override
    public boolean update(Book book) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "UPDATE Books " +
                    "SET title = :title, publisher = :publisher, year = :year," +
                    "authorId = :authorId " +
                    "WHERE isbn = :isbn";
            con.createQuery(sql)
                    .addParameter("title", book.getTitle())
                    .addParameter("publisher", book.getPublisher())
                    .addParameter("year", book.getYear())
                    .addParameter("authorId", book.getAuthorId())
                    .addParameter("isbn", book.getIsbn())
                    .executeUpdate();

            return true;
        }
    }
}
