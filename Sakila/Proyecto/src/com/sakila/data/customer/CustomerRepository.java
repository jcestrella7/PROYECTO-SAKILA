package com.sakila.data.customer;

import com.sakila.data.DataContext;
import com.sakila.models.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types; // Para manejar NULL con setNull

/**
 * Repositorio concreto para la entidad Customer.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class CustomerRepository extends DataContext<Customer, Integer> {

    @Override
    protected String getTableName() {
        return "customer";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "customer_id";
    }

    @Override
    protected Integer getPrimaryKeyValue(Customer entity) {
        return entity.getCustomerId();
    }

    @Override
    protected void setPrimaryKeyValue(Customer entity, Integer id) {
        entity.setCustomerId(id);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO customer (store_id, first_name, last_name, email, address_id, active, create_date, last_update) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE customer SET store_id = ?, first_name = ?, last_name = ?, email = ?, address_id = ?, active = ?, last_update = NOW() " +
                "WHERE customer_id = ?";
    }

    @Override
    protected void setParameters(PreparedStatement ps, Customer customer, boolean isUpdate) throws SQLException {
        int i = 1;
        ps.setInt(i++, customer.getStoreId());
        ps.setString(i++, customer.getFirstName());
        ps.setString(i++, customer.getLastName());

        // Manejo de email (puede ser NULL)
        if (customer.getEmail() != null) {
            ps.setString(i++, customer.getEmail());
        } else {
            ps.setNull(i++, Types.VARCHAR);
        }

        ps.setInt(i++, customer.getAddressId());
        ps.setBoolean(i++, customer.isActive());
        // create_date y last_update se manejan con NOW() en el SQL, no requieren parámetro aquí
        // customer_id para UPDATE se añade en DataContext, no aquí
    }

    @Override
    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setStoreId(rs.getInt("store_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setAddressId(rs.getInt("address_id"));
        customer.setActive(rs.getBoolean("active"));
        customer.setCreateDate(rs.getTimestamp("create_date"));
        customer.setLastUpdate(rs.getTimestamp("last_update"));
        return customer;
    }

    @Override
    protected int getParameterCount(Customer customer, boolean isUpdate) {
        // En INSERT/UPDATE: store_id, first_name, last_name, email, address_id, active (6 parámetros)
        // El customer_id para el WHERE en UPDATE se añade por DataContext
        return 6;
    }
}