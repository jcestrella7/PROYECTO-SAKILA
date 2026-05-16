package com.sakila.utils;

import java.util.regex.Pattern;

/**
 * Clase utilitaria con validaciones mediante expresiones regulares.
 * Cédulas, teléfonos, emails, fechas, etc.
 *
 * @author Estudiante INF514-Z06
 * @version 1.0
 */
public class Validator {

    // ── Patrones de expresiones regulares ─────────────────────────────
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^(\\+?1[-. ]?)?(\\(?\\d{3}\\)?[-. ]?\\d{3}[-. ]?\\d{4})$");

    private static final Pattern DATE_PATTERN =
        Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");

    private static final Pattern CEDULA_RD_PATTERN =
        Pattern.compile("^\\d{3}-\\d{7}-\\d{1}$");

    private static final Pattern SSN_PATTERN =
        Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");

    private static final Pattern YEAR_PATTERN =
        Pattern.compile("^(19|20)\\d{2}$");

    // Constructor privado - clase utilitaria
    private Validator() {}

    /**
     * Valida una dirección de correo electrónico.
     * @param email Email a validar
     * @return true si es válido
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida un número de teléfono (formato US/RD).
     * @param phone Teléfono a validar
     * @return true si es válido
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Valida una fecha en formato YYYY-MM-DD.
     * @param date Fecha a validar
     * @return true si el formato es correcto
     */
    public static boolean isValidDate(String date) {
        return date != null && DATE_PATTERN.matcher(date).matches();
    }

    /**
     * Valida una cédula dominicana (XXX-XXXXXXX-X).
     * @param cedula Cédula a validar
     * @return true si es válida
     */
    public static boolean isValidCedulaRD(String cedula) {
        return cedula != null && CEDULA_RD_PATTERN.matcher(cedula).matches();
    }

    /**
     * Valida un SSN americano (XXX-XX-XXXX).
     * @param ssn SSN a validar
     * @return true si es válido
     */
    public static boolean isValidSSN(String ssn) {
        return ssn != null && SSN_PATTERN.matcher(ssn).matches();
    }

    /**
     * Valida un año entre 1900 y 2099.
     * @param year Año como string
     * @return true si es válido
     */
    public static boolean isValidYear(String year) {
        return year != null && YEAR_PATTERN.matcher(year).matches();
    }

    /**
     * Valida que un texto no esté vacío.
     * @param text Texto a validar
     * @return true si no es null ni vacío
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
