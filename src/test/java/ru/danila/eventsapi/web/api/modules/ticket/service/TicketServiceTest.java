package ru.danila.eventsapi.web.api.modules.ticket.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import ru.danila.eventsapi.persistense.EventEntity;
import ru.danila.eventsapi.persistense.TicketEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.TicketRepository;
import ru.danila.eventsapi.security.UserDetailsImpl;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.event.service.EventService;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationRequest;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Сервис по покупке билетов")
@FieldDefaults(level = AccessLevel.PRIVATE)
class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Mock
    EventService eventService;

    @Mock
    AuthService authService;

    @Mock
    UserService userService;

    @Mock
    TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Покупка билета")
    void buyTicket() {
        when(eventService.findByTitle(getTicketCreationRequest().getEventTitle()))
                .thenReturn(Optional.of(getEvent()));
        when(authService.getCurrentAuthUser())
                .thenReturn(getUserDetails());
        when(userService.findByUsername(anyString()))
                .thenReturn(Optional.of(getUser()));
        when(ticketRepository.save(any(TicketEntity.class)))
                .thenReturn(getTicket());

        TicketEntity ticket = ticketService.buyTicket(getTicketCreationRequest());

        Assertions.assertNotNull(ticket);

        String expectedEventTitle = "test";
        String actualEventTitle = ticket.getEvent().getTitle();
        String expectedUsername = "test";
        String actualUsername = ticket.getUser().getUsername();

        Assertions.assertEquals(expectedEventTitle, actualEventTitle);
        Assertions.assertEquals(expectedUsername, actualUsername);
        verify(eventService, times(1)).findByTitle(anyString());
        verify(authService, times(1)).getCurrentAuthUser();
        verify(userService, times(1)).findByUsername(anyString());
        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(authService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    @DisplayName("Покупка билета на несуществующее событие")
    void buyTicket_If_Event_Is_Not_Found() {
        when(eventService.findByTitle(anyString()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> ticketService.buyTicket(getTicketCreationRequest()));

        String expectedExceptionMessage = "Нет события с таким названием";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);
        verify(eventService, times(1)).findByTitle(anyString());
        verifyNoMoreInteractions(eventService);
        verifyNoInteractions(authService);
        verifyNoInteractions(userService);
        verifyNoInteractions(ticketRepository);
    }

    @Test
    @DisplayName("Купить билет, если кончились билеты")
    void buyTicket_If_No_Tickets() {
        EventEntity event = getEvent();
        event.setFreeSeats(0L);

        when(eventService.findByTitle(anyString()))
                .thenReturn(Optional.of(event));
        when(authService.getCurrentAuthUser())
                .thenReturn(getUserDetails());
        when(userService.findByUsername(anyString()))
                .thenReturn(Optional.of(getUser()));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> ticketService.buyTicket(getTicketCreationRequest()));

        String expectedExceptionMessage = "Кончились билеты!";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(eventService, times(1)).findByTitle(anyString());
        verify(authService, times(1)).getCurrentAuthUser();
        verify(userService, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(authService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(ticketRepository);
    }

    private EventEntity getEvent() {
        return EventEntity.builder()
                .title("test")
                .date(Timestamp.from(Instant.now()))
                .organizer(getUser())
                .freeSeats(100L)
                .build();
    }

    private UserEntity getUser() {
        return UserEntity.builder()
                .firstName("test")
                .lastName("test")
                .username(getUserDetails().getUsername())
                .password(getUserDetails().getPassword())
                .build();
    }

    private TicketEntity getTicket() {
        return TicketEntity.builder()
                .event(getEvent())
                .user(getUser())
                .build();
    }

    private UserDetails getUserDetails() {
        return UserDetailsImpl.builder()
                .username("test")
                .password("test")
                .build();
    }

    private TicketCreationRequest getTicketCreationRequest() {
        return TicketCreationRequest.builder()
                .eventTitle("test")
                .build();
    }
}