package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Db {
    private static final String dbAddress = "jdbc:sqlite:test.db";

    public static String getDbAddress() {
        return dbAddress;
    }

    public static Product mostExpensiveProduct(String query) {
        return handleSelectQuery(query, Db::fetchProduct);
    }

    public static Product cheapestProduct(String query) {
        return handleSelectQuery(query, Db::fetchProduct);
    }

    public static int sum(String query) {
        return handleSelectQuery(query, Db::fetchFirstResultInt);
    }

    public static int count(String query) {
        return handleSelectQuery(query, Db::fetchFirstResultInt);
    }

    public static void addProduct(String query) {
        handleInsertQuery(query);
    }

    public static List<Product> selectAllProducts(String query) {
        return handleSelectQuery(query, Db::fetchAllProducts);
    }

    private static Product fetchProduct(ResultSet rs) {
        try {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            return new Product(name, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Product> fetchAllProducts(ResultSet rs) {
        List<Product> products = new ArrayList<>();
        try {
            while (rs.next()) {
                products.add(fetchProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    private static int fetchFirstResultInt(ResultSet rs) {
        try {
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static <V> V handleSelectQuery(String query, Function<ResultSet, V> handler) {
        V result;
        try (Connection c = DriverManager.getConnection(dbAddress)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            result = handler.apply(rs);
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static void handleInsertQuery(String query) {
        try {
            try (Connection c = DriverManager.getConnection(dbAddress)) {
                Statement stmt = c.createStatement();
                stmt.executeUpdate(query);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
