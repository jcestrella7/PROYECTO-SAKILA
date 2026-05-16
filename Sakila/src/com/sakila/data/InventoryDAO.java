package com.sakila.data;

import com.sakila.models.Inventory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto y final para la tabla inventory.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public final class InventoryDAO extends DataContext<Inventory> {

    @Override
    protected Inventory mapRow(ResultSet rs) throws SQLException {
        return new Inventory(
            rs.getInt("inventory_id"),
            rs.getInt("film_id"),
            rs.getInt("store_id")
        );
    }

    @Override protected String getTableName()  { return "inventory"; }
    @Override protected String getPrimaryKey() { return "inventory_id"; }

    @Override
    public boolean post(Inventory inv) {
        String sql = "INSERT INTO inventory (film_id, store_id) VALUES (?, ?)";
        int id = executeInsertGetKey(sql, inv.getFilmId(), inv.getStoreId());
        if (id > 0) { inv.setInventoryId(id); return true; }
        return false;
    }

    @Override
    public boolean put(Inventory inv) {
        return executeUpdate(
            "UPDATE inventory SET film_id=?, store_id=? WHERE inventory_id=?",
            inv.getFilmId(), inv.getStoreId(), inv.getInventoryId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM inventory WHERE inventory_id=?", id);
    }

    @Override
    public List<Inventory> get() {
        List<Inventory> list = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT * FROM inventory ORDER BY inventory_id");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[InventoryDAO] get() error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Inventory getById(int id) {
        ResultSet rs = executeQuery("SELECT * FROM inventory WHERE inventory_id=?", id);
        try {
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[InventoryDAO] getById() error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el inventario de una película específica.
     * @param filmId ID de la película
     * @return Lista de copias disponibles
     */
    public List<Inventory> getByFilm(int filmId) {
        List<Inventory> list = new ArrayList<>();
        ResultSet rs = executeQuery(
            "SELECT * FROM inventory WHERE film_id=? ORDER BY store_id", filmId);
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[InventoryDAO] getByFilm() error: " + e.getMessage());
        }
        return list;
    }
}
