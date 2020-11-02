package persistence;

import exception.DaoException;
import model.Article;
import model.UserReadings;
import org.sql2o.Sql2o;

import java.util.List;
import exception.DaoException;
import model.UserReadings;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

public class Sql2oUserReadingsDao implements UserReadingsDao {
    private final Sql2o sql2o;

    public Sql2oUserReadingsDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(UserReadings userReading) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT INTO UserReadings (userID, articleID, dateRead, readingID) " +
                    "VALUES (:userID, :articleID, :dateRead, NULL)";
            int id = (int) con.createQuery(query, true)
                    .bind(userReading)
                    .executeUpdate().getKey();
            userReading.setReadingID(id);
            return userReading.getReadingID();
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public List<UserReadings> listAll() {
        String sql = "SELECT * FROM UserReadings";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(UserReadings.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public boolean delete(int readingID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM UserReadings WHERE readingID = :reading";
            con.createQuery(sql)
                    .addParameter("readingID", readingID)
                    .executeUpdate();
        } catch (Sql2oException e) {
            throw new DaoException();
        }
        return true;
    }

    @Override
    public boolean update(UserReadings userReadings) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "UPDATE UserReadings " +
                    "SET dateRead = :dateRead " +
                    "WHERE readingID = :readingID";
            con.createQuery(sql)
                    .addParameter("dateRead", userReadings.getDateRead())
                    .executeUpdate();
            return true;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public int find(UserReadings reading) throws DaoException {
        return 0;
    }

    //get a list of all the articles a specific user has read
    public List<UserReadings> getAllUserReadings(int userID) {
        String sql = "SELECT * FROM UserReadings WHERE userID = :userID";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("userID", userID)
                    .executeAndFetch(UserReadings.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }
}
