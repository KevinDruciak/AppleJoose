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
        ds.setUrl("jdbc:sqlite:test.db");
        Sql2o sql2o = new Sql2o(ds);

        sqlAuthor = new Sql2oAuthorDao(sql2o);
        sqlBook = new Sql2oBookDao(sql2o);

        String URI = "jdbc:sqlite:./test.db";
        Connection conn = DriverManager.getConnection(URI);
        st = conn.createStatement();
    }

    @Before
    public void beforeEachTest() throws SQLException {
        String sql = "DROP TABLE IF EXISTS Authors";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Authors (id INTEGER PRIMARY KEY, name VARCHAR(100) NOT NULL UNIQUE," +
                " numOfBooks INTEGER, nationality VARCHAR(30));";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Books (id INTEGER PRIMARY KEY, title VARCHAR(200) NOT NULL," +
                " isbn VARCHAR(14) NOT NULL UNIQUE, publisher VARCHAR(14), year INTEGER," +
                " authorId INTEGER NOT NULL, FOREIGN KEY(authorId) REFERENCES Authors(id)" +
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
}
