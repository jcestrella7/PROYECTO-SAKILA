package com.sakila.data;

import com.sakila.models.Actor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto y final para la tabla actor.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public final class ActorDAO extends DataContext<Actor> {

    @Override
    protected Actor mapRow(ResultSet rs) throws SQLException {
        return new Actor(
            rs.getInt("actor_id"),
            rs.getString("first_name"),
            rs.getString("last_name")
        );
    }

    @Override protected String getTableName()  { return "actor"; }
    @Override protected String getPrimaryKey() { return "actor_id"; }

    @Override
    public boolean post(Actor actor) {
        String sql = "INSERT INTO actor (first_name, last_name) VALUES (?, ?)";
        int id = executeInsertGetKey(sql, actor.getFirstName(), actor.getLastName());
        if (id > 0) { actor.setActorId(id); return true; }
        return false;
    }

    @Override
    public boolean put(Actor actor) {
        return executeUpdate(
            "UPDATE actor SET first_name=?, last_name=? WHERE actor_id=?",
            actor.getFirstName(), actor.getLastName(), actor.getActorId());
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate("DELETE FROM actor WHERE actor_id=?", id);
    }

    @Override
    public List<Actor> get() {
        List<Actor> list = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT * FROM actor ORDER BY last_name");
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ActorDAO] get() error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Actor getById(int id) {
        ResultSet rs = executeQuery("SELECT * FROM actor WHERE actor_id=?", id);
        try {
            if (rs != null && rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[ActorDAO] getById() error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene los actores de una película específica.
     * @param filmId ID de la película
     * @return Lista de actores en esa película
     */
    public List<Actor> getByFilm(int filmId) {
        List<Actor> list = new ArrayList<>();
        String sql = "SELECT a.* FROM actor a " +
                     "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                     "WHERE fa.film_id=? ORDER BY a.last_name";
        ResultSet rs = executeQuery(sql, filmId);
        try {
            if (rs != null) while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ActorDAO] getByFilm() error: " + e.getMessage());
        }
        return list;
    }
}
