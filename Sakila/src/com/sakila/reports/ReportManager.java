package com.sakila.reports;

import com.sakila.data.*;
import com.sakila.models.*;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Clase de reportes y estadísticas del sistema Sakila.
 * Genera listados, exporta a CSV/JSON y calcula métricas.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class ReportManager {

    private final FilmDAO     filmDAO;
    private final ActorDAO    actorDAO;
    private final RentalDAO   rentalDAO;
    private final CustomerDAO customerDAO;

    public ReportManager() {
        this.filmDAO     = new FilmDAO();
        this.actorDAO    = new ActorDAO();
        this.rentalDAO   = new RentalDAO();
        this.customerDAO = new CustomerDAO();
    }

    // ── Estadísticas ──────────────────────────────────────────────────

    /**
     * Estadísticas generales de películas.
     * Usa HashMap para agrupar por rating.
     */
    public void filmStats() {
        List<Film> films = filmDAO.get();
        System.out.println("\n===== ESTADÍSTICAS DE PELÍCULAS =====");
        System.out.println("Total de películas: " + films.size());

        // Promedio de duración
        double avgLength = films.stream()
            .mapToInt(Film::getLength).average().orElse(0);
        System.out.printf("Duración promedio: %.1f min%n", avgLength);

        // Agrupación por rating con HashMap
        HashMap<String, Integer> byRating = new HashMap<>();
        for (Film f : films) {
            byRating.merge(f.getRating(), 1, Integer::sum);
        }
        System.out.println("\nPelículas por Rating:");
        byRating.entrySet().stream()
            .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
            .forEach(e -> System.out.printf("  %-8s : %d%n", e.getKey(), e.getValue()));
    }

    /**
     * Estadísticas de rentas por cliente (Top 10).
     */
    public void rentalByCustomer() {
        System.out.println("\n===== RENTAS POR CLIENTE (Top 10) =====");
        List<Rental> rentals = rentalDAO.get();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Rental r : rentals) {
            map.merge(r.getCustomerId(), 1, Integer::sum);
        }
        map.entrySet().stream()
            .sorted(Map.Entry.<Integer,Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> {
                Customer c = customerDAO.getById(e.getKey());
                String name = c != null ? c.getFirstName() + " " + c.getLastName() : "N/A";
                System.out.printf("  %-30s → %d rentas%n", name, e.getValue());
            });
    }

    /**
     * Actores y cantidad de películas en las que participan (Top 10).
     */
    public void actorFilmCount() {
        System.out.println("\n===== ACTORES POR Nº DE PELÍCULAS (Top 10) =====");
        ResultSet rs = actorDAO.executeQuery(
            "SELECT a.actor_id, a.first_name, a.last_name, COUNT(fa.film_id) as total " +
            "FROM actor a JOIN film_actor fa ON a.actor_id = fa.actor_id " +
            "GROUP BY a.actor_id ORDER BY total DESC LIMIT 10");
        try {
            if (rs != null) {
                while (rs.next()) {
                    System.out.printf("  [%d] %-25s → %d películas%n",
                        rs.getInt("actor_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getInt("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReportManager] actorFilmCount error: " + e.getMessage());
        }
    }

    // ── Exportar CSV ──────────────────────────────────────────────────

    /**
     * Exporta todas las películas a CSV.
     * @param path Ruta del archivo de salida
     */
    public void exportFilmsCSV(String path) {
        List<Film> films = filmDAO.get();
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("film_id,title,release_year,rating,rental_rate,length");
            for (Film f : films) {
                pw.printf("%d,\"%s\",%d,%s,%.2f,%d%n",
                    f.getFilmId(), f.getTitle(), f.getReleaseYear(),
                    f.getRating(), f.getRentalRate(), f.getLength());
            }
            System.out.println("[CSV] Exportado: " + path + " (" + films.size() + " registros)");
        } catch (IOException e) {
            System.err.println("[CSV] Error: " + e.getMessage());
        }
    }

    /**
     * Exporta todos los clientes a CSV.
     * @param path Ruta del archivo de salida
     */
    public void exportCustomersCSV(String path) {
        List<Customer> customers = customerDAO.get();
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("customer_id,first_name,last_name,email,store_id,active");
            for (Customer c : customers) {
                pw.printf("%d,\"%s\",\"%s\",\"%s\",%d,%s%n",
                    c.getCustomerId(), c.getFirstName(), c.getLastName(),
                    c.getEmail(), c.getStoreId(), c.isActive());
            }
            System.out.println("[CSV] Exportado: " + path + " (" + customers.size() + " registros)");
        } catch (IOException e) {
            System.err.println("[CSV] Error: " + e.getMessage());
        }
    }

    // ── Exportar JSON ─────────────────────────────────────────────────

    /**
     * Exporta todas las películas a JSON.
     * @param path Ruta del archivo de salida
     */
    public void exportFilmsJSON(String path) {
        List<Film> films = filmDAO.get();
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("[");
            for (int i = 0; i < films.size(); i++) {
                Film f = films.get(i);
                pw.printf(
                    "  {\"film_id\":%d,\"title\":\"%s\",\"release_year\":%d," +
                    "\"rating\":\"%s\",\"rental_rate\":%.2f,\"length\":%d}%s%n",
                    f.getFilmId(), escapeJson(f.getTitle()), f.getReleaseYear(),
                    f.getRating(), f.getRentalRate(), f.getLength(),
                    i < films.size() - 1 ? "," : "");
            }
            pw.println("]");
            System.out.println("[JSON] Exportado: " + path + " (" + films.size() + " registros)");
        } catch (IOException e) {
            System.err.println("[JSON] Error: " + e.getMessage());
        }
    }

    /** Escapa caracteres especiales para JSON */
    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\"");
    }

    /** Cierra todas las conexiones */
    public void close() {
        filmDAO.closeConnection();
        actorDAO.closeConnection();
        rentalDAO.closeConnection();
        customerDAO.closeConnection();
    }
}
