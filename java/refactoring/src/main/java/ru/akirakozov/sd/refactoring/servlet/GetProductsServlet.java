package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Db;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.utils.HTMLUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = Db.selectAllProducts("SELECT * FROM PRODUCT");
        HTMLUtils.writeHTMLDocument(
                response.getWriter(),
                List.of(),
                products.stream()
                        .map(product -> product.toString() + "</br>")
                        .collect(Collectors.joining("\n"))
        );

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
