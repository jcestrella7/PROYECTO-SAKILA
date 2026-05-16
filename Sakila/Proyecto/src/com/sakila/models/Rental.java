package com.sakila.models;

import java.sql.Timestamp;

/**
 * Representa un alquiler de película en el sistema Sakila.
 * Este es un POJO (Plain Old Java Object) que mapea a la tabla 'rental'.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class Rental {
    private int rentalId;
    private Timestamp rentalDate;
    private int inventoryId;
    private int customerId;
    private Timestamp returnDate; // Puede ser null
    private int staffId;
    private Timestamp lastUpdate;

    // Constructor vacío
    public Rental() {
    }

    // Constructor completo
    public Rental(int rentalId, Timestamp rentalDate, int inventoryId, int customerId,
                  Timestamp returnDate, int staffId, Timestamp lastUpdate) {
        this.rentalId = rentalId;
        this.rentalDate = rentalDate;
        this.inventoryId = inventoryId;
        this.customerId = customerId;
        this.returnDate = returnDate;
        this.staffId = staffId;
        this.lastUpdate = lastUpdate;
    }

    // --- Getters y Setters ---

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Timestamp getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Timestamp rentalDate) {
        this.rentalDate = rentalDate;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", rentalDate=" + rentalDate +
                ", inventoryId=" + inventoryId +
                ", customerId=" + customerId +
                ", returnDate=" + returnDate +
                ", staffId=" + staffId +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}