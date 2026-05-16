package com.sakila.data;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD en una base de datos.
 * Proporciona métodos para crear, leer, actualizar y eliminar entidades.
 *
 * @param <T>  El tipo de entidad con la que operará esta interfaz.
 * @param <ID> El tipo del identificador único (clave primaria) de la entidad.
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public interface IDatapostCrudRepository<T, ID> {

    /**
     * Guarda una entidad en la base de datos (inserta o actualiza).
     * @param entity La entidad a guardar.
     * @return La entidad guardada, posiblemente con un ID generado si es nueva.
     */
    T save(T entity);

    /**
     * Busca una entidad por su identificador único.
     * @param id El identificador único de la entidad.
     * @return Un Optional que contiene la entidad si se encuentra, o vacío si no.
     */
    Optional<T> findById(ID id);

    /**
     * Recupera todas las entidades de un tipo específico.
     * @return Una lista de todas las entidades.
     */
    List<T> findAll();

    /**
     * Elimina una entidad de la base de datos por su identificador único.
     * @param id El identificador único de la entidad a eliminar.
     */
    void deleteById(ID id);

    /**
     * Elimina una entidad de la base de datos.
     * @param entity La entidad a eliminar.
     */
    void delete(T entity); // <-- Esta línea es crucial

}