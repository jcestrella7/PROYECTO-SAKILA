package com.sakila.controllers;

import com.sakila.data.inventory.InventoryRepository;
import com.sakila.models.Inventory;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;

/**
 * Controlador para la gestión de inventario.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class InventoryController extends BaseController<Inventory, Integer> {

    private final InventoryRepository inventoryRepository;

    public InventoryController(Scanner scanner) {
        super(new InventoryRepository(), scanner);
        this.inventoryRepository = (InventoryRepository) repository;
    }

    @Override
    protected String getEntityNameSingular() {
        return "inventario";
    }

    @Override
    protected String getEntityNamePlural() {
        return "inventario"; // El plural para inventario puede ser el mismo
    }

    @Override
    protected void displayEntity(Inventory inventory) {
        System.out.println("  ID de Inventario: " + inventory.getInventoryId());
        System.out.println("  ID de Película: " + inventory.getFilmId());
        System.out.println("  ID de Tienda: " + inventory.getStoreId());
        System.out.println("  Última Actualización: " + inventory.getLastUpdate());
        System.out.println("----------------------------------------");
    }

    @Override
    public void handleCreate() {
        System.out.println("\n--- Registrar Nuevo Elemento de Inventario ---");
        Inventory newInventory = new Inventory();

        System.out.print("ID de Película: ");
        newInventory.setFilmId(Integer.parseInt(scanner.nextLine()));

        System.out.print("ID de Tienda: ");
        newInventory.setStoreId(Integer.parseInt(scanner.nextLine()));

        try {
            Inventory createdInventory = inventoryRepository.save(newInventory);
            System.out.println("Elemento de inventario registrado con éxito. ID: " + createdInventory.getInventoryId());
        } catch (RuntimeException e) {
            System.err.println("Error al registrar el elemento de inventario: " + e.getMessage());
        }
    }

    @Override
    public void handleUpdate() {
        System.out.println("\n--- Actualizar Elemento de Inventario ---");
        System.out.print("Ingrese el ID del elemento de inventario a actualizar: ");
        int inventoryId = Integer.parseInt(scanner.nextLine());

        Optional<Inventory> existingInventoryOpt = inventoryRepository.findById(inventoryId);
        if (existingInventoryOpt.isPresent()) {
            Inventory existingInventory = existingInventoryOpt.get();
            System.out.println("Datos actuales del inventario:");
            displayEntity(existingInventory);

            System.out.print("Nuevo ID de Película (" + existingInventory.getFilmId() + "): ");
            String filmIdStr = scanner.nextLine();
            if (!filmIdStr.isEmpty()) existingInventory.setFilmId(Integer.parseInt(filmIdStr));

            System.out.print("Nuevo ID de Tienda (" + existingInventory.getStoreId() + "): ");
            String storeIdStr = scanner.nextLine();
            if (!storeIdStr.isEmpty()) existingInventory.setStoreId(Integer.parseInt(storeIdStr));

            try {
                Inventory updatedInventory = inventoryRepository.update(existingInventory);
                System.out.println("Elemento de inventario con ID " + updatedInventory.getInventoryId() + " actualizado con éxito.");
            } catch (RuntimeException e) {
                System.err.println("Error al actualizar el elemento de inventario: " + e.getMessage());
            }

        } else {
            System.out.println("No se encontró ningún elemento de inventario con el ID " + inventoryId + ".");
        }
    }

    @Override
    public void handleDelete() {
        System.out.println("\n--- Eliminar Elemento de Inventario ---");
        System.out.print("Ingrese el ID del elemento de inventario a eliminar: ");
        int inventoryId = Integer.parseInt(scanner.nextLine());

        Optional<Inventory> inventoryToDelete = inventoryRepository.findById(inventoryId);
        if (inventoryToDelete.isPresent()) {
            System.out.print("¿Está seguro de que desea eliminar el elemento de inventario con ID '" + inventoryToDelete.get().getInventoryId() + "' (S/N)? ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("S")) {
                try {
                    inventoryRepository.deleteById(inventoryId);
                    System.out.println("Elemento de inventario con ID " + inventoryId + " eliminado con éxito.");
                } catch (RuntimeException e) {
                    System.err.println("Error al eliminar el elemento de inventario: " + e.getMessage());
                }
            } else {
                System.out.println("Operación de eliminación cancelada.");
            }
        } else {
            System.out.println("No se encontró ningún elemento de inventario con el ID " + inventoryId + ".");
        }
    }

    @Override
    public void handleSearch() {
        System.out.println("\n--- Buscar Inventario ---");
        System.out.println("Opciones de búsqueda:");
        System.out.println("1. Por ID de Película");
        System.out.println("2. Por ID de Tienda");
        System.out.print("Seleccione una opción: ");
        String searchOption = scanner.nextLine();
        List<Inventory> results = null;

        switch (searchOption) {
            case "1":
                System.out.print("Ingrese el ID de la película: ");
                int filmId = Integer.parseInt(scanner.nextLine());
                results = inventoryRepository.search("film_id = ?", filmId);
                break;
            case "2":
                System.out.print("Ingrese el ID de la tienda: ");
                int storeId = Integer.parseInt(scanner.nextLine());
                results = inventoryRepository.search("store_id = ?", storeId);
                break;
            default:
                System.out.println("Opción de búsqueda no válida.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("\n--- Resultados de la Búsqueda ---");
            results.forEach(this::displayEntity);
        } else {
            System.out.println("No se encontraron elementos de inventario que coincidan con los criterios de búsqueda.");
        }
    }
}