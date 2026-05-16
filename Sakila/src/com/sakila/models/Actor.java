package com.sakila.models;

/**
 * Modelo que representa la tabla actor de sakila.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class Actor {

    private int actorId;
    private String firstName;
    private String lastName;

    public Actor() {}

    public Actor(int actorId, String firstName, String lastName) {
        this.actorId   = actorId;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    public int getActorId()               { return actorId; }
    public void setActorId(int id)        { this.actorId = id; }

    public String getFirstName()          { return firstName; }
    public void setFirstName(String n)    { this.firstName = n; }

    public String getLastName()           { return lastName; }
    public void setLastName(String n)     { this.lastName = n; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s", actorId, firstName, lastName);
    }
}
