package com.sakila.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta híbrida que gestiona la conexión y operaciones
 * base de datos. Los métodos son final para que los hijos no puedan
 * sobreescribirlos (inmutables en comportamiento).
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 * @since 2021
 */
public abstract class DataContext<T> implements IDatapost<T> {

    // ── Configuración de conexión ──────────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/sakila";
    private static final String USER     = "root";
    private static final String PASSWORD = "root";

    protected Connection connection;

    // ── Constructor ───────────────────────────────────────────────────

    /**
     * Constructor: abre la conexión con la base de datos sakila.
     */
    public DataContext() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DataContext] Conexión establecida con sakila.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DataContext] Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[DataContext] Error de conexión: " + e.getMessage());
        }
    }

    // ── Métodos FINALES (no override permitido) ───────────────────────

    /**
     * Ejecuta un INSERT, UPDATE o DELETE genérico.
     * @param sql    Sentencia SQL con parámetros (?)
     * @param params Valores de los parámetros
     * @return true si afectó al menos 1 fila
     */
    protected final boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DataContext] executeUpdate error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ejecuta un SELECT y retorna el ResultSet.
     * @param sql    Sentencia SQL con parámetros (?)
     * @param params Valores de los parámetros
     * @return ResultSet con los resultados
     */
    protected final ResultSet executeQuery(String sql, Object... params) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            setParameters(ps, params);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("[DataContext] executeQuery error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Ejecuta INSERT y retorna la clave generada (autoincrement).
     * @param sql    Sentencia INSERT con parámetros (?)
     * @param params Valores de los parámetros
     * @return ID generado, -1 si falla
     */
    protected final int executeInsertGetKey(String sql, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(ps, params);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[DataContext] executeInsertGetKey error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Cierra la conexión con la base de datos.
     */
    public final void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DataContext] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DataContext] Error al cerrar: " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión está activa.
     * @return true si está conectado
     */
    public final boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── Método auxiliar privado ───────────────────────────────────────

    /**
     * Asigna los parámetros al PreparedStatement.
     */
    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    // ── Métodos abstractos que DEBEN implementar los hijos ────────────

    /**
     * Mapea un ResultSet a un objeto del modelo.
     * @param rs ResultSet posicionado en la fila actual
     * @return Objeto del modelo correspondiente
     */
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    /**
     * Retorna el nombre de la tabla asociada.
     * @return nombre de la tabla en sakila
     */
    protected abstract String getTableName();

    /**
     * Retorna el nombre de la columna PK de la tabla.
     * @return nombre de la PK
     */
    protected abstract String getPrimaryKey();
}
