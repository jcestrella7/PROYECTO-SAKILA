package com.sakila.ui;

import com.sakila.controllers.FilmController;
import java.util.Scanner;

/**
 * Interfaz de usuario para la gestión de películas.
 * Maneja el menú específico para operaciones con películas y delega
 * la lógica de negocio al FilmController.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class FilmConsoleUI {

    private final Scanner scanner;
    private final FilmController filmController;

    public FilmConsoleUI(Scanner scanner) {
        this.scanner = scanner;
        this.filmController = new FilmController(scanner);
    }

    /**
     * Inicia el menú de operaciones con películas.
     */
    public void startFilmMenu() {
        int choice;
        do {
            printFilmMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                choice = -1; // Para que el bucle continúe
                continue;
            }


            switch (choice) {
                case 1:
                    filmController.listAll();
                    break;
                case 2:
                    System.out.print("Ingrese el ID de la película a buscar: ");
                    try {
                        int filmId = Integer.parseInt(scanner.nextLine());
                        filmController.findAndDisplayById(filmId);
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido. Por favor, ingrese un número.");
                    }
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

    private void printFilmMenu() {
        System.out.println("\n--- Gestión de Películas ---");
        System.out.println("1. Listar Películas");
        System.out.println("2. Buscar Película por ID");
        System.out.println("3. Crear Película");
        System.out.println("4. Actualizar Película");
        System.out.println("5. Eliminar Película");
        System.out.println("6. Buscar Películas por criterios");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }
}