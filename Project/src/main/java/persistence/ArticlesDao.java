package persistence;

import exception.DaoException;
import model.Articles;

import java.util.List;

public interface ArticlesDao {

    int add(Articles article) throws DaoException;

    List<Articles> listAll();

    boolean delete(int articleID) throws DaoException;

    boolean update(Articles article) throws DaoException;

}
