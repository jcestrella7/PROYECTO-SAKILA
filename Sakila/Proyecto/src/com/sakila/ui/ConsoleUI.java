package com.sakila.ui;

import com.sakila.controllers.FilmController;
import com.sakila.controllers.CustomerController; // Importar el CustomerController
import java.util.Scanner;

/**
 * Clase principal de la interfaz de usuario basada en consola para la aplicación Sakila.
 * Permite al usuario interactuar con diferentes módulos (películas, clientes, etc.).
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class ConsoleUI {

    private final Scanner scanner;
    private final FilmController filmController;
    private final CustomerController customerController; // Instancia del CustomerController

    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
        this.filmController = new FilmController(scanner);
        this.customerController = new CustomerController(scanner); // Inicializar CustomerController
    }

    public void start() {
        int choice;
        do {
            printMainMenu();
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    handleFilmOperations();
                    break;
                case 2:
                    handleCustomerOperations(); // Nueva opción para clientes
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (choice != 0);
    }

    private void printMainMenu() {
        System.out.println("\n--- Menú Principal Sakila ---");
        System.out.println("1. Gestión de Películas");
        System.out.println("2. Gestión de Clientes"); // Nueva opción
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void printSubMenu(String entityName) {
        System.out.println("\n--- Gestión de " + entityName + " ---");
        System.out.println("1. Listar " + entityName + "");
        System.out.println("2. Buscar " + entityName + " por ID");
        System.out.println("3. Crear " + entityName + "");
        System.out.println("4. Actualizar " + entityName + "");
        System.out.println("5. Eliminar " + entityName + "");
        System.out.println("6. Buscar " + entityName + " por criterios"); // Búsqueda más flexible
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }

    private void handleFilmOperations() {
        int choice;
        do {
            printSubMenu("Películas");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    filmController.listAll();
                    break;
                case 2:
                    System.out.print("Ingrese el ID de la película: ");
                    String filmIdStr = scanner.nextLine().trim();
                    if (filmIdStr.isEmpty()) {
                        System.out.println("ID requerido!");
                        continue;
                    }
                    int filmId = Integer.parseInt(filmIdStr);
                    break;
                case 3:
                    filmController.handleCreate();
                    break;
                case 4:
                    filmController.handleUpdate();
                    break;
                case 5:
                    filmController.handleDelete();
                    break;
                case 6:
                    filmController.handleSearch();
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (choice != 0);
    }

    private void handleCustomerOperations() {
        int choice;
        do {
            printSubMenu("Clientes");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    customerController.listAll();
                    break;
                case 2:
                    System.out.print("Ingrese el ID del cliente: ");
                    String customerIdStr = scanner.nextLine().trim();
                    if (customerIdStr.isEmpty()) {
                        System.out.println("ID requerido!");
                        continue;
                    }
                    int customerId = Integer.parseInt(customerIdStr);
                    break;
                case 3:
                    customerController.handleCreate();
                    break;
                case 4:
                    customerController.handleUpdate();
                    break;
                case 5:
                    customerController.handleDelete();
                    break;
                case 6:
                    customerController.handleSearch();
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (choice != 0);
    }
}