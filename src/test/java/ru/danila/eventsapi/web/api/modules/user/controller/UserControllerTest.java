package ru.danila.eventsapi.web.api.modules.user.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.ticket.dto.assembler.TicketDtoAssembler;
import ru.danila.eventsapi.web.api.modules.ticket.service.TicketService;
import ru.danila.eventsapi.web.api.modules.user.dto.assemlber.UserDtoAssembler;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
@DisplayName("Контроллер управления пользователями")
class UserControllerTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UserService userService;

    @Autowired
    UserDtoAssembler userDtoAssembler;

    @Autowired
    TicketService ticketService;

    @Autowired
    AuthService authService;

    @Autowired
    TicketDtoAssembler ticketDtoAssembler;

    @Autowired
    MockMvc mockMvc;

    final String endPoint = "/api/v1/users";
    static final int port = 8090;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("server.port", () -> port);
    }

    @Test
    @DisplayName("Подгрузка контекста")
    void contextLoads() {
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(userDtoAssembler);
        Assertions.assertNotNull(ticketDtoAssembler);
        Assertions.assertNotNull(ticketService);
        Assertions.assertNotNull(authService);
        Assertions.assertNotNull(mockMvc);
    }
}