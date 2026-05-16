package com.sakila.models;

/**
 * Modelo que representa la tabla customer de sakila.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class Customer {

    private int customerId;
    private int storeId;
    private String firstName;
    private String lastName;
    private String email;
    private int addressId;
    private boolean active;

    public Customer() {}

    public Customer(int customerId, int storeId, String firstName,
                    String lastName, String email, int addressId, boolean active) {
        this.customerId = customerId;
        this.storeId    = storeId;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
        this.addressId  = addressId;
        this.active     = active;
    }

    public int getCustomerId()             { return customerId; }
    public void setCustomerId(int id)      { this.customerId = id; }

    public int getStoreId()                { return storeId; }
    public void setStoreId(int id)         { this.storeId = id; }

    public String getFirstName()           { return firstName; }
    public void setFirstName(String n)     { this.firstName = n; }

    public String getLastName()            { return lastName; }
    public void setLastName(String n)      { this.lastName = n; }

    public String getEmail()               { return email; }
    public void setEmail(String e)         { this.email = e; }

    public int getAddressId()              { return addressId; }
    public void setAddressId(int id)       { this.addressId = id; }

    public boolean isActive()              { return active; }
    public void setActive(boolean a)       { this.active = a; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s | %s | Tienda:%d | Activo:%s",
                customerId, firstName, lastName, email, storeId, active ? "Sí" : "No");
    }
}
