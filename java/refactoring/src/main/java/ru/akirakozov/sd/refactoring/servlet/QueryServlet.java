package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Db;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.*;

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

            writer.println("<html><body>");
            writer.println("<h1>Product with max price: </h1>");
            writer.println(mostExpensiveProduct.getName() + "\t" + mostExpensiveProduct.getPrice() + "</br>");
            writer.println("</body></html>");
        } else if ("min".equals(command)) {
            Product cheapestProduct = Db.cheapestProduct("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            writer.println("<html><body>");
            writer.println("<h1>Product with min price: </h1>");
            writer.println(cheapestProduct.getName() + "\t" + cheapestProduct.getPrice() + "</br>");
            writer.println("</body></html>");
        } else if ("sum".equals(command)) {
            int summaryPrice = Db.sum("SELECT SUM(price) FROM PRODUCT");

            writer.println("<html><body>");
            writer.println("Summary price: ");
            writer.println(summaryPrice);
            writer.println("</body></html>");
        } else if ("count".equals(command)) {
            int numberOfProducts = Db.count("SELECT COUNT(*) FROM PRODUCT");

            writer.println("<html><body>");
            writer.println("Number of products: ");
            writer.println(numberOfProducts);
            writer.println("</body></html>");
        } else {
            writer.println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
