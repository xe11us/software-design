package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Db;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.utils.HTMLUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        PrintWriter writer = response.getWriter();

        if ("max".equals(command)) {
            Product mostExpensiveProduct =
                    Db.mostExpensiveProduct("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

            HTMLUtils.writeHTMLDocument(writer, List.of("Product with max price: "),
                    mostExpensiveProduct.getName() + "\t" + mostExpensiveProduct.getPrice() + "</br>");
        } else if ("min".equals(command)) {
            Product cheapestProduct = Db.cheapestProduct("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            HTMLUtils.writeHTMLDocument(writer, List.of("Product with min price: "),
                    cheapestProduct.getName() + "\t" + cheapestProduct.getPrice() + "</br>");
        } else if ("sum".equals(command)) {
            int summaryPrice = Db.sum("SELECT SUM(price) FROM PRODUCT");

            HTMLUtils.writeHTMLDocument(writer, List.of(), "Summary price: \n" + summaryPrice);
        } else if ("count".equals(command)) {
            int numberOfProducts = Db.count("SELECT COUNT(*) FROM PRODUCT");

            HTMLUtils.writeHTMLDocument(writer, List.of(), "Number of products: \n" + numberOfProducts);
        } else {
            writer.println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
