package com.sakila.data; // Asegúrate de que el paquete sea correcto

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sakila.data.ConnectionManager; // Asegúrate de importar ConnectionManager
import com.sakila.data.IDatapostCrudRepository; // <-- Esta es la línea de importación

/**
 * Clase abstracta base para el contexto de datos, proporcionando operaciones CRUD comunes.
 * Maneja la lógica de conexión y ejecución de sentencias SQL.
 *
 * @param <T>  Tipo de la entidad (ej. Film, Actor).
 * @param <ID> Tipo del identificador de la entidad (ej. Integer, Long).
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public abstract class DataContext<T, ID> implements IDatapostCrudRepository<T, ID> {

    // Métodos abstractos que las subclases deben implementar
    protected abstract String getTableName();
    protected abstract String getPrimaryKeyColumnName();
    protected abstract ID getPrimaryKeyValue(T entity);
    protected abstract void setPrimaryKeyValue(T entity, ID id);
    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract void setParameters(PreparedStatement ps, T entity, boolean isUpdate) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract int getParameterCount(T entity, boolean isUpdate); // Método para contar parámetros en setParameters

    // Implementación del método create (Save)
    @Override
    public final T save(T entity) {
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql;
            // Asumiendo que 0 es el valor por defecto para un ID no asignado si ID es Integer
            boolean isNew = (getPrimaryKeyValue(entity) == null || (getPrimaryKeyValue(entity) instanceof Integer && ((Integer) getPrimaryKeyValue(entity)) == 0));

            if (isNew) {
                sql = getInsertSql();
            } else {
                sql = getUpdateSql();
            }

            try (PreparedStatement ps = isNew ? conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(sql)) {
                setParameters(ps, entity, !isNew); // !isNew es true si es update, false si es insert

                if (!isNew) { // Si es una actualización, añadir el ID al final de los parámetros
                    ps.setObject(getParameterCount(entity, true) + 1, getPrimaryKeyValue(entity)); // +1 porque setObject es 1-based index
                }

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("La operación falló, no se afectaron filas.");
                }

                if (isNew) { // Si fue un insert, intentar recuperar el ID generado
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Asume que ID es Integer para este ejemplo, ajusta si es Long u otro tipo
                            setPrimaryKeyValue(entity, (ID) Integer.valueOf(generatedKeys.getInt(1)));
                        } else {
                            throw new SQLException("La creación de la entidad falló, no se obtuvo ID generado.");
                        }
                    }
                }
            }
            return entity;
        } catch (SQLException e) {
            System.err.println("Error al guardar la entidad: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la entidad", e);
        } finally {
            ConnectionManager.closeConnection(); // Cierra la conexión estática
        }
    }

    // Implementación del método findById (Read)
    @Override
    public final Optional<T> findById(ID id) {
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKeyColumnName() + " = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por ID en la entidad " + getTableName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al buscar por ID", e);
        } finally {
            ConnectionManager.closeConnection(); // Cierra la conexión estática
        }
        return Optional.empty();
    }

    // Implementación del método findAll (Read all)
    @Override
    public final List<T> findAll() {
        List<T> entities = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "SELECT * FROM " + getTableName();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    entities.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todas las entidades de " + getTableName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al listar todas las entidades", e);
        } finally {
            ConnectionManager.closeConnection(); // Cierra la conexión estática
        }
        return entities;
    }

    // Implementación del método update (Update)

    public final T update(T entity) {
        return save(entity); // Delegar a save, ya que maneja la lógica de inserción/actualización
    }

    // Implementación del método deleteById (Delete)
    @Override
    public final void deleteById(ID id) {
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKeyColumnName() + " = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, id);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("Advertencia: No se encontró la entidad con ID " + id + " para eliminar.");
                } else {
                    System.out.println("Entidad con ID " + id + " eliminada correctamente.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar la entidad con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la entidad", e);
        } finally {
            ConnectionManager.closeConnection(); // Cierra la conexión estática
        }
    }

    // *******************************************************************
    // Implementación del método delete(T entity) - ¡ESTE ES EL QUE DEBE ESTAR!
    // *******************************************************************
    @Override // <-- Esta anotación indica que estamos implementando un método de la interfaz
    public final void delete(T entity) {
        // Delega la eliminación a deleteById usando el ID de la entidad
        if (entity != null && getPrimaryKeyValue(entity) != null) {
            deleteById(getPrimaryKeyValue(entity));
        } else {
            System.err.println("Error: No se pudo eliminar la entidad porque el objeto o su ID es nulo.");
            throw new IllegalArgumentException("La entidad o su ID no pueden ser nulos para eliminar.");
        }
    }

    // Método para búsqueda genérica con WHERE clause
    public final List<T> search(String whereClause, Object... params) {
        List<T> entities = new ArrayList<>();
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            String sql = "SELECT * FROM " + getTableName();
            if (whereClause != null && !whereClause.trim().isEmpty()) {
                sql += " WHERE " + whereClause;
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        entities.add(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la búsqueda en " + getTableName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en la búsqueda", e);
        } finally {
            ConnectionManager.closeConnection(); // Cierra la conexión estática
        }
        return entities;
    }
}