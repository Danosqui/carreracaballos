package com.uade.carreracaballos.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
        initSchema();
    }

    private void initSchema() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement()) {
            st.execute(
                "CREATE TABLE IF NOT EXISTS caballos (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  nombre VARCHAR(100) NOT NULL," +
                "  tipo VARCHAR(20) NOT NULL," +
                "  velocidad DOUBLE," +
                "  resistencia DOUBLE," +
                "  energia DOUBLE," +
                "  distanciaRecorrida DOUBLE" +
                ")"
            );
            st.execute(
                "CREATE TABLE IF NOT EXISTS jugadores (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  nombre VARCHAR(100) NOT NULL," +
                "  mail VARCHAR(100)," +
                "  puntaje INT" +
                ")"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar el esquema de la base de datos", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
