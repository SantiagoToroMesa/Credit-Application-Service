package com.springboot.credit_application_service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Clase base abstracta que configura y arranca un contenedor MySQL usando Testcontainers.
 * Todas las clases de pruebas de integración deben extender esta clase.
 */
@Testcontainers // Habilita la integración de JUnit 5 con Testcontainers
public abstract class AbstractIntegrationTest {

    // 1. Definición del Contenedor MySQL
    // Se usa la versión 8 para mayor compatibilidad con tu conector y Flyway
    @Container
    protected static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.44")
            // Nombres de configuración
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            // Configuraciones de Testcontainers (opcional, pero útil para depuración)
            .withReuse(false); // No reutilizar el contenedor entre ejecuciones (entorno limpio)

    // 2. Inyección Dinámica de Propiedades
    /**
     * Este método inyecta las propiedades dinámicas generadas por el contenedor
     * (el puerto asignado, la URL, etc.) en el contexto de Spring Boot.
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Le dice a Spring que use la URL JDBC generada por el contenedor
        registry.add("spring.datasource.url", mysql::getJdbcUrl);

        // Le dice a Spring que use las credenciales del contenedor
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);

        // Configuramos la propiedad ddl-auto de Hibernate para pruebas
        // Asumiendo que Flyway ya creó el esquema, Hibernate no debe modificarlo.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }
}