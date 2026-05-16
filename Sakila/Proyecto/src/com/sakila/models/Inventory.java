package com.sakila.models;

import java.sql.Timestamp;

/**
 * Representa un inventario de películas en el sistema Sakila.
 * Este es un POJO (Plain Old Java Object) que mapea a la tabla 'inventory'.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class Inventory {
    private int inventoryId;
    private int filmId;
    private int storeId;
    private Timestamp lastUpdate;

    // Constructor vacío
    public Inventory() {
    }

    // Constructor completo
    public Inventory(int inventoryId, int filmId, int storeId, Timestamp lastUpdate) {
        this.inventoryId = inventoryId;
        this.filmId = filmId;
        this.storeId = storeId;
        this.lastUpdate = lastUpdate;
    }

    // --- Getters y Setters ---

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", filmId=" + filmId +
                ", storeId=" + storeId +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}