package com.uade.carreracaballos.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static DBConnection instance;

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        Properties fileProps = new Properties();
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                fileProps.load(is);
            }
        } catch (IOException ignored) {}

        URL      = fileProps.getProperty("url",
                "jdbc:mysql://localhost:3306/carreracaballos?createDatabaseIfNotExist=true&serverTimezone=UTC");
        USER     = fileProps.getProperty("user", "root");
        PASSWORD = fileProps.getProperty("password", "");
    }

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado. Verificar mysql-connector-java en pom.xml", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
