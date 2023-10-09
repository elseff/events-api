package ru.danila.eventsapi.web.api.modules.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.EventEntity;
import ru.danila.eventsapi.persistense.PlaceEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.EventRepository;
import ru.danila.eventsapi.persistense.dao.PlaceRepository;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.event.dto.assembler.EventDtoAssembler;
import ru.danila.eventsapi.web.api.modules.event.dto.EventCreationRequest;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {

    EventRepository eventRepository;

    EventDtoAssembler eventDtoAssembler;

    PlaceRepository placeRepository;

    UserService userService;

    AuthService authService;

    @Transactional
    public EventEntity addEvent(EventCreationRequest request) {
        PlaceEntity place = placeRepository.findByAddress(request.getPlaceAddress()).orElseThrow(
                () -> new IllegalArgumentException("Места с таким адресом не сущесвует"));

        UserEntity currentUser = userService.findByUsername(authService.getCurrentAuthUser().getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Косяк"));
        EventEntity event = eventDtoAssembler.mapEventCreationRequestToEventEntity(request);
        event.setPlace(place);
        event.setOrganizer(currentUser);

        event.setFreeSeats(place.getCapacity());

        return eventRepository.save(event);
    }

    @Transactional
    public List<EventEntity> findAll() {
        return eventRepository.findAll();
    }

    @Transactional
    public Optional<EventEntity> findByTitle(String title) {
        return eventRepository.findByTitle(title);
    }

    @Transactional
    public void deleteByTitle(String title) {
        EventEntity event = eventRepository.findByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("Не сущесвует такого события"));

        UserEntity currentUser = userService.findByUsername(authService.getCurrentAuthUser().getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Косяк"));

        if (!event.getOrganizer().equals(currentUser))
            throw new IllegalArgumentException("Нельзя отменить чужое событие");
        else
            eventRepository.delete(event);
    }
}
