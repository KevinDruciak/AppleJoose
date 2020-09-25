package persistence;

import exception.DaoException;
import model.Author;
import model.Book;
import java.util.List;
import org.sql2o.*;

public class Sql2oBookDao implements BookDao {

    private final Sql2o sql2o;

    public Sql2oBookDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int add(Book book) throws DaoException {
        try (Connection con = sql2o.open()) {
            String query = "SELECT DISTINCT name FROM " +
                    "Authors WHERE name = :authorName";
            List<Author> temp = con.createQuery(query)
                    .addParameter("authorName", book.getAuthor().getName())
                    .executeAndFetch(Author.class);
            Author author = temp.get(0);

            String temp1 = author.getName();
            String temp2 = book.getAuthor().getName();

            int authorId;
            if (!temp1.equals(temp2)) {
                System.out.println("unequal author names test");
                return -1;
            } else {
                authorId = author.getId();
                book.setAuthorId(authorId);
            }

            String sql = "INSERT INTO Books (title, isbn, publisher, year, " +
                    "author, authorID) VALUES (:title, :isbn, :publisher, :year, " +
                    ":author, :authorID)";
            Book bookEntry = (Book) con.createQuery(sql)
                    .bind(book)
                    .executeAndFetch(Book.class);

            return authorId;
        }
    }

    @Override
    public List<Book> listAll() {
        String sql = "SELECT * FROM Books";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Book.class);
        }
    }

    @Override
    public boolean delete(Book book) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "DELETE FROM Books WHERE isbn = :isbn";
            con.createQuery(sql)
                    .addParameter("isbn", book.getIsbn())
                    .executeUpdate();

            return true;
        }
    }

    @Override
    public boolean update(Book book) throws DaoException {
        try (Connection con = sql2o.open()){
            String sql = "UPDATE Books " +
                    "SET title = :title, publisher = :publisher, year = :year," +
                    " author = :author, authorId = :authorId " +
                    "WHERE isbn = :isbn";
            con.createQuery(sql)
                    .addParameter("title", book.getTitle())
                    .addParameter("publisher", book.getPublisher())
                    .addParameter("year", book.getYear())
                    .addParameter("author", book.getAuthor())
                    .addParameter("authorId", book.getAuthorId())
                    .addParameter("isbn", book.getIsbn())
                    .executeUpdate();

            return true;
        }
    }
}
