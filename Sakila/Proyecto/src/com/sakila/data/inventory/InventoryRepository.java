package com.sakila.data.inventory;

import com.sakila.data.DataContext;
import com.sakila.models.Inventory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repositorio concreto para la entidad Inventory.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class InventoryRepository extends DataContext<Inventory, Integer> {

    @Override
    protected String getTableName() {
        return "inventory";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "inventory_id";
    }

    @Override
    protected Integer getPrimaryKeyValue(Inventory entity) {
        return entity.getInventoryId();
    }

    @Override
    protected void setPrimaryKeyValue(Inventory entity, Integer id) {
        entity.setInventoryId(id);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO inventory (film_id, store_id, last_update) VALUES (?, ?, NOW())";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE inventory SET film_id = ?, store_id = ?, last_update = NOW() WHERE inventory_id = ?";
    }

    @Override
    protected void setParameters(PreparedStatement ps, Inventory inventory, boolean isUpdate) throws SQLException {
        int i = 1;
        ps.setInt(i++, inventory.getFilmId());
        ps.setInt(i++, inventory.getStoreId());
        // last_update se maneja con NOW() en el SQL, no requiere parámetro aquí
        // inventory_id para UPDATE se añade en DataContext, no aquí
    }

    @Override
    protected Inventory mapResultSetToEntity(ResultSet rs) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(rs.getInt("inventory_id"));
        inventory.setFilmId(rs.getInt("film_id"));
        inventory.setStoreId(rs.getInt("store_id"));
        inventory.setLastUpdate(rs.getTimestamp("last_update"));
        return inventory;
    }

    @Override
    protected int getParameterCount(Inventory inventory, boolean isUpdate) {
        // En INSERT/UPDATE: film_id, store_id (2 parámetros)
        // El inventory_id para el WHERE en UPDATE se añade por DataContext
        return 2;
    }
}