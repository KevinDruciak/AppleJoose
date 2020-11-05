package persistence;

import exception.DaoException;
import model.Article;
import model.UserReadings;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;
import exception.DaoException;

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
    public UserReadings find(int readingID) throws DaoException {
        String sql = "SELECT DISTINCT * FROM UserReadings WHERE readingID = :readingID";
        try (Connection con = sql2o.open()) {
            return (UserReadings) con.createQuery(sql)
                    .addParameter("readingID", readingID)
                    .executeAndFetch(UserReadings.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public List<UserReadings> getAllUserReadings(int userID) throws DaoException {
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

    @Override
    public List<UserReadings> getMostRecentUserReadings(int userID, int numArticles) throws DaoException {
        String sql = "SELECT * FROM UserReadings WHERE userID = :userID ORDER BY dateRead DESC";
        List<UserReadings> in;
        List<UserReadings> out = new ArrayList<>();
        try (Connection con = sql2o.open()) {
            in = con.createQuery(sql)
                    .addParameter("userID", userID)
                    .executeAndFetch(UserReadings.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }

        /*
        for(int i = 0; i < numArticles; i++) {
            out.set(i, in.get(i));
        }

         */
        return out;
    }
}
