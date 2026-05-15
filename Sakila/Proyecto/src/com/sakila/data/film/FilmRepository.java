package com.sakila.data.film;

import com.sakila.data.DataContext;
import com.sakila.models.Film;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types; // Importa Types para manejar NULL

/**
 * Repositorio concreto para la entidad Film.
 *
 * @author Dola
 * @version 1.0
 * @since 2026-05-13
 */
public class FilmRepository extends DataContext<Film, Integer> {

    @Override
    protected String getTableName() {
        return "film";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "film_id";
    }

    @Override
    protected Integer getPrimaryKeyValue(Film entity) {
        return entity.getFilmId();
    }

    @Override
    protected void setPrimaryKeyValue(Film entity, Integer id) {
        entity.setFilmId(id);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO film (title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features, last_update) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE film SET title = ?, description = ?, release_year = ?, language_id = ?, original_language_id = ?, rental_duration = ?, rental_rate = ?, length = ?, replacement_cost = ?, rating = ?, special_features = ?, last_update = NOW() " +
                "WHERE film_id = ?";
    }

    @Override
    protected void setParameters(PreparedStatement ps, Film film, boolean isUpdate) throws SQLException {
        int i = 1;
        ps.setString(i++, film.getTitle());
        ps.setString(i++, film.getDescription());

        // Manejo de Integer que pueden ser NULL en la BD
        if (film.getReleaseYear() != null) {
            ps.setInt(i++, film.getReleaseYear());
        } else {
            ps.setNull(i++, Types.SMALLINT); // release_year es SMALLINT en Sakila
        }

        if (film.getLanguageId() != null) {
            ps.setInt(i++, film.getLanguageId());
        } else {
            ps.setNull(i++, Types.TINYINT); // language_id es TINYINT en Sakila
        }

        if (film.getOriginalLanguageId() != null) {
            ps.setInt(i++, film.getOriginalLanguageId());
        } else {
            ps.setNull(i++, Types.TINYINT); // original_language_id es TINYINT en Sakila
        }

        if (film.getRentalDuration() != null) {
            ps.setInt(i++, film.getRentalDuration());
        } else {
            ps.setNull(i++, Types.TINYINT); // rental_duration es TINYINT en Sakila
        }

        ps.setBigDecimal(i++, film.getRentalRate());

        if (film.getLength() != null) {
            ps.setInt(i++, film.getLength());
        } else {
            ps.setNull(i++, Types.SMALLINT); // length es SMALLINT en Sakila
        }

        ps.setBigDecimal(i++, film.getReplacementCost());

        // Manejo de String que pueden ser NULL en la BD
        if (film.getRating() != null) {
            ps.setString(i++, film.getRating());
        } else {
            ps.setNull(i++, Types.VARCHAR);
        }

        if (film.getSpecialFeatures() != null) {
            ps.setString(i++, film.getSpecialFeatures());
        } else {
            ps.setNull(i++, Types.VARCHAR);
        }
        // last_update se maneja con NOW() en el SQL, no requiere parámetro aquí
        // film_id para UPDATE se añade en DataContext, no aquí
    }

    @Override
    protected Film mapResultSetToEntity(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getInt("film_id"));
        film.setTitle(rs.getString("title"));
        film.setDescription(rs.getString("description"));

        // Usar getObject(columna, Tipo.class) para manejar valores NULL para Integer
        film.setReleaseYear(rs.getObject("release_year", Integer.class));
        film.setLanguageId(rs.getObject("language_id", Integer.class));
        film.setOriginalLanguageId(rs.getObject("original_language_id", Integer.class));
        film.setRentalDuration(rs.getObject("rental_duration", Integer.class));
        film.setRentalRate(rs.getBigDecimal("rental_rate"));
        film.setLength(rs.getObject("length", Integer.class));
        film.setReplacementCost(rs.getBigDecimal("replacement_cost"));
        film.setRating(rs.getString("rating"));
        film.setSpecialFeatures(rs.getString("special_features"));
        film.setLastUpdate(rs.getTimestamp("last_update"));
        return film;
    }

    @Override
    protected int getParameterCount(Film film, boolean isUpdate) {
        // En INSERT/UPDATE: title, description, release_year, language_id, original_language_id,
        // rental_duration, rental_rate, length, replacement_cost, rating, special_features
        // Son 11 parámetros
        // El film_id para el WHERE en UPDATE se añade por DataContext
        return 11;
    }
}