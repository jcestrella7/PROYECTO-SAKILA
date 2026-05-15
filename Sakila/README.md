# ORM Data Manager - Sakila DB
## INF514 Z06 - UASD | Programación II Java

---

## Estructura del Proyecto

```
com.sakila/
├── Main.java                    ← Punto de entrada (menú consola)
├── data/
│   ├── IDatapost.java           ← Interface CRUD genérica
│   ├── DataContext.java         ← Clase abstract híbrida (métodos final)
│   ├── FilmDAO.java             ← DAO final para tabla film
│   ├── ActorDAO.java            ← DAO final para tabla actor
│   ├── CustomerDAO.java         ← DAO final para tabla customer
│   ├── RentalDAO.java           ← DAO final para tabla rental
│   └── InventoryDAO.java        ← DAO final para tabla inventory
├── models/
│   ├── Film.java
│   ├── Actor.java
│   ├── Customer.java
│   ├── Rental.java
│   └── Inventory.java
├── controllers/
│   ├── FilmController.java      ← MVC Controller para películas
│   └── RentalController.java    ← MVC Controller para rentas
├── reports/
│   └── ReportManager.java       ← Estadísticas + CSV/JSON export
└── utils/
    └── Validator.java           ← Regex: email, cédula, teléfono, fecha
```

---

## Requisitos

- Java 17+
- MySQL 5.8+ o 8.1+
- MySQL Workbench
- Driver JDBC: `mysql-connector-java-8.x.x.jar`
- Base de datos sakila instalada

---

## Configuración de Base de Datos

1. Descargue sakila desde: https://dev.mysql.com/doc/sakila/en/
2. Ejecute en MySQL Workbench:
   ```sql
   SOURCE sakila-schema.sql;
   SOURCE sakila-data.sql;
   ```
3. Edite la conexión en `DataContext.java`:
   ```java
   private static final String URL      = "jdbc:mysql://localhost:3306/sakila";
   private static final String USER     = "root";
   private static final String PASSWORD = "su_contraseña";
   ```

---

## Compilación

```bash
# Desde la carpeta raíz del proyecto
javac -cp mysql-connector-java-8.x.x.jar \
      -d out \
      src/com/sakila/**/*.java \
      src/com/sakila/*.java
```

## Ejecución

```bash
java -cp "out:mysql-connector-java-8.x.x.jar" com.sakila.Main
```

## Crear JAR ejecutable

```bash
jar cfm sakila-orm.jar MANIFEST.MF -C out .
java -jar sakila-orm.jar
```

**MANIFEST.MF:**
```
Main-Class: com.sakila.Main
Class-Path: mysql-connector-java-8.x.x.jar
```

---

## Funcionalidades

### CRUD Películas
- Listar, buscar por título/rating, agregar, eliminar

### Gestión de Rentas
- Ver rentas activas, nueva renta, registrar devolución

### Reportes
- Estadísticas de películas (por rating, duración promedio)
- Top 10 clientes por rentas
- Top 10 actores por películas
- Exportar a CSV y JSON

---

## Autor
**Mae. Silverio Del Orbe** - Profesor INF514 Z06  
**Estudiante:** [Tu nombre aquí]  
**UASD - Facultad de Ciencias, Escuela de Informática**  
© 2021
