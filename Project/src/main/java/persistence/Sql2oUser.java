package persistence;

import exception.DaoException;
import model.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Sql2oUser implements UserDao {

    private final Sql2o sql2o;
    public Sql2oUser(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(User user) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT INTO Users (id, userName, stats, history)" +
                    "VALUES (:id, :userName, :stats, :history)";
            int id = (int) con.createQuery(query, true)
                    .bind(user)
                    .executeUpdate().getKey();
            user.setId(id);
            return id;
        }
    }

    @Override
    public List<User> listAll() {
        String sql = "SELECT * FROM Users";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(User.class);
        }
    }

    @Override
    public boolean delete(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM Users WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", userID)
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
                    "SET userName = :userName, stats = :stats, history = :history"+
                    " WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("userName", user.getUserName())
                    .addParameter("stats", user.getStats())
                    .addParameter("history", user.getHistory())
                    .executeUpdate();
            return true;
        }
    }
}