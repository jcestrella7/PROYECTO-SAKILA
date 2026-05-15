package com.sakila.data.actor;

import com.sakila.data.DataContext;
import com.sakila.models.Actor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repositorio concreto para la entidad Actor.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class ActorRepository extends DataContext<Actor, Integer> {

    @Override
    protected String getTableName() {
        return "actor";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "actor_id";
    }

    @Override
    protected Integer getPrimaryKeyValue(Actor entity) {
        return entity.getActorId();
    }

    @Override
    protected void setPrimaryKeyValue(Actor entity, Integer id) {
        entity.setActorId(id);
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO actor (first_name, last_name, last_update) VALUES (?, ?, NOW())";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE actor SET first_name = ?, last_name = ?, last_update = NOW() WHERE actor_id = ?";
    }

    @Override
    protected void setParameters(PreparedStatement ps, Actor actor, boolean isUpdate) throws SQLException {
        int i = 1;
        ps.setString(i++, actor.getFirstName());
        ps.setString(i++, actor.getLastName());
        // last_update se maneja con NOW() en el SQL, no requiere parámetro aquí
        // actor_id para UPDATE se añade en DataContext, no aquí
    }

    @Override
    protected Actor mapResultSetToEntity(ResultSet rs) throws SQLException {
        Actor actor = new Actor();
        actor.setActorId(rs.getInt("actor_id"));
        actor.setFirstName(rs.getString("first_name"));
        actor.setLastName(rs.getString("last_name"));
        actor.setLastUpdate(rs.getTimestamp("last_update"));
        return actor;
    }

    @Override
    protected int getParameterCount(Actor actor, boolean isUpdate) {
        // En INSERT: first_name, last_name (2 parámetros)
        // En UPDATE: first_name, last_name (2 parámetros)
        // El actor_id para el WHERE en UPDATE se añade por DataContext
        return 2;
    }
}