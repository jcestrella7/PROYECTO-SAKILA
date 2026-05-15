package com.sakila.models;

import java.sql.Timestamp;

/**
 * Representa un actor en el sistema Sakila.
 * Este es un POJO (Plain Old Java Object) que mapea a la tabla 'actor'.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class Actor {
    private int actorId;
    private String firstName;
    private String lastName;
    private Timestamp lastUpdate;

    // Constructor vacío
    public Actor() {
    }

    // Constructor completo
    public Actor(int actorId, String firstName, String lastName, Timestamp lastUpdate) {
        this.actorId = actorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdate = lastUpdate;
    }

    // --- Getters y Setters ---

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "actorId=" + actorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}