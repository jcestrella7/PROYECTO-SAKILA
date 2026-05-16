package com.sakila.data;

import com.sakila.models.Film;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto y final para la tabla film.
 * Extiende DataContext e implementa todas las operaciones CRUD.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public final class FilmDAO extends DataContext<Film> {

    // ── Implementación de métodos abstractos ──────────────────────────

    @Override
    protected Film mapRow(ResultSet rs) throws SQLException {
        return new Film(
            rs.getInt("film_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("release_year"),
            rs.getInt("language_id"),
            rs.getInt("rental_duration"),
            rs.getBigDecimal("rental_rate"),
            rs.getInt("length"),
            rs.getBigDecimal("replacement_cost"),
            rs.getString("rating")
        );
    }

    @Override
    protected String getTableName()  { return "film"; }

    @Override
    protected String getPrimaryKey() { return "film_id"; }

    // ── CRUD ──────────────────────────────────────────────────────────

    /**
     * Inserta una nueva película. El film_id es autoincrement.
     * @param film Objeto Film a insertar
     * @return true si se insertó correctamente
     */
    @Override
    public boolean post(Film film) {
        String sql = "INSERT INTO film (title, description, release_year, language_id, " +
                     "rental_duration, rental_rate, length, replacement_cost, rating) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int id = executeInsertGetKey(sql,
                film.getTitle(), film.getDescription(), film.getReleaseYear(),
                film.getLanguageId(), film.getRentalDuration(), film.getRentalRate(),
                film.getLength(), film.getReplacementCost(), film.getRating());
        if (id > 0) { film.setFilmId(id); return true; }
        return false;
    }

    /**
     * Actualiza una película existente.
     * @param film Objeto Film con datos actualizados
     * @return true si se actualizó correctamente
     */
    @Override
    public boolean put(Film film) {
        String sql = "UPDATE film SET title=?, description=?, release_year=?, " +
                     "language_id=?, rental_duration=?, rental_rate=?, " +
                     "length=?, replacement_cost=?, rating=? WHERE film_id=?";
        return executeUpdate(sql,
                film.getTitle(), film.getDescription(), film.getReleaseYear(),
                film.getLanguageId(), film.getRentalDuration(), film.getRentalRate(),
                film.getLength(), film.getReplacementCost(), film.getRating(),
                film.getFilmId());
    }

    /**
     * Elimina una película por ID.
     * @param id film_id a eliminar
     * @return true si se eliminó correctamente
     */
    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM film WHERE film_id=?", id);
    }

    /**
     * Obtiene todas las películas.
     * @return Lista de Film
     */
    @Override
    public List<Film> get() {
        List<Film> list = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT * FROM film ORDER BY title");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[FilmDAO] get() error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Obtiene una película por su ID.
     * @param id film_id
     * @return Film encontrado o null
     */
    @Override
    public Film getById(int id) {
        ResultSet rs = executeQuery("SELECT * FROM film WHERE film_id=?", id);
        try {
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[FilmDAO] getById() error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca películas por título (búsqueda parcial).
     * @param title Texto a buscar en el título
     * @return Lista de Film que coinciden
     */
    public List<Film> getByTitle(String title) {
        List<Film> list = new ArrayList<>();
        ResultSet rs = executeQuery(
            "SELECT * FROM film WHERE title LIKE ? ORDER BY title",
            "%" + title + "%");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[FilmDAO] getByTitle() error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Obtiene películas por rating (G, PG, PG-13, R, NC-17).
     * @param rating Clasificación
     * @return Lista de Film con ese rating
     */
    public List<Film> getByRating(String rating) {
        List<Film> list = new ArrayList<>();
        ResultSet rs = executeQuery(
            "SELECT * FROM film WHERE rating=? ORDER BY title", rating);
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[FilmDAO] getByRating() error: " + e.getMessage());
        }
        return list;
    }
}
