package com.sakila.controllers;

import com.sakila.data.RentalDAO;
import com.sakila.data.InventoryDAO;
import com.sakila.models.Rental;
import java.util.List;

/**
 * Controlador MVC para la gestión de rentas.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class RentalController {

    private final RentalDAO   rentalDAO;
    private final InventoryDAO inventoryDAO;

    public RentalController() {
        this.rentalDAO    = new RentalDAO();
        this.inventoryDAO = new InventoryDAO();
    }

    /**
     * Lista todas las rentas (últimas 500).
     * @return Lista de Rental
     */
    public List<Rental> listAll() {
        return rentalDAO.get();
    }

    /**
     * Lista rentas pendientes de devolución.
     * @return Lista de rentas activas
     */
    public List<Rental> listPending() {
        return rentalDAO.getPending();
    }

    /**
     * Registra una nueva renta.
     * @param inventoryId ID del inventario
     * @param customerId  ID del cliente
     * @param staffId     ID del empleado
     * @return true si se registró correctamente
     */
    public boolean rentFilm(int inventoryId, int customerId, int staffId) {
        if (inventoryDAO.getById(inventoryId) == null) {
            System.out.println("[RentalController] Error: Inventario no encontrado.");
            return false;
        }
        Rental r = new Rental();
        r.setInventoryId(inventoryId);
        r.setCustomerId(customerId);
        r.setStaffId(staffId);
        boolean ok = rentalDAO.post(r);
        if (ok) System.out.println("[RentalController] Renta registrada con ID: " + r.getRentalId());
        return ok;
    }

    /**
     * Registra la devolución de una renta.
     * @param rentalId ID de la renta
     * @return true si se registró la devolución
     */
    public boolean returnFilm(int rentalId) {
        if (rentalDAO.getById(rentalId) == null) {
            System.out.println("[RentalController] Error: Renta no encontrada.");
            return false;
        }
        return rentalDAO.registerReturn(rentalId);
    }

    /**
     * Obtiene una renta por ID.
     * @param id rental_id
     * @return Rental o null
     */
    public Rental findById(int id) {
        return rentalDAO.getById(id);
    }

    /** Cierra las conexiones */
    public void close() {
        rentalDAO.closeConnection();
        inventoryDAO.closeConnection();
    }
}
