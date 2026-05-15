package com.sakila.models;

import java.sql.Timestamp;

/**
 * Representa un cliente en el sistema Sakila.
 * Este es un POJO (Plain Old Java Object) que mapea a la tabla 'customer'.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class Customer {
    private int customerId;
    private int storeId;
    private String firstName;
    private String lastName;
    private String email;
    private int addressId;
    private boolean active;
    private Timestamp createDate;
    private Timestamp lastUpdate;

    // Constructor vacío
    public Customer() {
    }

    // Constructor completo
    public Customer(int customerId, int storeId, String firstName, String lastName,
                    String email, int addressId, boolean active, Timestamp createDate,
                    Timestamp lastUpdate) {
        this.customerId = customerId;
        this.storeId = storeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    // --- Getters y Setters ---

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", storeId=" + storeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", addressId=" + addressId +
                ", active=" + active +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}