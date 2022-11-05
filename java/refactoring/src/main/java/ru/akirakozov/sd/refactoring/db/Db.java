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

    public static Product selectFirstProductInSorted(String tableName, String orderBy) throws DbException {
        checkArguments(tableName, orderBy);
        return handleSelectQuery(
                "SELECT NAME, PRICE FROM " + tableName + " ORDER BY " + orderBy + " LIMIT 1",
                Db::fetchProduct
        );
    }

    public static Product selectLastProductInSorted(String tableName, String orderBy) throws DbException {
        checkArguments(tableName, orderBy);
        return handleSelectQuery(
                "SELECT * FROM " + tableName + " ORDER BY " + orderBy + " DESC LIMIT 1",
                Db::fetchProduct
        );
    }

    public static int sum(String tableName, String propertyToSum) throws DbException {
        checkArguments(tableName, propertyToSum);
        return handleSelectQuery(
                "SELECT SUM(" + propertyToSum + ") FROM " + tableName,
                Db::fetchFirstResultInt
        );
    }

    public static int count(String tableName) throws DbException {
        checkArguments(tableName);
        return handleSelectQuery(
                "SELECT COUNT(*) FROM " + tableName,
                Db::fetchFirstResultInt
        );
    }

    public static void add(String tableName, List<String> properties, List<String> values) throws DbException {
        if (properties.size() != values.size()) {
            throw new DbException("Properties and values have different sizes");
        }
        checkArguments(tableName);
        checkArguments(properties.toArray(new String[0]));
        checkArguments(values.toArray(new String[0]));
        handleUpdateQuery(
                "INSERT INTO " + tableName + " (" + String.join(",", properties) + ")"
                        + " VALUES (\"" + String.join("\",\"", values) + "\")"
        );
    }

    public static void clear(String tableName) throws DbException {
        checkArguments(tableName);
        handleUpdateQuery("DELETE FROM " + tableName);
    }

    public static List<Product> selectAllProducts(String tableName) throws DbException {
        checkArguments(tableName);
        return handleSelectQuery("SELECT NAME, PRICE FROM " + tableName, Db::fetchAllProducts);
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

    private static void handleUpdateQuery(String query) {
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

    private static void checkArguments(String... args) throws DbException {
        for (String arg : args) {
            if (arg.contains(" ")) {
                throw new DbException("Illegal characters in argument: " + arg);
            }
        }
    }
}
