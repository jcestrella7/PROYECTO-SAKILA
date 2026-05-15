package com.sakila.data;

import com.sakila.models.Rental;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto y final para la tabla rental.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public final class RentalDAO extends DataContext<Rental> {

    @Override
    protected Rental mapRow(ResultSet rs) throws SQLException {
        Rental r = new Rental();
        r.setRentalId(rs.getInt("rental_id"));
        r.setInventoryId(rs.getInt("inventory_id"));
        r.setCustomerId(rs.getInt("customer_id"));
        r.setStaffId(rs.getInt("staff_id"));
        Timestamp rent = rs.getTimestamp("rental_date");
        Timestamp ret  = rs.getTimestamp("return_date");
        if (rent != null) r.setRentalDate(rent.toLocalDateTime());
        if (ret  != null) r.setReturnDate(ret.toLocalDateTime());
        return r;
    }

    @Override protected String getTableName()  { return "rental"; }
    @Override protected String getPrimaryKey() { return "rental_id"; }

    @Override
    public boolean post(Rental rental) {
        String sql = "INSERT INTO rental (rental_date, inventory_id, customer_id, staff_id) " +
                     "VALUES (NOW(), ?, ?, ?)";
        int id = executeInsertGetKey(sql,
                rental.getInventoryId(), rental.getCustomerId(), rental.getStaffId());
        if (id > 0) { rental.setRentalId(id); return true; }
        return false;
    }

    @Override
    public boolean put(Rental rental) {
        return executeUpdate(
            "UPDATE rental SET return_date=? WHERE rental_id=?",
            rental.getReturnDate() != null
                ? Timestamp.valueOf(rental.getReturnDate()) : null,
            rental.getRentalId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM rental WHERE rental_id=?", id);
    }

    @Override
    public List<Rental> get() {
        List<Rental> list = new ArrayList<>();
        ResultSet rs = executeQuery(
            "SELECT * FROM rental ORDER BY rental_date DESC LIMIT 500");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[RentalDAO] get() error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Rental getById(int id) {
        ResultSet rs = executeQuery("SELECT * FROM rental WHERE rental_id=?", id);
        try {
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[RentalDAO] getById() error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene rentas pendientes de devolución (return_date IS NULL).
     * @return Lista de rentas activas
     */
    public List<Rental> getPending() {
        List<Rental> list = new ArrayList<>();
        ResultSet rs = executeQuery(
            "SELECT * FROM rental WHERE return_date IS NULL ORDER BY rental_date");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[RentalDAO] getPending() error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Registra la devolución de una renta.
     * @param rentalId ID de la renta
     * @return true si se registró
     */
    public boolean registerReturn(int rentalId) {
        return executeUpdate(
            "UPDATE rental SET return_date=NOW() WHERE rental_id=?", rentalId);
    }
}
