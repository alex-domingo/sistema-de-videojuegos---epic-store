package com.epicstore.epicstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String IP = "localhost";
    private static final int PUERTO = 3306;
    private static final String SCHEMA = "tienda_videojuegos";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "7777";

    public static final String URL = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + SCHEMA
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guatemala";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver MySQL en el classpath", e);
        }
    }

    public void connect() {
        System.out.println("URL de conexion: " + URL);
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            System.out.println("Esquema: " + connection.getSchema());
            System.out.println("Catalogo: " + connection.getCatalog());
        } catch (SQLException e) {
            System.out.println("Error al conectarse");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
