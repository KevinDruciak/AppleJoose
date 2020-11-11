package persistence;

import exception.DaoException;
import model.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oUserDao implements UserDao {
    private final Sql2o sql2o;

    public Sql2oUserDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(User user) throws DaoException {
        /*try (Connection con = sql2o.open()) {
            String name = user.getUserName();
            String q = "SELECT userID FROM Users WHERE name = :name";
            int i = con.createQuery(q)
                    .addParameter("name", name)
                    .executeAndFetchFirst(Integer.class);
            if (i > 0) {
                return i;
            }
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
        }*/
        try (Connection con = sql2o.open()) {
            String query = "INSERT OR IGNORE INTO Users (userName)" +
                    "VALUES (:userName)";
            int id = (int) con.createQuery(query, true)
                    .bind(user)
                    .executeUpdate().getKey();
            user.setUserID(id);

            return id;
        }
    }

    //find existing user; return user's id if exists, else -1
    @Override
    public User find(String userName) throws DaoException {
        try (Connection con = sql2o.open()) {
            String q = "SELECT * FROM Users WHERE userName = :userName";
            return con.createQuery(q)
                    .addParameter("userName", userName)
                    .executeAndFetchFirst(User.class);
        } catch (Sql2oException | NullPointerException e) {
            throw new DaoException();
        }
    }

    @Override
    public List<User> listAll() throws DaoException {
        String sql = "SELECT * FROM Users";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(User.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public boolean delete(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM Users WHERE userID = :userID";
            con.createQuery(sql)
                    .addParameter("userID", userID)
                    .executeUpdate();
        } catch (DaoException e) {
            throw new DaoException();
        }
        return true;
    }

    @Override
    public boolean update(User user) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "UPDATE Users " +
                    "SET userName = :userName, userStats = :userStats,"+
                    " userHistory = :userHistory WHERE userID = :userID";
            con.createQuery(sql)
                    .addParameter("userName", user.getUserName())
                    .addParameter("userStats", user.getUserStats())
                    .addParameter("userHistory", user.getUserHistory())
                    .addParameter("userID", user.getUserID())
                    .executeUpdate();

        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }

        return true;
    }
}
