import org.junit.*;
import ru.akirakozov.sd.refactoring.db.Db;
import ru.akirakozov.sd.refactoring.db.DbException;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductAppTest {
    private static Connection connection;
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static Writer writer = new StringWriter();

    @BeforeClass
    public static void beforeClass() {
        try {
            connection = DriverManager.getConnection(Db.getDbAddress());
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
            createProductsTable();
            when(response.getWriter()).thenReturn(new PrintWriter(writer));
            Db.clear("PRODUCT");
        } catch (SQLException | IOException | DbException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Before
    public void before() {
        try {
            writer = new StringWriter();
            when(response.getWriter()).thenReturn(new PrintWriter(writer));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void getFromEmptyTable() throws IOException {
        GetProductsServlet getServlet = new GetProductsServlet();

        getServlet.doGet(request, response);
        assertEquals(writer.toString(), "<html><body>\n</body></html>\n");
    }

    @Test
    public void addToTable() throws IOException {
        AddProductServlet addServlet = new AddProductServlet();
        when(request.getParameter("name")).thenReturn("iphone6");
        when(request.getParameter("price")).thenReturn("1000");
        addServlet.doGet(request, response);
        assertEquals(writer.toString(), "OK\n");
    }

    private static void createProductsTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterClass
    public static void afterClass() {
        try {
            Db.clear("PRODUCT");
            connection.close();
        } catch (SQLException | DbException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
