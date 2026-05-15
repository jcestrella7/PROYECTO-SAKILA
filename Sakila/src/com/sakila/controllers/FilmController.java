package com.sakila.controllers;

import com.sakila.data.FilmDAO;
import com.sakila.models.Film;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador MVC para la gestión de películas.
 * Coordina la lógica entre la vista (consola) y el modelo (FilmDAO).
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class FilmController {

    private final FilmDAO filmDAO;

    /** Constructor */
    public FilmController() {
        this.filmDAO = new FilmDAO();
    }

    /**
     * Lista todas las películas.
     * @return Lista de Film
     */
    public List<Film> listAll() {
        return filmDAO.get();
    }

    /**
     * Busca películas por título.
     * @param title Texto a buscar
     * @return Lista de coincidencias
     */
    public List<Film> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) return new ArrayList<>();
        return filmDAO.getByTitle(title.trim());
    }

    /**
     * Busca películas por rating.
     * @param rating Clasificación (G, PG, PG-13, R, NC-17)
     * @return Lista de películas con ese rating
     */
    public List<Film> searchByRating(String rating) {
        return filmDAO.getByRating(rating.toUpperCase());
    }

    /**
     * Obtiene una película por ID.
     * @param id film_id
     * @return Film o null
     */
    public Film findById(int id) {
        return filmDAO.getById(id);
    }

    /**
     * Crea una nueva película con validación de datos.
     * @return true si se creó correctamente
     */
    public boolean create(String title, String description, int year,
                          int langId, int duration, double rate,
                          int length, double cost, String rating) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("[FilmController] Error: El título no puede estar vacío.");
            return false;
        }
        Film f = new Film();
        f.setTitle(title.trim());
        f.setDescription(description);
        f.setReleaseYear(year);
        f.setLanguageId(langId);
        f.setRentalDuration(duration);
        f.setRentalRate(BigDecimal.valueOf(rate));
        f.setLength(length);
        f.setReplacementCost(BigDecimal.valueOf(cost));
        f.setRating(rating);
        return filmDAO.post(f);
    }

    /**
     * Actualiza una película existente.
     * @param film Film con datos actualizados
     * @return true si se actualizó
     */
    public boolean update(Film film) {
        if (film.getFilmId() <= 0) {
            System.out.println("[FilmController] Error: ID inválido.");
            return false;
        }
        return filmDAO.put(film);
    }

    /**
     * Elimina una película por ID.
     * @param id film_id
     * @return true si se eliminó
     */
    public boolean delete(int id) {
        if (filmDAO.getById(id) == null) {
            System.out.println("[FilmController] Error: Película no encontrada.");
            return false;
        }
        return filmDAO.delete(id);
    }

    /** Cierra la conexión del DAO */
    public void close() { filmDAO.closeConnection(); }
}
