package com.test.hoteleria.dbconexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class config {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hoteloasis?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&useUnicode=true&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "2348";

    private static Connection cn = null;  // La conexión será única (Singleton)

    // Constructor privado para evitar que se cree más de una instancia
    private config() {
    }

    // Método estático para obtener la conexión (lazy loading)
    public static Connection getCn() throws SQLException {
        if (cn == null || cn.isClosed()) {  // Si la conexión no existe o está cerrada
            synchronized (config.class) {  // Bloqueo para garantizar que solo un hilo la cree
                if (cn == null || cn.isClosed()) {
                    try {
                        System.out.println("Intentando conectar a la base de datos...");
                        // Cargar el controlador JDBC y establecer la conexión
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        cn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Error al cargar el controlador de MySQL", e);
                    }
                }
            }
        }
        return cn;  // Retorna la conexión
    }

    // Método para cerrar la conexión
    /*  public static void desconectar() {
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                cn = null;  // Se pone en null después de cerrarla
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } */
    // Método para cerrar la conexión y desregistrar el driver
    public static void desconectar() {
        try {
            if (cn != null && !cn.isClosed()) {
                // Desregistrar el driver
                DriverManager.deregisterDriver(DriverManager.getDriver(DB_URL));
                cn.close();
                cn = null;  // Se pone en null después de cerrarla
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método main para probar la conexión
    public static void main(String[] args) {
        try {
            // Intentar obtener la conexión
            Connection connection = config.getCn();
            if (connection != null) {
                System.out.println("Conexión exitosa.");
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al conectar a la base de datos: " + ex.getMessage());
        } finally {
            // Cerrar la conexión después de usarla
            config.desconectar();
        }
    }
}
