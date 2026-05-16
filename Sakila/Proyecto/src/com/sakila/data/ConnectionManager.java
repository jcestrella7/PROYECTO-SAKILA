package com.sakila.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión a la base de datos usando un archivo db.properties.
 * Esta clase proporciona una conexión estática y única para toda la aplicación.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class ConnectionManager {

    private static Connection connection = null;
    private static Properties properties = new Properties();

    // Bloque estático para cargar las propiedades de la base de datos una sola vez
    // y establecer la conexión inicial.
    static {
        try {
            String rutaDbProperties = "C:\\Users\\aadam\\Downloads\\Sakila\\db.properties";

            java.io.File archivo = new java.io.File(rutaDbProperties);
            if (!archivo.exists()) {
                throw new RuntimeException("CREA: " + rutaDbProperties);
            }

            properties.load(new java.io.FileInputStream(rutaDbProperties));

            // ✅ SIN Class.forName - MySQL 9.7.0 autocarga
            System.out.println("✅ MySQL 9.7.0 CONFIGURADO");

        } catch (Exception e) {
            throw new RuntimeException("Error db.properties", e);
        }
    }
    private ConnectionManager() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Obtiene la conexión activa a la base de datos. Si la conexión es nula o está cerrada,
     * intenta establecer una nueva conexión.
     * @return Una instancia de Connection.
     * @throws SQLException Si ocurre un error al establecer la conexión.
     */
    public static Connection getConnection() throws SQLException {
        // Solo establecer la conexión si es nula o está cerrada
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password")
            );
        }
        return connection;
    }

    /**
     * Cierra la conexión estática a la base de datos si está abierta.
     * NOTA: Este método cierra la única conexión global gestionada por ConnectionManager.
     */
    public static void closeConnection() { // <-- SIN ARGUMENTOS
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Marcar la conexión como cerrada para que se pueda reabrir
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
}