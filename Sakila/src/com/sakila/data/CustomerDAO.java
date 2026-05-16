package com.sakila.data;

import com.sakila.models.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto y final para la tabla customer.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public final class CustomerDAO extends DataContext<Customer> {

    @Override
    protected Customer mapRow(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("customer_id"),
            rs.getInt("store_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getInt("address_id"),
            rs.getBoolean("active")
        );
    }

    @Override protected String getTableName()  { return "customer"; }
    @Override protected String getPrimaryKey() { return "customer_id"; }

    @Override
    public boolean post(Customer c) {
        String sql = "INSERT INTO customer (store_id, first_name, last_name, email, address_id, active) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        int id = executeInsertGetKey(sql,
                c.getStoreId(), c.getFirstName(), c.getLastName(),
                c.getEmail(), c.getAddressId(), c.isActive());
        if (id > 0) { c.setCustomerId(id); return true; }
        return false;
    }

    @Override
    public boolean put(Customer c) {
        return executeUpdate(
            "UPDATE customer SET first_name=?, last_name=?, email=?, active=? WHERE customer_id=?",
            c.getFirstName(), c.getLastName(), c.getEmail(), c.isActive(), c.getCustomerId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM customer WHERE customer_id=?", id);
    }

    @Override
    public List<Customer> get() {
        List<Customer> list = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT * FROM customer ORDER BY last_name");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[CustomerDAO] get() error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Customer getById(int id) {
        ResultSet rs = executeQuery("SELECT * FROM customer WHERE customer_id=?", id);
        try {
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[CustomerDAO] getById() error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca clientes por nombre o apellido.
     * @param name Texto a buscar
     * @return Lista de clientes que coinciden
     */
    public List<Customer> search(String name) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE first_name LIKE ? OR last_name LIKE ? " +
                     "ORDER BY last_name";
        ResultSet rs = executeQuery(sql, "%" + name + "%", "%" + name + "%");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[CustomerDAO] search() error: " + e.getMessage());
        }
        return list;
    }
}
