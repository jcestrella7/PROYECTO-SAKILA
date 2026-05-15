package com.sakila.data;

import java.util.List;

/**
 * Interface estándar CRUD para la base de datos Sakila.
 * Define los métodos básicos de acceso a datos.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 * @since 2021
 */
public interface IDatapost<T> {

    /**
     * Inserta un nuevo registro en la base de datos.
     * @param entity Objeto a insertar
     * @return true si se insertó correctamente
     */
    boolean post(T entity);

    /**
     * Actualiza un registro existente en la base de datos.
     * @param entity Objeto con los datos actualizados
     * @return true si se actualizó correctamente
     */
    boolean put(T entity);

    /**
     * Elimina un registro de la base de datos.
     * @param id Identificador del registro a eliminar
     * @return true si se eliminó correctamente
     */
    boolean delete(int id);

    /**
     * Obtiene todos los registros de la tabla.
     * @return Lista con todos los registros
     */
    List<T> get();

    /**
     * Obtiene un registro por su ID.
     * @param id Identificador del registro
     * @return Objeto encontrado o null
     */
    T getById(int id);
}
