package com.sakila; // Ajusta el paquete si MainApp está directamente en src/, pero es buena práctica tenerlo en com.sakila

import com.sakila.ui.ConsoleUI;
import java.util.Scanner;

/**
 * Clase principal que arranca la aplicación de gestión de Sakila en consola.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class MainApp {


    public static void main(String[] args) {
        System.out.println("Iniciando aplicación Sakila...");
        Scanner scanner = new Scanner(System.in);
        ConsoleUI ui = new ConsoleUI(scanner);
        ui.start();
        scanner.close(); // Cerrar el scanner al finalizar la aplicación
        System.out.println("Aplicación finalizada.");
    }
}