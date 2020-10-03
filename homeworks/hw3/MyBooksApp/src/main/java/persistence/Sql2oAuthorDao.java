package persistence;

import exception.DaoException;
import model.Author;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Sql2oAuthorDao implements AuthorDao {

    private final Sql2o sql2o;

    public Sql2oAuthorDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(Author author) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT INTO Authors (name, numOfBooks, nationality)" +
                    "VALUES (:name, :numOfBooks, :nationality)";
            int id = (int) con.createQuery(query, true)
                    .bind(author)
                    .executeUpdate().getKey();
            author.setId(id);

            return id;
        }
    }

    @Override
    public List<Author> listAll() throws DaoException {
        String sql = "SELECT * FROM Authors";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Author.class);
        }
    }

    @Override
    public boolean delete(String name) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM Authors WHERE name = :name";
            con.createQuery(sql)
                    .addParameter("name", name)
                    .executeUpdate();
        } catch (DaoException e) {
            throw new DaoException();
        }
        return true;
    }

    @Override
    public boolean update(Author author) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "UPDATE Authors " +
                    "SET numOfBooks = :numOfBooks, nationality = :nationality"+
                    " WHERE name = :name";
            con.createQuery(sql)
                    .addParameter("numOfBooks", author.getNumOfBooks())
                    .addParameter("nationality", author.getNationality())
                    .addParameter("name", author.getName())
                    .executeUpdate();

            return true;
        }
    }
}
