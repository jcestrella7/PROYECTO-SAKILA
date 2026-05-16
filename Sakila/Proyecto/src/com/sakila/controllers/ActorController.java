package com.sakila.controllers;

import com.sakila.data.actor.ActorRepository;
import com.sakila.models.Actor;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;

/**
 * Controlador para la gestión de actores.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class ActorController extends BaseController<Actor, Integer> {

    private final ActorRepository actorRepository;

    public ActorController(Scanner scanner) {
        super(new ActorRepository(), scanner);
        this.actorRepository = (ActorRepository) repository;
    }

    @Override
    protected String getEntityNameSingular() {
        return "actor";
    }

    @Override
    protected String getEntityNamePlural() {
        return "actores";
    }

    @Override
    protected void displayEntity(Actor actor) {
        System.out.println("  ID: " + actor.getActorId());
        System.out.println("  Nombre: " + actor.getFirstName() + " " + actor.getLastName());
        System.out.println("  Última Actualización: " + actor.getLastUpdate());
        System.out.println("----------------------------------------");
    }

    @Override
    public void handleCreate() {
        System.out.println("\n--- Registrar Nuevo Actor ---");
        Actor newActor = new Actor();

        System.out.print("Nombre: ");
        newActor.setFirstName(scanner.nextLine());

        System.out.print("Apellido: ");
        newActor.setLastName(scanner.nextLine());

        try {
            Actor createdActor = actorRepository.save(newActor);
            System.out.println("Actor registrado con éxito. ID: " + createdActor.getActorId());
        } catch (RuntimeException e) {
            System.err.println("Error al registrar el actor: " + e.getMessage());
        }
    }

    @Override
    public void handleUpdate() {
        System.out.println("\n--- Actualizar Actor ---");
        System.out.print("Ingrese el ID del actor a actualizar: ");
        int actorId = Integer.parseInt(scanner.nextLine());

        Optional<Actor> existingActorOpt = actorRepository.findById(actorId);
        if (existingActorOpt.isPresent()) {
            Actor existingActor = existingActorOpt.get();
            System.out.println("Datos actuales del actor:");
            displayEntity(existingActor);

            System.out.print("Nuevo Nombre (" + existingActor.getFirstName() + "): ");
            String firstName = scanner.nextLine();
            if (!firstName.isEmpty()) existingActor.setFirstName(firstName);

            System.out.print("Nuevo Apellido (" + existingActor.getLastName() + "): ");
            String lastName = scanner.nextLine();
            if (!lastName.isEmpty()) existingActor.setLastName(lastName);

            try {
                Actor updatedActor = actorRepository.update(existingActor);
                System.out.println("Actor con ID " + updatedActor.getActorId() + " actualizado con éxito.");
            } catch (RuntimeException e) {
                System.err.println("Error al actualizar el actor: " + e.getMessage());
            }

        } else {
            System.out.println("No se encontró ningún actor con el ID " + actorId + ".");
        }
    }

    @Override
    public void handleDelete() {
        System.out.println("\n--- Eliminar Actor ---");
        System.out.print("Ingrese el ID del actor a eliminar: ");
        int actorId = Integer.parseInt(scanner.nextLine());

        Optional<Actor> actorToDelete = actorRepository.findById(actorId);
        if (actorToDelete.isPresent()) {
            System.out.print("¿Está seguro de que desea eliminar al actor '" + actorToDelete.get().getFirstName() + " " + actorToDelete.get().getLastName() + "' (S/N)? ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("S")) {
                try {
                    actorRepository.deleteById(actorId);
                    System.out.println("Actor con ID " + actorId + " eliminado con éxito.");
                } catch (RuntimeException e) {
                    System.err.println("Error al eliminar el actor: " + e.getMessage());
                }
            } else {
                System.out.println("Operación de eliminación cancelada.");
            }
        } else {
            System.out.println("No se encontró ningún actor con el ID " + actorId + ".");
        }
    }

    @Override
    public void handleSearch() {
        System.out.println("\n--- Buscar Actores ---");
        System.out.println("Opciones de búsqueda:");
        System.out.println("1. Por nombre (contiene)");
        System.out.print("Seleccione una opción: ");
        String searchOption = scanner.nextLine();
        List<Actor> results = null;

        switch (searchOption) {
            case "1":
                System.out.print("Ingrese parte del nombre o apellido a buscar: ");
                String namePart = scanner.nextLine();
                results = actorRepository.search("(first_name LIKE ? OR last_name LIKE ?)", "%" + namePart + "%", "%" + namePart + "%");
                break;
            default:
                System.out.println("Opción de búsqueda no válida.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("\n--- Resultados de la Búsqueda ---");
            results.forEach(this::displayEntity);
        } else {
            System.out.println("No se encontraron actores que coincidan con los criterios de búsqueda.");
        }
    }
}