package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Db;
import ru.akirakozov.sd.refactoring.db.DbException;
import ru.akirakozov.sd.refactoring.utils.HTMLUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xellus
 */
public class ClearProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Db.clear("PRODUCT");
        } catch (DbException e) {
            System.err.println(e.getMessage());
        }

        HTMLUtils.writeHTMLDocument(
                response.getWriter(),
                List.of("Products list cleared"),
                ""
        );

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
