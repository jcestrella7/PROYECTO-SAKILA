package com.sakila.controllers;

import com.sakila.data.rental.RentalRepository;
import com.sakila.models.Rental;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;

/**
 * Controlador para la gestión de alquileres.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class RentalController extends BaseController<Rental, Integer> {

    private final RentalRepository rentalRepository;
    // Formato para parsear y mostrar fechas
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RentalController(Scanner scanner) {
        super(new RentalRepository(), scanner);
        this.rentalRepository = (RentalRepository) repository;
    }

    @Override
    protected String getEntityNameSingular() {
        return "alquiler";
    }

    @Override
    protected String getEntityNamePlural() {
        return "alquileres";
    }

    @Override
    protected void displayEntity(Rental rental) {
        System.out.println("  ID de Alquiler: " + rental.getRentalId());
        System.out.println("  Fecha de Alquiler: " + (rental.getRentalDate() != null ? dateFormat.format(rental.getRentalDate()) : "N/A"));
        System.out.println("  ID de Inventario: " + rental.getInventoryId());
        System.out.println("  ID de Cliente: " + rental.getCustomerId());
        System.out.println("  Fecha de Devolución: " + (rental.getReturnDate() != null ? dateFormat.format(rental.getReturnDate()) : "PENDIENTE"));
        System.out.println("  ID de Empleado: " + rental.getStaffId());
        System.out.println("  Última Actualización: " + (rental.getLastUpdate() != null ? dateFormat.format(rental.getLastUpdate()) : "N/A"));
        System.out.println("----------------------------------------");
    }

    @Override
    public void handleCreate() {
        System.out.println("\n--- Registrar Nuevo Alquiler ---");
        Rental newRental = new Rental();

        System.out.print("ID de Inventario: ");
        newRental.setInventoryId(Integer.parseInt(scanner.nextLine()));

        System.out.print("ID de Cliente: ");
        newRental.setCustomerId(Integer.parseInt(scanner.nextLine()));

        System.out.print("ID de Empleado: ");
        newRental.setStaffId(Integer.parseInt(scanner.nextLine()));

        // La fecha de alquiler es el momento actual
        newRental.setRentalDate(new Timestamp(System.currentTimeMillis()));

        // La fecha de devolución inicialmente puede ser null
        newRental.setReturnDate(null);

        try {
            Rental createdRental = rentalRepository.save(newRental);
            System.out.println("Alquiler registrado con éxito. ID: " + createdRental.getRentalId());
        } catch (RuntimeException e) {
            System.err.println("Error al registrar el alquiler: " + e.getMessage());
        }
    }

    @Override
    public void handleUpdate() {
        System.out.println("\n--- Actualizar Alquiler ---");
        System.out.print("Ingrese el ID del alquiler a actualizar: ");
        int rentalId = Integer.parseInt(scanner.nextLine());

        Optional<Rental> existingRentalOpt = rentalRepository.findById(rentalId);
        if (existingRentalOpt.isPresent()) {
            Rental existingRental = existingRentalOpt.get();
            System.out.println("Datos actuales del alquiler:");
            displayEntity(existingRental);

            System.out.print("Nuevo ID de Inventario (" + existingRental.getInventoryId() + "): ");
            String inventoryIdStr = scanner.nextLine();
            if (!inventoryIdStr.isEmpty()) existingRental.setInventoryId(Integer.parseInt(inventoryIdStr));

            System.out.print("Nuevo ID de Cliente (" + existingRental.getCustomerId() + "): ");
            String customerIdStr = scanner.nextLine();
            if (!customerIdStr.isEmpty()) existingRental.setCustomerId(Integer.parseInt(customerIdStr));

            System.out.print("Nuevo ID de Empleado (" + existingRental.getStaffId() + "): ");
            String staffIdStr = scanner.nextLine();
            if (!staffIdStr.isEmpty()) existingRental.setStaffId(Integer.parseInt(staffIdStr));

            System.out.print("Nueva Fecha de Devolución (yyyy-MM-dd HH:mm:ss, dejar en blanco para mantener/vacío): ");
            String returnDateStr = scanner.nextLine();
            if (!returnDateStr.isEmpty()) {
                try {
                    Date parsedDate = dateFormat.parse(returnDateStr);
                    existingRental.setReturnDate(new Timestamp(parsedDate.getTime()));
                } catch (ParseException e) {
                    System.err.println("Formato de fecha no válido. Se mantendrá la fecha anterior.");
                    // Opcionalmente, podrías establecerla a null si el usuario ingresó algo incorrecto
                    // existingRental.setReturnDate(null);
                }
            } else {
                // Si el usuario deja en blanco, y no había fecha de devolución, o si se quiere "limpiar"
                // Aquí, decidimos que dejar en blanco significa no cambiarla si ya existía.
                // Si quieres que dejar en blanco la ponga a NULL, tendrías que hacer:
                // existingRental.setReturnDate(null);
            }

            try {
                Rental updatedRental = rentalRepository.update(existingRental);
                System.out.println("Alquiler con ID " + updatedRental.getRentalId() + " actualizado con éxito.");
            } catch (RuntimeException e) {
                System.err.println("Error al actualizar el alquiler: " + e.getMessage());
            }

        } else {
            System.out.println("No se encontró ningún alquiler con el ID " + rentalId + ".");
        }
    }

    @Override
    public void handleDelete() {
        System.out.println("\n--- Eliminar Alquiler ---");
        System.out.print("Ingrese el ID del alquiler a eliminar: ");
        int rentalId = Integer.parseInt(scanner.nextLine());

        Optional<Rental> rentalToDelete = rentalRepository.findById(rentalId);
        if (rentalToDelete.isPresent()) {
            System.out.print("¿Está seguro de que desea eliminar el alquiler con ID '" + rentalToDelete.get().getRentalId() + "' (S/N)? ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("S")) {
                try {
                    rentalRepository.deleteById(rentalId);
                    System.out.println("Alquiler con ID " + rentalId + " eliminado con éxito.");
                } catch (RuntimeException e) {
                    System.err.println("Error al eliminar el alquiler: " + e.getMessage());
                }
            } else {
                System.out.println("Operación de eliminación cancelada.");
            }
        } else {
            System.out.println("No se encontró ningún alquiler con el ID " + rentalId + ".");
        }
    }

    @Override
    public void handleSearch() {
        System.out.println("\n--- Buscar Alquileres ---");
        System.out.println("Opciones de búsqueda:");
        System.out.println("1. Por ID de Cliente");
        System.out.println("2. Por ID de Inventario");
        System.out.print("Seleccione una opción: ");
        String searchOption = scanner.nextLine();
        List<Rental> results = null;

        switch (searchOption) {
            case "1":
                System.out.print("Ingrese el ID del cliente: ");
                int customerId = Integer.parseInt(scanner.nextLine());
                results = rentalRepository.search("customer_id = ?", customerId);
                break;
            case "2":
                System.out.print("Ingrese el ID del inventario: ");
                int inventoryId = Integer.parseInt(scanner.nextLine());
                results = rentalRepository.search("inventory_id = ?", inventoryId);
                break;
            default:
                System.out.println("Opción de búsqueda no válida.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("\n--- Resultados de la Búsqueda ---");
            results.forEach(this::displayEntity);
        } else {
            System.out.println("No se encontraron alquileres que coincidan con los criterios de búsqueda.");
        }
    }
}