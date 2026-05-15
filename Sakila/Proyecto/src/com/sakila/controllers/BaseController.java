package com.sakila.controllers;

import com.sakila.data.IDatapostCrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Clase base abstracta para controladores genéricos.
 * Proporciona funcionalidades comunes para gestionar entidades a través de la interfaz CRUD.
 *
 * @param <T> El tipo de entidad que este controlador gestionará.
 * @param <ID> El tipo del identificador único de la entidad.
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public abstract class BaseController<T, ID> {

    protected final IDatapostCrudRepository<T, ID> repository;
    protected final Scanner scanner;

    public BaseController(IDatapostCrudRepository<T, ID> repository, Scanner scanner) {
        this.repository = repository;
        this.scanner = scanner;
    }

    /**
     * Muestra todas las entidades.
     * @return Una lista de todas las entidades.
     */
    public List<T> listAll() {
        List<T> entities = repository.findAll();
        if (entities.isEmpty()) {
            System.out.println("No hay " + getEntityNamePlural() + " registrados.");
        } else {
            System.out.println("\n--- Listado de " + getEntityNamePlural() + " ---");
            entities.forEach(this::displayEntity);
        }
        return entities;
    }

    /**
     * Busca y muestra una entidad por su ID.
     * @param id El ID de la entidad a buscar.
     * @return Un Optional que contiene la entidad si se encuentra, o vacío si no.
     */
    public Optional<T> findAndDisplayById(ID id) {
        Optional<T> entity = repository.findById(id);
        if (entity.isPresent()) {
            System.out.println("\n--- Detalle de " + getEntityNameSingular() + " ---");
            displayEntity(entity.get());
        } else {
            System.out.println("No se encontró " + getEntityNameSingular() + " con ID: " + id);
        }
        return entity;
    }

    /**
     * Abstract method to get the singular name of the entity.
     * @return The singular name of the entity.
     */
    protected abstract String getEntityNameSingular();

    /**
     * Abstract method to get the plural name of the entity.
     * @return The plural name of the entity.
     */
    protected abstract String getEntityNamePlural();

    /**
     * Abstract method to display a single entity's details to the console.
     * @param entity The entity to display.
     */
    protected abstract void displayEntity(T entity);

    /**
     * Método para manejar la creación de una entidad.
     * La lógica de cómo pedir los datos y crear la entidad debe implementarse en la subclase.
     */
    public abstract void handleCreate();

    /**
     * Método para manejar la actualización de una entidad.
     * La lógica de cómo pedir los datos y actualizar la entidad debe implementarse en la subclase.
     */
    public abstract void handleUpdate();

    /**
     * Método para manejar la eliminación de una entidad por ID.
     * La lógica de cómo pedir el ID y eliminar debe implementarse en la subclase.
     */
    public abstract void handleDelete();

    /**
     * Método para manejar la búsqueda de entidades.
     * La lógica de cómo pedir los criterios de búsqueda debe implementarse en la subclase.
     */
    public abstract void handleSearch();
}