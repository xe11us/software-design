package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Db;
import ru.akirakozov.sd.refactoring.db.DbException;
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        PrintWriter writer = response.getWriter();

        try {
            switch (command) {
                case "max":
                    Product mostExpensiveProduct = Db.selectLastProductInSorted("PRODUCT", "PRICE");
                    HTMLUtils.writeHTMLDocument(
                            writer,
                            List.of("Product with max price: "),
                            mostExpensiveProduct.toString() + "</br>"
                    );
                    break;
                case "min":
                    Product cheapestProduct = Db.selectFirstProductInSorted("PRODUCT", "PRICE");
                    HTMLUtils.writeHTMLDocument(
                            writer,
                            List.of("Product with min price: "),
                            cheapestProduct.toString() + "</br>"
                    );
                    break;
                case "sum":
                    int summaryPrice = Db.sum("PRODUCT", "PRICE");
                    HTMLUtils.writeHTMLDocument(writer, List.of(), "Summary price: \n" + summaryPrice);
                    break;
                case "count":
                    int numberOfProducts = Db.count("PRODUCT");
                    HTMLUtils.writeHTMLDocument(writer, List.of(), "Number of products: \n" + numberOfProducts);
                    break;
                default:
                    writer.println("Unknown command: " + command);
            }

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (DbException e) {
            // Поддерживаем инвариант, что всё приложение не должно падать из-за неверного запроса
            System.err.println(e.getMessage());
        }
    }
}
