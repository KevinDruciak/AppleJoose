package persistence;

import exception.DaoException;
import model.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.sql2o.*;

public class Sql2oAuthorDao implements AuthorDao {

    private final Sql2o sql2o;

    public Sql2oAuthorDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }


    @Override
    public int add(Author author) throws DaoException {
        try (Connection con = sql2o.open()) {
            String name = author.getName();
            String q = "SELECT id FROM Authors WHERE name = :name";
            //System.out.println("ONE1");
            int test = con.createQuery(q).addParameter("name", name).executeAndFetchFirst(Integer.class);
            //System.out.println(test);
            //System.out.println("TWO2");
            int i = test;
            if (i > 0) {
                return i;
            }
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
            //System.out.println("EXCEPTION HERE");
        }
        try (Connection con = sql2o.open()) {
            //System.out.println("REACHES 1");
            String query = "INSERT INTO Authors (name, numOfBooks, nationality)" +
                    "VALUES (:name, :numOfBooks, :nationality)";
            //System.out.println("REACHES 2");
            int id = (int) con.createQuery(query, true)
                    .bind(author)
                    .executeUpdate().getKey();
            //System.out.println("REACHES 3");
            author.setId(id);
            return id;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public List<Author> listAll() throws DaoException {
        String sql = "SELECT * FROM Authors";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Author.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public boolean update(Author author) throws DaoException {
        String sql = "Update Authors SET name = :name, numOfBooks = :numOfBooks, " +
                     "nationality = :nationality WHERE name = :name";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(author)
                    .executeUpdate();
            return true;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }
    @Override
    public boolean delete(Author author) throws DaoException {
        String sql = "DELETE FROM Authors WHERE name =:name";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(author)
                    .executeUpdate();
            return true;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

}
