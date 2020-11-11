package persistence;

import exception.DaoException;
import model.Article;
import model.Statistics;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql2oStatisticsDao implements StatisticsDao {
    private final Sql2o sql2o;

    public Sql2oStatisticsDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public int add(Statistics stats) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "INSERT OR IGNORE INTO Statistics (id, biasRating, biasName, " +
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
            System.out.print("add user stats throwing DAOEX");
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
    public boolean update(int id, List<Article> userHistory) throws DaoException {
        String sql = "SELECT * FROM Statistics WHERE id = :id";
        List<Statistics> statsList;
        try (Connection con = sql2o.open()) {
            statsList = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(Statistics.class);
            Statistics stat = statsList.get(0);

            double biasTotal = 0;
            double numArticles = 0;
            Map<String, Integer> newsSources = new HashMap<>();
            Map<String, Integer> topics = new HashMap<>();

            for (Article a : userHistory) {
                biasTotal += a.getBiasRating();
                numArticles++;
                Integer i = newsSources.get(a.getNewsSource());
                newsSources.put(a.getNewsSource(), i == null ? 1 : i + 1);
                Integer j = topics.get(a.getTopic());
                topics.put(a.getTopic(), j == null ? 1 : j + 1);
            }

            Map.Entry<String, Integer> favNews = null;
            Map.Entry<String, Integer> favTopic = null;

            for (Map.Entry<String, Integer> e : newsSources.entrySet()) {
                if (favNews == null || e.getValue() > favNews.getValue()) {
                    favNews = e;
                }
            }

            for (Map.Entry<String, Integer> h : topics.entrySet()) {
                if (favTopic == null || h.getValue() > favTopic.getValue()) {
                    favTopic = h;
                }
            }

            stat.setFavTopic(favTopic.getKey());
            stat.setFavNewsSource(favNews.getKey());
            stat.setBiasRating((int) Math.round(biasTotal / numArticles));
            stat.setBiasName(stat.createBiasName(stat.getBiasRating()));
            stat.setExecSummary(stat.createExecSummary());

            sql = "UPDATE Statistics SET biasRating = :biasRating, " +
                    "biasName = :biasName, favNewsSource = :favNewsSource, " +
                    "favTopic = :favTopic, execSummary = :execSummary " +
                    "WHERE id = :id";

            con.createQuery(sql)
                    .addParameter("biasRating", stat.getBiasRating())
                    .addParameter("biasName", stat.getBiasName())
                    .addParameter("favNewsSource", stat.getFavNewsSource())
                    .addParameter("favTopic", stat.getFavTopic())
                    .addParameter("execSummary", stat.getExecSummary())
                    .addParameter("id", id)
                    .executeUpdate();
        }
        catch (Sql2oException ex) {
            System.out.println(ex.toString());
            throw new DaoException();
        }

        return true;
    }

    @Override
    public List<Statistics> find(int userID) throws DaoException {
        String sql = "SELECT * FROM Statistics WHERE userID = :userID";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("userID", userID)
                    .executeAndFetch(Statistics.class);
        }
        catch (Sql2oException ex) {
            System.out.println("find stats throwing dao ex");
            throw new DaoException();
        }
    }

    //WHAT FOLLOWS ARE THE UPDATE FUNCTIONS FOR UPDATING STATISTICS ROW IN DATABASE

}
