package persistence;

import exception.DaoException;
import model.Statistics;
import model.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oStatisticsDao implements StatisticsDao {
    private final Sql2o sql2o;

    public Sql2oStatisticsDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public int add(Statistics stats) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT INTO Statistics (id, biasRating, biasName, " +
                    "favNewsSource, favTopic, execSummary, userID)" +
                    "VALUES (NULL, :biasRating, :biasName, :favNewsSource," +
                    " :favTopic, :execSummary, :userID)";
            int id = (int) con.createQuery(query, true)
                    .bind(stats)
                    .executeUpdate().getKey();
            stats.setID(id);
            return id;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public List<Statistics> listAll() throws DaoException {
        String sql = "SELECT * FROM Statistics";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Statistics.class);
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }

    @Override
    public boolean delete(int userID) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "DELETE FROM Statistics WHERE userID = :userID";
            con.createQuery(sql)
                    .addParameter("userID", userID)
                    .executeUpdate();

            return true;
        }
        catch (Sql2oException ex){
            throw new DaoException();
        }
    }

    @Override
    public boolean update(Statistics stats) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "UPDATE Statistics " +
                    "SET biasRating = :biasRating, biasName = :biasName, " +
                    "favNewsSource = :favNewsSource, favTopic = :favTopic, " +
                    "execSummary = :execSummary " +
                    "WHERE userID = :userID";
            con.createQuery(sql)
                    .addParameter("biasRating", stats.getBiasRating())
                    .addParameter("biasName", stats.getBiasName())
                    .addParameter("favNewsSource", stats.getFavNewsSource())
                    .addParameter("favTopic", stats.getFavTopic())
                    //.addParameter("recentArticles", stats.getRecentArticles())
                    .addParameter("execSummary", stats.getExecSummary())
                    .addParameter("userID", stats.getUserID())
                    .executeUpdate();

            return true;
        }
        catch (Sql2oException ex) {
            throw new DaoException();
        }
    }


    //get a user's bias rating
    public int getBias(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String q = "SELECT biasRating FROM Statistics WHERE userID = :userID";
            return con.createQuery(q).addParameter("userID", userID).executeAndFetchFirst(Integer.class);
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
            System.out.println("TEST IF EXCEPTION");
        }
        return -404; //temp return value
    }

    //get a user's bias name
    public String getBiasName(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String q = "SELECT biasNAME FROM Statistics WHERE userID = :userID";
            return con.createQuery(q).addParameter("userID", userID).executeAndFetchFirst(String.class);
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
            System.out.println("TEST IF EXCEPTION");
        }
        return "N/A"; //temp return value
    }

    //get a user's favorite news source
    public String getFavNews(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String q = "SELECT favNewsSource FROM Statistics WHERE userID = :userID";
            return con.createQuery(q).addParameter("userID", userID).executeAndFetchFirst(String.class);
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
            System.out.println("TEST IF EXCEPTION");
        }
        return "N/A"; //temp return value
    }

    //get a user's favorite topic
    public String getFavTopic(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String q = "SELECT favTopic FROM Statistics WHERE userID = :userID";
            return con.createQuery(q).addParameter("userID", userID).executeAndFetchFirst(String.class);
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
            System.out.println("TEST IF EXCEPTION");
        }
        return "N/A"; //temp return value
    }

    //get a user's summary
    public String getExecSummary(int userID) throws DaoException {
        try (Connection con = sql2o.open()) {
            String q = "SELECT execSummary FROM Statistics WHERE userID = :userID";
            return con.createQuery(q).addParameter("userID", userID).executeAndFetchFirst(String.class);
        } catch (Sql2oException | NullPointerException e) {
            //do nothing
            System.out.println("TEST IF EXCEPTION");
        }
        return "N/A"; //temp return value
    }
}
