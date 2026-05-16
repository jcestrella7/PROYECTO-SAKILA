package com.sakila.controllers;

import com.sakila.data.customer.CustomerRepository;
import com.sakila.models.Customer;
import java.sql.Timestamp; // Necesario para el campo createDate, lastUpdate
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controlador para la gestión de clientes.
 * Extiende BaseController y proporciona la lógica específica para Customer.
 *
 * @author JEAN
 * @version 1.0
 * @since 2026-05-13
 */
public class CustomerController extends BaseController<Customer, Integer> {

    private final CustomerRepository customerRepository;

    public CustomerController(Scanner scanner) {
        super(new CustomerRepository(), scanner); // Pasa la instancia de CustomerRepository al super constructor
        this.customerRepository = (CustomerRepository) repository; // Casting seguro
    }

    @Override
    protected String getEntityNameSingular() {
        return "cliente";
    }

    @Override
    protected String getEntityNamePlural() {
        return "clientes";
    }

    @Override
    protected void displayEntity(Customer customer) {
        System.out.println("  ID: " + customer.getCustomerId());
        System.out.println("  Nombre: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("  Email: " + customer.getEmail());
        System.out.println("  Tienda ID: " + customer.getStoreId());
        System.out.println("  Dirección ID: " + customer.getAddressId());
        System.out.println("  Activo: " + (customer.isActive() ? "Sí" : "No"));
        System.out.println("  Fecha de Creación: " + customer.getCreateDate());
        System.out.println("  Última Actualización: " + customer.getLastUpdate());
        System.out.println("----------------------------------------");
    }

    @Override
    public void handleCreate() {
        System.out.println("\n--- Registrar Nuevo Cliente ---");
        Customer newCustomer = new Customer();

        System.out.print("ID de la Tienda (ej. 1 o 2): ");
        newCustomer.setStoreId(Integer.parseInt(scanner.nextLine()));

        System.out.print("Nombre: ");
        newCustomer.setFirstName(scanner.nextLine());

        System.out.print("Apellido: ");
        newCustomer.setLastName(scanner.nextLine());

        System.out.print("Email: ");
        newCustomer.setEmail(scanner.nextLine());

        System.out.print("ID de Dirección (ej. 1 para una dirección existente): ");
        // En una aplicación real, esto implicaría buscar una dirección o crear una nueva.
        newCustomer.setAddressId(Integer.parseInt(scanner.nextLine()));

        System.out.print("¿Está activo (true/false)? ");
        newCustomer.setActive(Boolean.parseBoolean(scanner.nextLine()));

        // createDate y lastUpdate se establecen en la base de datos con NOW() en el SQL INSERT
        // No necesitamos pedirlos al usuario aquí.
        newCustomer.setCreateDate(new Timestamp(System.currentTimeMillis())); // Solo para el objeto Java
        newCustomer.setLastUpdate(new Timestamp(System.currentTimeMillis())); // Solo para el objeto Java

        try {
            Customer createdCustomer = customerRepository.save(newCustomer);
            System.out.println("Cliente registrado con éxito. ID: " + createdCustomer.getCustomerId());
        } catch (RuntimeException e) {
            System.err.println("Error al registrar el cliente: " + e.getMessage());
        }
    }

    @Override
    public void handleUpdate() {
        System.out.println("\n--- Actualizar Cliente ---");
        System.out.print("Ingrese el ID del cliente a actualizar: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Optional<Customer> existingCustomerOpt = customerRepository.findById(customerId);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            System.out.println("Datos actuales del cliente:");
            displayEntity(existingCustomer);

            System.out.print("Nuevo ID de Tienda (" + existingCustomer.getStoreId() + "): ");
            String storeIdStr = scanner.nextLine();
            if (!storeIdStr.isEmpty()) existingCustomer.setStoreId(Integer.parseInt(storeIdStr));

            System.out.print("Nuevo Nombre (" + existingCustomer.getFirstName() + "): ");
            String firstName = scanner.nextLine();
            if (!firstName.isEmpty()) existingCustomer.setFirstName(firstName);

            System.out.print("Nuevo Apellido (" + existingCustomer.getLastName() + "): ");
            String lastName = scanner.nextLine();
            if (!lastName.isEmpty()) existingCustomer.setLastName(lastName);

            System.out.print("Nuevo Email (" + existingCustomer.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) existingCustomer.setEmail(email);

            System.out.print("Nuevo ID de Dirección (" + existingCustomer.getAddressId() + "): ");
            String addressIdStr = scanner.nextLine();
            if (!addressIdStr.isEmpty()) existingCustomer.setAddressId(Integer.parseInt(addressIdStr));

            System.out.print("¿Activo (true/false) (" + existingCustomer.isActive() + ")? ");
            String activeStr = scanner.nextLine();
            if (!activeStr.isEmpty()) existingCustomer.setActive(Boolean.parseBoolean(activeStr));

            try {
                Customer updatedCustomer = customerRepository.update(existingCustomer);
                System.out.println("Cliente con ID " + updatedCustomer.getCustomerId() + " actualizado con éxito.");
            } catch (RuntimeException e) {
                System.err.println("Error al actualizar el cliente: " + e.getMessage());
            }

        } else {
            System.out.println("No se encontró ningún cliente con el ID " + customerId + ".");
        }
    }

    @Override
    public void handleDelete() {
        System.out.println("\n--- Eliminar Cliente ---");
        System.out.print("Ingrese el ID del cliente a eliminar: ");
        int customerId = Integer.parseInt(scanner.nextLine());

        Optional<Customer> customerToDelete = customerRepository.findById(customerId);
        if (customerToDelete.isPresent()) {
            System.out.print("¿Está seguro de que desea eliminar al cliente '" + customerToDelete.get().getFirstName() + " " + customerToDelete.get().getLastName() + "' (S/N)? ");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("S")) {
                try {
                    customerRepository.deleteById(customerId);
                    System.out.println("Cliente con ID " + customerId + " eliminado con éxito.");
                } catch (RuntimeException e) {
                    System.err.println("Error al eliminar el cliente: " + e.getMessage());
                }
            } else {
                System.out.println("Operación de eliminación cancelada.");
            }
        } else {
            System.out.println("No se encontró ningún cliente con el ID " + customerId + ".");
        }
    }

    @Override
    public void handleSearch() {
        System.out.println("\n--- Buscar Clientes ---");
        System.out.println("Opciones de búsqueda:");
        System.out.println("1. Por nombre (contiene)");
        System.out.println("2. Por email (contiene)");
        System.out.print("Seleccione una opción: ");
        String searchOption = scanner.nextLine();
        List<Customer> results = null;

        switch (searchOption) {
            case "1":
                System.out.print("Ingrese parte del nombre a buscar: ");
                String namePart = scanner.nextLine();
                // Ojo: Esto busca en first_name O last_name. Puedes refinarlo.
                results = customerRepository.search("(first_name LIKE ? OR last_name LIKE ?)", "%" + namePart + "%", "%" + namePart + "%");
                break;
            case "2":
                System.out.print("Ingrese parte del email a buscar: ");
                String emailPart = scanner.nextLine();
                results = customerRepository.search("email LIKE ?", "%" + emailPart + "%");
                break;
            default:
                System.out.println("Opción de búsqueda no válida.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("\n--- Resultados de la Búsqueda ---");
            results.forEach(this::displayEntity);
        } else {
            System.out.println("No se encontraron clientes que coincidan con los criterios de búsqueda.");
        }
    }
}