package persistence;

import exception.DaoException;
import model.Statistics;

import java.util.List;

public interface StatisticsDao {

    int add(Statistics statistics) throws DaoException;

    List<Statistics> listAll();

    boolean delete(int articleID) throws DaoException;

    boolean update(Statistics statistics) throws DaoException;
}
