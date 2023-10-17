package ru.danila.eventsapi.web.api.modules.event.service;

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
import ru.danila.eventsapi.persistense.PlaceEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.EventRepository;
import ru.danila.eventsapi.persistense.dao.PlaceRepository;
import ru.danila.eventsapi.security.UserDetailsImpl;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationRequest;
import ru.danila.eventsapi.web.api.modules.event.dto.assembler.EventDtoAssembler;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Управление событиями")
@FieldDefaults(level = AccessLevel.PRIVATE)
class EventServiceTest {

    @Mock
    EventRepository eventRepository;

    @Mock
    EventDtoAssembler eventDtoAssembler;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    UserService userService;

    @Mock
    AuthService authService;

    @InjectMocks
    EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Найти все события")
    void findAll() {
        when(eventRepository.findAll()).thenReturn(List.of(getEvent1(), getEvent2()));

        List<EventEntity> events = eventService.findAll();

        int expectedListSize = 2;
        int actualListSize = events.size();

        Assertions.assertEquals(expectedListSize, actualListSize);

        verify(eventRepository, times(1)).findAll();
        verifyNoMoreInteractions(eventRepository);
        verifyNoInteractions(eventDtoAssembler);
        verifyNoInteractions(placeRepository);
        verifyNoInteractions(userService);
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("Добавить событие")
    void addEvent() {
        EventEntity event = getEvent1();
        when(placeRepository.findByAddress(anyString())).thenReturn(Optional.of(getPlace1()));
        when(eventDtoAssembler.mapEventCreationRequestToEventEntity(any(EventCreationRequest.class))).thenReturn(event);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity createdEvent = eventService.addEvent(getEventCreationRequest());

        Assertions.assertNotNull(createdEvent);

        String expectedOrganizerUsername = "test1";
        String actualOrganizerUsername = createdEvent.getOrganizer().getUsername();

        Assertions.assertEquals(expectedOrganizerUsername, actualOrganizerUsername);

        verify(placeRepository, times(1)).findByAddress(anyString());
        verify(authService, times(1)).getCurrentAuthUser();
        verify(userService, times(1)).findByUsername(anyString());
        verify(eventDtoAssembler, times(1)).mapEventCreationRequestToEventEntity(any(EventCreationRequest.class));
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        verifyNoMoreInteractions(eventDtoAssembler);
        verifyNoMoreInteractions(eventRepository);
        verifyNoMoreInteractions(placeRepository);
        verifyNoMoreInteractions(authService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Добавить событие, если место проведения не найдено")
    void addEvent_If_Place_Is_Not_Found() {
        when(placeRepository.findByAddress(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> eventService.addEvent(getEventCreationRequest()));

        String expectedExceptionMessage = "Места с таким адресом не существует";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(placeRepository, times(1)).findByAddress(anyString());
        verifyNoMoreInteractions(placeRepository);
        verifyNoInteractions(eventDtoAssembler);
        verifyNoInteractions(eventRepository);
        verifyNoInteractions(authService);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Найти событие по названию")
    void findByTitle() {
        when(eventRepository.findByTitle(anyString())).thenReturn(Optional.of(getEvent1()));

        Optional<EventEntity> eventOptional = eventService.findByTitle(anyString());

        Assertions.assertTrue(eventOptional.isPresent());

        EventEntity event = eventOptional.get();

        String expectedEventTitle = event.getTitle();
        String actualEventTitle = event.getTitle();

        Assertions.assertEquals(expectedEventTitle, actualEventTitle);
        verify(eventRepository, times(1)).findByTitle(anyString());
        verifyNoMoreInteractions(eventRepository);
        verifyNoInteractions(eventDtoAssembler);
        verifyNoInteractions(placeRepository);
        verifyNoInteractions(authService);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Удалить событие по названию")
    void deleteByTitle() {
        when(eventRepository.findByTitle(anyString())).thenReturn(Optional.of(getEvent1()));
        when(authService.getCurrentAuthUser()).thenReturn(getUserDetails1());
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(getUserEntity1()));
        doNothing().when(eventRepository).delete(any(EventEntity.class));

        eventService.deleteByTitle("test");

        verify(eventRepository, times(1)).findByTitle(anyString());
        verify(authService, times(1)).getCurrentAuthUser();
        verify(userService, times(1)).findByUsername(anyString());
        verify(eventRepository, times(1)).delete(any(EventEntity.class));
        verifyNoInteractions(eventDtoAssembler);
        verifyNoInteractions(placeRepository);
        verifyNoMoreInteractions(eventRepository);
        verifyNoMoreInteractions(authService);
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("Удалить событие, если оно не найдено")
    void deleteByTitle_If_Event_Is_Not_Found() {
        when(eventRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> eventService.deleteByTitle("test"));

        String expectedExceptionMessage = "Не существует такого события";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(eventRepository, times(1)).findByTitle(anyString());
        verifyNoMoreInteractions(eventRepository);
        verifyNoInteractions(eventDtoAssembler);
        verifyNoInteractions(placeRepository);
        verifyNoInteractions(authService);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Удалить чужое событие")
    void deleteByTitle_If_Someone_Else_Event() {
        when(eventRepository.findByTitle(anyString())).thenReturn(Optional.of(getEvent1()));
        when(authService.getCurrentAuthUser()).thenReturn(getUserDetails2());
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(getUserEntity2()));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> eventService.deleteByTitle(anyString()));

        String expectedExceptionMessage = "Нельзя отменить чужое событие";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(eventRepository, times(1)).findByTitle(anyString());
        verify(authService, times(1)).getCurrentAuthUser();
        verify(userService, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(eventRepository);
        verifyNoMoreInteractions(authService);
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(eventDtoAssembler);
        verifyNoInteractions(placeRepository);
    }

    private EventCreationRequest getEventCreationRequest() {
        return EventCreationRequest.builder()
                .date(getEvent1().getDate())
                .title(getEvent1().getTitle())
                .placeAddress(getPlace1().getAddress())
                .build();
    }

    private EventEntity getEvent1() {
        return EventEntity.builder()
                .title("test1")
                .place(getPlace1())
                .organizer(getUserEntity1())
                .freeSeats(100L)
                .date(Timestamp.from(Instant.now()))
                .build();
    }

    private EventEntity getEvent2() {
        return EventEntity.builder()
                .title("test2")
                .place(getPlace2())
                .organizer(getUserEntity2())
                .freeSeats(100L)
                .date(Timestamp.from(Instant.now()))
                .build();
    }

    private PlaceEntity getPlace1() {
        return PlaceEntity.builder()
                .address("test1")
                .capacity(100L)
                .build();
    }

    private PlaceEntity getPlace2() {
        return PlaceEntity.builder()
                .address("test2")
                .capacity(200L)
                .build();
    }

    private UserDetails getUserDetails1() {
        return UserDetailsImpl.builder()
                .username("test1")
                .password("test1")
                .build();
    }

    private UserEntity getUserEntity1() {
        return UserEntity.builder()
                .id(1L)
                .firstName("test1")
                .lastName("test1")
                .username(getUserDetails1().getUsername())
                .password(getUserDetails1().getPassword())
                .build();
    }

    private UserDetails getUserDetails2() {
        return UserDetailsImpl.builder()
                .username("test2")
                .password("test2")
                .build();
    }

    private UserEntity getUserEntity2() {
        return UserEntity.builder()
                .id(2L)
                .firstName("test2")
                .lastName("test2")
                .username(getUserDetails2().getUsername())
                .password(getUserDetails2().getPassword())
                .build();
    }
}