package com.sakila.controllers;

import com.sakila.data.film.FilmRepository;
import com.sakila.models.Film;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Arrays;


/**
 * Controlador para la gestión de películas.
 * Extiende BaseController y proporciona la lógica específica para Film.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class FilmController extends BaseController<Film, Integer> {

    private final FilmRepository filmRepository;

    public FilmController(Scanner scanner) {
        super(new FilmRepository(), scanner);
        this.filmRepository = (FilmRepository) repository;
    }

    @Override
    protected String getEntityNameSingular() {
        return "película";
    }

    @Override
    protected String getEntityNamePlural() {
        return "películas";
    }

    @Override
    protected void displayEntity(Film film) {
        System.out.println("  ID: " + film.getFilmId());
        System.out.println("  Título: " + film.getTitle());
        System.out.println("  Descripción: " + film.getDescription());
        System.out.println("  Año de Lanzamiento: " + (film.getReleaseYear() != null ? film.getReleaseYear() : "N/A"));
        System.out.println("  ID de Idioma: " + (film.getLanguageId() != null ? film.getLanguageId() : "N/A"));
        System.out.println("  ID de Idioma Original: " + (film.getOriginalLanguageId() != null ? film.getOriginalLanguageId() : "N/A (Ninguno)"));
        System.out.println("  Duración de Alquiler (días): " + (film.getRentalDuration() != null ? film.getRentalDuration() : "N/A"));
        System.out.println("  Tasa de Alquiler: " + film.getRentalRate());
        System.out.println("  Longitud (minutos): " + (film.getLength() != null ? film.getLength() : "N/A"));
        System.out.println("  Costo de Reemplazo: " + film.getReplacementCost());
        System.out.println("  Clasificación: " + (film.getRating() != null ? film.getRating() : "N/A"));
        System.out.println("  Características Especiales: " + (film.getSpecialFeatures() != null ? film.getSpecialFeatures() : "N/A"));
        System.out.println("  Última Actualización: " + film.getLastUpdate());
        System.out.println("----------------------------------------");
    }

    @Override
    public void handleCreate() {
        System.out.println("\n--- Crear Nueva Película ---");
        Film newFilm = new Film();

        // Título
        System.out.print("Título: ");
        newFilm.setTitle(scanner.nextLine().trim());

        // Descripción
        System.out.print("Descripción: ");
        newFilm.setDescription(scanner.nextLine().trim());

        // Año de Lanzamiento
        System.out.print("Año de Lanzamiento (opcional): ");
        String releaseYearStr = scanner.nextLine().trim();
        if (!releaseYearStr.isEmpty()) {
            newFilm.setReleaseYear(Integer.parseInt(releaseYearStr));
        }

        // ID de Idioma (REQUERIDO)
        System.out.print("ID de Idioma (requerido): ");
        String langIdStr = scanner.nextLine().trim();
        if (langIdStr.isEmpty()) {
            System.out.println("❌ ID de Idioma requerido!");
            return;
        }
        newFilm.setLanguageId(Integer.parseInt(langIdStr));

        // Duración de Alquiler
        System.out.print("Duración de Alquiler (días, opcional): ");
        String rentalDurationStr = scanner.nextLine().trim();
        if (!rentalDurationStr.isEmpty()) {
            newFilm.setRentalDuration(Integer.parseInt(rentalDurationStr));
        }

        // Tasa de Alquiler
        System.out.print("Tasa de Alquiler (ej. 4.99): ");
        newFilm.setRentalRate(new BigDecimal(scanner.nextLine().trim()));

        // Longitud
        System.out.print("Longitud (minutos, opcional): ");
        String lengthStr = scanner.nextLine().trim();
        if (!lengthStr.isEmpty()) {
            newFilm.setLength(Integer.parseInt(lengthStr));
        }

        // Costo de Reemplazo
        System.out.print("Costo de Reemplazo (ej. 19.99): ");
        newFilm.setReplacementCost(new BigDecimal(scanner.nextLine().trim()));

        // Clasificación
        System.out.print("Clasificación (G,PG,PG-13,R,NC-17, opcional): ");
        String ratingStr = scanner.nextLine().trim();
        if (!ratingStr.isEmpty()) {
            newFilm.setRating(ratingStr.toUpperCase());
        }

        // ✅ Características Especiales (SOLO UNA VEZ, CON VALIDACIÓN)
        System.out.print("Características Especiales (Trailers,Commentaries, etc., opcional): ");
        String specialFeaturesStr = scanner.nextLine().trim();
        if (!specialFeaturesStr.isEmpty()) {
            String[] validFeatures = {"Trailers", "Commentaries", "Deleted Scenes", "Behind the Scenes"};
            StringBuilder valid = new StringBuilder();
            for (String feature : specialFeaturesStr.split(",")) {
                String clean = feature.trim();
                if (clean.length() > 0 && Arrays.asList(validFeatures).contains(clean)) {
                    if (valid.length() > 0) valid.append(",");
                    valid.append(clean);
                }
            }
            newFilm.setSpecialFeatures(valid.length() > 0 ? valid.toString() : null);
        }

        try {
            Film createdFilm = filmRepository.save(newFilm);
            System.out.println("✅ Película creada con éxito. ID: " + createdFilm.getFilmId());
        } catch (Exception e) {
            System.err.println("❌ Error al crear película: " + e.getMessage());
        }
    }

    @Override
    public void handleUpdate() {
        System.out.println("\n--- Actualizar Película ---");
        System.out.print("Ingrese el ID de la película a actualizar: ");
        int filmId = Integer.parseInt(scanner.nextLine());

        Optional<Film> existingFilmOpt = filmRepository.findById(filmId);
        if (existingFilmOpt.isPresent()) {
            Film existingFilm = existingFilmOpt.get();
            System.out.println("Datos actuales de la película:");
            displayEntity(existingFilm);

            System.out.print("Nuevo Título (" + existingFilm.getTitle() + "): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) existingFilm.setTitle(title);

            System.out.print("Nueva Descripción (" + existingFilm.getDescription() + "): ");
            String description = scanner.nextLine();
            if (!description.isEmpty()) existingFilm.setDescription(description);

            System.out.print("Nuevo Año de Lanzamiento (" + (existingFilm.getReleaseYear() != null ? existingFilm.getReleaseYear() : "Ninguno") + ", opcional): ");
            String releaseYearStr = scanner.nextLine();
            if (!releaseYearStr.isEmpty()) {
                existingFilm.setReleaseYear(Integer.parseInt(releaseYearStr));
            } else {
                existingFilm.setReleaseYear(null);
            }

            System.out.print("Nuevo ID de Idioma (" + (existingFilm.getLanguageId() != null ? existingFilm.getLanguageId() : "Ninguno") + "): ");
            String languageIdStr = scanner.nextLine();
            if (!languageIdStr.isEmpty()) {
                existingFilm.setLanguageId(Integer.parseInt(languageIdStr));
            } else {
                // Si se deja en blanco, y el campo es requerido, esto podría causar problemas al guardar.
                // Aquí asumimos que language_id NO se puede dejar nulo.
                // Puedes optar por mantener el valor anterior o lanzar un error si es crítico.
                System.out.println("El ID de Idioma no puede ser vacío. Se mantendrá el valor anterior.");
            }

            System.out.print("Nuevo ID de Idioma Original (" + (existingFilm.getOriginalLanguageId() != null ? existingFilm.getOriginalLanguageId() : "Ninguno") + ", opcional): ");
            String originalLanguageIdStr = scanner.nextLine();
            if (!originalLanguageIdStr.isEmpty()) {
                existingFilm.setOriginalLanguageId(Integer.parseInt(originalLanguageIdStr));
            } else {
                existingFilm.setOriginalLanguageId(null);
            }

            System.out.print("Nueva Duración de Alquiler en días (" + (existingFilm.getRentalDuration() != null ? existingFilm.getRentalDuration() : "Ninguno") + ", opcional): ");
            String rentalDurationStr = scanner.nextLine();
            if (!rentalDurationStr.isEmpty()) {
                existingFilm.setRentalDuration(Integer.parseInt(rentalDurationStr));
            } else {
                existingFilm.setRentalDuration(null);
            }

            System.out.print("Nueva Tasa de Alquiler (" + existingFilm.getRentalRate() + "): ");
            String rentalRateStr = scanner.nextLine();
            if (!rentalRateStr.isEmpty()) existingFilm.setRentalRate(new BigDecimal(rentalRateStr));

            System.out.print("Nueva Longitud en minutos (" + (existingFilm.getLength() != null ? existingFilm.getLength() : "Ninguno") + ", opcional): ");
            String lengthStr = scanner.nextLine();
            if (!lengthStr.isEmpty()) {
                existingFilm.setLength(Integer.parseInt(lengthStr));
            } else {
                existingFilm.setLength(null);
            }

            System.out.print("Nuevo Costo de Reemplazo (" + existingFilm.getReplacementCost() + "): ");
            String replacementCostStr = scanner.nextLine();
            if (!replacementCostStr.isEmpty()) existingFilm.setReplacementCost(new BigDecimal(replacementCostStr));

            System.out.print("Nueva Clasificación (" + (existingFilm.getRating() != null ? existingFilm.getRating() : "Ninguno") + ", opcional): ");
            String ratingStr = scanner.nextLine();
            if (!ratingStr.isEmpty()) {
                existingFilm.setRating(ratingStr);
            } else {
                existingFilm.setRating(null);
            }

            System.out.print("Nuevas Características Especiales (" + (existingFilm.getSpecialFeatures() != null ? existingFilm.getSpecialFeatures() : "Ninguno") + ", opcional): ");
            String specialFeaturesStr = scanner.nextLine();
            if (!specialFeaturesStr.isEmpty()) {
                existingFilm.setSpecialFeatures(specialFeaturesStr);
            } else {
                existingFilm.setSpecialFeatures(null);
            }

            try {
                Film updatedFilm = filmRepository.update(existingFilm);
                System.out.println("Película con ID " + updatedFilm.getFilmId() + " actualizada con éxito.");
            } catch (RuntimeException e) {
                System.err.println("Error al actualizar la película: " + e.getMessage());
            }

        } else {
            System.out.println("No se encontró ninguna película con el ID " + filmId + ".");
        }
    }

    @Override
    public void handleDelete() {
        System.out.println("\n--- Eliminar Película ---");
        System.out.print("Ingrese el ID de la película a eliminar: ");
        int filmId = Integer.parseInt(scanner.nextLine());

        Optional<Film> filmToDelete = filmRepository.findById(filmId);
        if (filmToDelete.isPresent()) {
            System.out.print("¿Está seguro de que desea eliminar la película '" + filmToDelete.get().getTitle() + "' (S/N)? ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("S")) {
                try {
                    filmRepository.deleteById(filmId);
                    System.out.println("Película con ID " + filmId + " eliminada con éxito.");
                } catch (RuntimeException e) {
                    System.err.println("Error al eliminar la película: " + e.getMessage());
                }
            } else {
                System.out.println("Operación de eliminación cancelada.");
            }
        } else {
            System.out.println("No se encontró ninguna película con el ID " + filmId + ".");
        }
    }

    @Override
    public void handleSearch() {
        System.out.println("\n--- Buscar Películas ---");
        System.out.println("Opciones de búsqueda:");
        System.out.println("1. Por título (contiene)");
        System.out.println("2. Por año de lanzamiento");
        System.out.print("Seleccione una opción: ");
        String searchOption = scanner.nextLine();
        List<Film> results = null;

        switch (searchOption) {
            case "1":
                System.out.print("Ingrese parte del título a buscar: ");
                String titlePart = scanner.nextLine();
                results = filmRepository.search("title LIKE ?", "%" + titlePart + "%");
                break;
            case "2":
                System.out.print("Ingrese el año de lanzamiento: ");
                int releaseYear = Integer.parseInt(scanner.nextLine());
                results = filmRepository.search("release_year = ?", releaseYear);
                break;
            default:
                System.out.println("Opción de búsqueda no válida.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("\n--- Resultados de la Búsqueda ---");
            results.forEach(this::displayEntity);
        } else {
            System.out.println("No se encontraron películas que coincidan con los criterios de búsqueda.");
        }
    }
}