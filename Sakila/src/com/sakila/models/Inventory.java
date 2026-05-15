package com.sakila.models;

/**
 * Modelo que representa la tabla inventory de sakila.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class Inventory {

    private int inventoryId;
    private int filmId;
    private int storeId;

    public Inventory() {}

    public Inventory(int inventoryId, int filmId, int storeId) {
        this.inventoryId = inventoryId;
        this.filmId      = filmId;
        this.storeId     = storeId;
    }

    public int getInventoryId()          { return inventoryId; }
    public void setInventoryId(int id)   { this.inventoryId = id; }

    public int getFilmId()               { return filmId; }
    public void setFilmId(int id)        { this.filmId = id; }

    public int getStoreId()              { return storeId; }
    public void setStoreId(int id)       { this.storeId = id; }

    @Override
    public String toString() {
        return String.format("[%d] Film:%d | Tienda:%d", inventoryId, filmId, storeId);
    }
}
