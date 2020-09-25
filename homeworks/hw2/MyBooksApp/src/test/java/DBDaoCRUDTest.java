import model.Author;
import model.Book;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import org.sql2o.*;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import persistence.Sql2oAuthorDao;
import persistence.Sql2oBookDao;

public class DBDaoCRUDTest {

    private static Statement st;
    private static Sql2oAuthorDao sqlAuthor;
    private static Sql2oBookDao sqlBook;

    @BeforeClass
    public static void beforeClassTests() throws SQLException {

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        config.setPragma(SQLiteConfig.Pragma.FOREIGN_KEYS, "ON");

        // create data source
        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:MyBooksApp.db");
        Sql2o sql2o = new Sql2o(ds);

        sqlAuthor = new Sql2oAuthorDao(sql2o);
        sqlBook = new Sql2oBookDao(sql2o);

        String URI = "jdbc:sqlite:./MyBooksApp.db";
        Connection conn = DriverManager.getConnection(URI);
        st = conn.createStatement();
    }

    @Before
    public void beforeEachTest() throws SQLException {
        String sql = "DROP TABLE IF EXISTS Authors";
        st.execute(sql);
        sql = "DROP TABLE IF EXISTS Books";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Authors (id INTEGER PRIMARY KEY, name VARCHAR(100) NOT NULL UNIQUE," +
                " numOfBooks INTEGER, nationality VARCHAR(30));";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Books (id INTEGER PRIMARY KEY, title VARCHAR(200) NOT NULL," +
                " isbn VARCHAR(14) NOT NULL UNIQUE, publisher VARCHAR(14), year INTEGER," +
                " authorId INTEGER, FOREIGN KEY(authorId) REFERENCES Authors(id)" +
                " ON UPDATE CASCADE ON DELETE CASCADE);";
        st.execute(sql);
    }

    @Test
    public void testAuthorCREATE() {
        int testID;

        Author a = new Author ("John Smith", 10, "Earth");
        testID = sqlAuthor.add(a);
        System.out.println(testID);
        Assert.assertEquals(1, testID);

        Author b = new Author ("Bob", 2, "Arctic");
        testID = sqlAuthor.add(b);
        System.out.println(testID);
        Assert.assertEquals(2, testID);

        Author c = new Author ("Chen", 100, "Antarctic");
        testID = sqlAuthor.add(c);
        System.out.println(testID);
        Assert.assertEquals(3, testID);
    }

    @Test
    public void testAuthorREAD() {

        Author a = new Author ("John Smith", 10, "Earth");
        Author b = new Author ("Bob", 2, "Arctic");
        Author c = new Author ("Chen", 100, "Antarctic");

        sqlAuthor.add(a);
        sqlAuthor.add(b);
        sqlAuthor.add(c);

        List<Author> authorList = sqlAuthor.listAll();

        Assert.assertTrue(authorList.contains(a));
        Assert.assertTrue(authorList.contains(b));
        Assert.assertTrue(authorList.contains(c));

        Assert.assertEquals("[Author{id=1, name='John Smith', numOfBooks=10, nationality='Earth'}," +
                " Author{id=2, name='Bob', numOfBooks=2, nationality='Arctic'}," +
                " Author{id=3, name='Chen', numOfBooks=100, nationality='Antarctic'}]", authorList.toString());
        Assert.assertEquals(3, authorList.size());
    }

    @Test
    public void testAuthorUPDATE() {
        Author a = new Author ("John Smith", 10, "Earth");
        Author b = new Author ("Bob", 2, "Arctic");
        Author c = new Author ("Chen", 100, "Antarctic");

        sqlAuthor.add(a);
        sqlAuthor.add(b);
        sqlAuthor.add(c);

        Author aUPDATED = new Author("John Smith", 12, "French");
        boolean updateA = sqlAuthor.update(aUPDATED);
        Assert.assertTrue(updateA);

        Author bUPDATED = new Author("Bob", 7, "Russian");
        boolean updateB = sqlAuthor.update(bUPDATED);
        Assert.assertTrue(updateB);

        Author cUPDATED = new Author("Chen", 5186, "Pluto");
        boolean updateC = sqlAuthor.update(cUPDATED);
        Assert.assertTrue(updateC);

        List<Author> authorList = sqlAuthor.listAll();

        Assert.assertEquals("[Author{id=1, name='John Smith', numOfBooks=12, nationality='French'}," +
                " Author{id=2, name='Bob', numOfBooks=7, nationality='Russian'}," +
                " Author{id=3, name='Chen', numOfBooks=5186, nationality='Pluto'}]", authorList.toString());
        Assert.assertEquals(3, authorList.size());
    }

    @Test
    public void testAuthorDELETE() {
        Author a = new Author ("John Smith", 10, "Earth");
        Author b = new Author ("Bob", 2, "Arctic");
        Author c = new Author ("Chen", 100, "Antarctic");

        sqlAuthor.add(a);
        sqlAuthor.add(b);
        sqlAuthor.add(c);

        List<Author> authorList = sqlAuthor.listAll();

        Assert.assertTrue(authorList.contains(a));
        Assert.assertTrue(authorList.contains(b));
        Assert.assertTrue(authorList.contains(c));
        Assert.assertEquals(3, authorList.size());

        boolean deleteA = sqlAuthor.delete(a);
        Assert.assertTrue(deleteA);
        authorList = sqlAuthor.listAll();
        Assert.assertFalse(authorList.contains(a));
        Assert.assertTrue(authorList.contains(b));
        Assert.assertTrue(authorList.contains(c));
        Assert.assertEquals(2, authorList.size());

        boolean deleteB = sqlAuthor.delete(b);
        Assert.assertTrue(deleteB);
        authorList = sqlAuthor.listAll();
        Assert.assertFalse(authorList.contains(a));
        Assert.assertFalse(authorList.contains(b));
        Assert.assertTrue(authorList.contains(c));
        Assert.assertEquals(1, authorList.size());

        boolean deleteC = sqlAuthor.delete(c);
        Assert.assertTrue(deleteC);
        authorList = sqlAuthor.listAll();
        Assert.assertFalse(authorList.contains(a));
        Assert.assertFalse(authorList.contains(b));
        Assert.assertFalse(authorList.contains(c));
        Assert.assertEquals(0, authorList.size());
    }

    @Test
    public void testBookCREATE() {
        Author author = new Author ("John Smith", 10, "Earth");
        int aID = sqlAuthor.add(author);

        Book a = new Book("Title1", "ISBN1", "Publisher1", 2020, author.getId() );
        int authorIDa = sqlBook.add(a);

        Book b = new Book("Title2", "ISBN2", "Publisher2", 2021, author.getId() );
        int authorIDb = sqlBook.add(b);

        Assert.assertEquals(aID, authorIDa);
        Assert.assertEquals(aID, authorIDb);


        Author author2 = new Author ("John Smith2", 12, "Earth2");
        int bID = sqlAuthor.add(author2);

        Book c = new Book("Title2", "ISBN3", "Publisher2", 2021, author2.getId() );
        int authorIDc = sqlBook.add(c);

        Assert.assertEquals(bID, authorIDc);


        Assert.assertTrue(sqlBook.listAll().contains(a));
        Assert.assertTrue(sqlBook.listAll().contains(b));
        Assert.assertTrue(sqlBook.listAll().contains(c));
    }

    @Test
    public void testBookREAD() {

        Author auth = new Author ("John Smith", 10, "Earth");
        sqlAuthor.add(auth);

        Book a = new Book("Title1", "ISBN1", "Publisher1", 2020, auth.getId() );
        sqlBook.add(a);

        Book b = new Book("Title2", "ISBN2", "Publisher2", 2021, auth.getId() );
        sqlBook.add(b);

        Book c = new Book("Title3", "ISBN3", "Publisher3", 2022, auth.getId() );
        sqlBook.add(c);

        List<Book> bookList = sqlBook.listAll();

        Assert.assertTrue(bookList.contains(a));
        Assert.assertTrue(bookList.contains(b));
        Assert.assertTrue(bookList.contains(c));

        Assert.assertEquals("[Book{title='Title1', isbn='ISBN1', publisher='Publisher1', year=2020, author=1}," +
                " Book{title='Title2', isbn='ISBN2', publisher='Publisher2', year=2021, author=1}," +
                " Book{title='Title3', isbn='ISBN3', publisher='Publisher3', year=2022, author=1}]",
                bookList.toString());
        Assert.assertEquals(3, bookList.size());
    }
    @Test
    public void testBookUPDATE() {
        Author auth = new Author ("John Smith", 10, "Earth");
        sqlAuthor.add(auth);

        Book a = new Book("Title1", "ISBN1", "Publisher1", 2020, auth.getId() );
        int authorIDa = sqlBook.add(a);
        Book aUPDATED = new Book("Title2", "ISBN1", "Publisher2", 2021, auth.getId() );
        boolean updateA = sqlBook.update(aUPDATED);

        Assert.assertTrue(updateA);
        Assert.assertNotEquals(a, aUPDATED);

        List<Book> bookList = sqlBook.listAll();

        Assert.assertEquals("[Book{title='Title2', isbn='ISBN1', publisher='Publisher2', year=2021, author=1}]"
                , bookList.toString());
        Assert.assertEquals(1, bookList.size());
    }

    @Test
    public void testBookDELETE() {
        Author auth = new Author ("John Smith", 10, "Earth");
        sqlAuthor.add(auth);

        Book a = new Book("Title1", "ISBN1", "Publisher1", 2020, auth.getId() );
        Book b = new Book("Title2", "ISBN2", "Publisher1", 2020, auth.getId() );
        Book c = new Book("Title3", "ISBN3", "Publisher1", 2020, auth.getId() );

        sqlBook.add(a);
        sqlBook.add(b);
        sqlBook.add(c);

        List<Book> bookList = sqlBook.listAll();

        Assert.assertTrue(bookList.contains(a));
        Assert.assertTrue(bookList.contains(b));
        Assert.assertTrue(bookList.contains(c));
        Assert.assertEquals(3, bookList.size());

        boolean deleteA = sqlBook.delete(a);
        Assert.assertTrue(deleteA);
        bookList = sqlBook.listAll();
        Assert.assertFalse(bookList.contains(a));
        Assert.assertTrue(bookList.contains(b));
        Assert.assertTrue(bookList.contains(c));
        Assert.assertEquals(2, bookList.size());

        boolean deleteB = sqlBook.delete(b);
        Assert.assertTrue(deleteB);
        bookList = sqlBook.listAll();
        Assert.assertFalse(bookList.contains(a));
        Assert.assertFalse(bookList.contains(b));
        Assert.assertTrue(bookList.contains(c));
        Assert.assertEquals(1, bookList.size());

        boolean deleteC = sqlBook.delete(c);
        Assert.assertTrue(deleteC);
        bookList = sqlBook.listAll();
        Assert.assertFalse(bookList.contains(a));
        Assert.assertFalse(bookList.contains(b));
        Assert.assertFalse(bookList.contains(c));
        Assert.assertEquals(0, bookList.size());
    }


}
