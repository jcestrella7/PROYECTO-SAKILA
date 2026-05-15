package com.sakila;

import com.sakila.controllers.*;
import com.sakila.models.*;
import com.sakila.reports.ReportManager;
import java.util.*;

/**
 * Punto de entrada principal del sistema ORM Sakila.
 * Interfaz de usuario por consola con menú interactivo.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 * @since 2021
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static FilmController   filmCtrl;
    private static RentalController rentalCtrl;
    private static ReportManager    reportMgr;

    /**
     * Método principal - inicia el sistema.
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   SISTEMA ORM - SAKILA DB  v1.0          ║");
        System.out.println("║   INF514 Z06 - UASD                      ║");
        System.out.println("╚══════════════════════════════════════════╝");

        filmCtrl   = new FilmController();
        rentalCtrl = new RentalController();
        reportMgr  = new ReportManager();

        boolean running = true;
        while (running) {
            printMainMenu();
            int option = readInt("Seleccione una opción: ");
            switch (option) {
                case 1 -> menuFilms();
                case 2 -> menuRentals();
                case 3 -> menuReports();
                case 0 -> running = false;
                default -> System.out.println("Opción no válida.");
            }
        }

        // Cierre de conexiones
        filmCtrl.close();
        rentalCtrl.close();
        reportMgr.close();
        System.out.println("\n¡Hasta luego! Sistema cerrado correctamente.");
    }

    // ── Menú Principal ────────────────────────────────────────────────

    private static void printMainMenu() {
        System.out.println("\n══════════════ MENÚ PRINCIPAL ══════════════");
        System.out.println("  1. Gestión de Películas");
        System.out.println("  2. Gestión de Rentas");
        System.out.println("  3. Reportes y Estadísticas");
        System.out.println("  0. Salir");
        System.out.println("════════════════════════════════════════════");
    }

    // ── Menú Películas ────────────────────────────────────────────────

    private static void menuFilms() {
        boolean back = false;
        while (!back) {
            System.out.println("\n──── PELÍCULAS ────");
            System.out.println("  1. Listar todas");
            System.out.println("  2. Buscar por título");
            System.out.println("  3. Buscar por rating");
            System.out.println("  4. Ver película por ID");
            System.out.println("  5. Agregar película");
            System.out.println("  6. Eliminar película");
            System.out.println("  0. Volver");

            int opt = readInt("Opción: ");
            switch (opt) {
                case 1 -> filmCtrl.listAll().forEach(System.out::println);
                case 2 -> {
                    String t = readString("Título a buscar: ");
                    filmCtrl.searchByTitle(t).forEach(System.out::println);
                }
                case 3 -> {
                    System.out.println("Ratings: G, PG, PG-13, R, NC-17");
                    String r = readString("Rating: ");
                    filmCtrl.searchByRating(r).forEach(System.out::println);
                }
                case 4 -> {
                    int id = readInt("ID de película: ");
                    Film f = filmCtrl.findById(id);
                    System.out.println(f != null ? f : "No encontrada.");
                }
                case 5 -> addFilm();
                case 6 -> {
                    int id = readInt("ID a eliminar: ");
                    System.out.println(filmCtrl.delete(id)
                        ? "✔ Eliminada." : "✘ Error al eliminar.");
                }
                case 0 -> back = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private static void addFilm() {
        System.out.println("\n── Nueva Película ──");
        String title = readString("Título: ");
        String desc  = readString("Descripción: ");
        int year     = readInt("Año de lanzamiento: ");
        int lang     = readInt("ID de idioma (1=Inglés): ");
        int dur      = readInt("Duración de renta (días): ");
        double rate  = readDouble("Precio de renta: ");
        int len      = readInt("Duración (minutos): ");
        double cost  = readDouble("Costo de reposición: ");
        System.out.println("Rating (G, PG, PG-13, R, NC-17): ");
        String rating = readString("Rating: ");

        boolean ok = filmCtrl.create(title, desc, year, lang, dur, rate, len, cost, rating);
        System.out.println(ok ? "✔ Película agregada." : "✘ Error al agregar.");
    }

    // ── Menú Rentas ───────────────────────────────────────────────────

    private static void menuRentals() {
        boolean back = false;
        while (!back) {
            System.out.println("\n──── RENTAS ────");
            System.out.println("  1. Listar rentas activas (pendientes)");
            System.out.println("  2. Nueva renta");
            System.out.println("  3. Registrar devolución");
            System.out.println("  4. Buscar renta por ID");
            System.out.println("  0. Volver");

            int opt = readInt("Opción: ");
            switch (opt) {
                case 1 -> rentalCtrl.listPending().forEach(System.out::println);
                case 2 -> {
                    int inv  = readInt("ID de inventario: ");
                    int cust = readInt("ID de cliente: ");
                    int stf  = readInt("ID de empleado: ");
                    System.out.println(rentalCtrl.rentFilm(inv, cust, stf)
                        ? "✔ Renta registrada." : "✘ Error.");
                }
                case 3 -> {
                    int id = readInt("ID de renta: ");
                    System.out.println(rentalCtrl.returnFilm(id)
                        ? "✔ Devolución registrada." : "✘ Error.");
                }
                case 4 -> {
                    int id = readInt("ID de renta: ");
                    Rental r = rentalCtrl.findById(id);
                    System.out.println(r != null ? r : "No encontrada.");
                }
                case 0 -> back = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    // ── Menú Reportes ─────────────────────────────────────────────────

    private static void menuReports() {
        boolean back = false;
        while (!back) {
            System.out.println("\n──── REPORTES ────");
            System.out.println("  1. Estadísticas de películas");
            System.out.println("  2. Top 10 clientes por rentas");
            System.out.println("  3. Top 10 actores por películas");
            System.out.println("  4. Exportar películas a CSV");
            System.out.println("  5. Exportar películas a JSON");
            System.out.println("  6. Exportar clientes a CSV");
            System.out.println("  0. Volver");

            int opt = readInt("Opción: ");
            switch (opt) {
                case 1 -> reportMgr.filmStats();
                case 2 -> reportMgr.rentalByCustomer();
                case 3 -> reportMgr.actorFilmCount();
                case 4 -> reportMgr.exportFilmsCSV("films.csv");
                case 5 -> reportMgr.exportFilmsJSON("films.json");
                case 6 -> reportMgr.exportCustomersCSV("customers.csv");
                case 0 -> back = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    // ── Helpers de lectura ────────────────────────────────────────────

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        try { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return 0.0; }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
