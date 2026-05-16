package com.sakila.data.rental;

import com.sakila.data.DataContext;
import com.sakila.models.Rental;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types; // Para manejar NULL

/**
 * Repositorio concreto para la entidad Rental.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class RentalRepository extends DataContext<Rental, Integer> {

    @Override
    protected String getTableName() {
        return "rental";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "rental_id";
    }

    @Override
    protected Integer getPrimaryKeyValue(Rental entity) {
        return entity.getRentalId();
    }

    @Override
    protected void setPrimaryKeyValue(Rental entity, Integer id) {
        entity.setRentalId(id);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO rental (rental_date, inventory_id, customer_id, return_date, staff_id, last_update) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE rental SET rental_date = ?, inventory_id = ?, customer_id = ?, return_date = ?, staff_id = ?, last_update = NOW() " +
                "WHERE rental_id = ?";
    }

    @Override
    protected void setParameters(PreparedStatement ps, Rental rental, boolean isUpdate) throws SQLException {
        int i = 1;
        ps.setTimestamp(i++, rental.getRentalDate());
        ps.setInt(i++, rental.getInventoryId());
        ps.setInt(i++, rental.getCustomerId());

        // Manejo de return_date (puede ser NULL)
        if (rental.getReturnDate() != null) {
            ps.setTimestamp(i++, rental.getReturnDate());
        } else {
            ps.setNull(i++, Types.TIMESTAMP);
        }

        ps.setInt(i++, rental.getStaffId());
        // last_update se maneja con NOW() en el SQL, no requiere parámetro aquí
        // rental_id para UPDATE se añade en DataContext, no aquí
    }

    @Override
    protected Rental mapResultSetToEntity(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setRentalId(rs.getInt("rental_id"));
        rental.setRentalDate(rs.getTimestamp("rental_date"));
        rental.setInventoryId(rs.getInt("inventory_id"));
        rental.setCustomerId(rs.getInt("customer_id"));
        rental.setReturnDate(rs.getTimestamp("return_date")); // getTimestamp maneja null automáticamente
        rental.setStaffId(rs.getInt("staff_id"));
        rental.setLastUpdate(rs.getTimestamp("last_update"));
        return rental;
    }

    @Override
    protected int getParameterCount(Rental rental, boolean isUpdate) {
        // En INSERT/UPDATE: rental_date, inventory_id, customer_id, return_date, staff_id (5 parámetros)
        // El rental_id para el WHERE en UPDATE se añade por DataContext
        return 5;
    }
}