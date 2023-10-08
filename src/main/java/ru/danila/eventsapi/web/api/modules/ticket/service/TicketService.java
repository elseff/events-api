package ru.danila.eventsapi.web.api.modules.ticket.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.danila.eventsapi.persistense.EventEntity;
import ru.danila.eventsapi.persistense.TicketEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.TicketRepository;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;
import ru.danila.eventsapi.web.api.modules.event.service.EventService;
import ru.danila.eventsapi.web.api.modules.ticket.dto.TicketCreationRequest;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketService {

    EventService eventService;

    UserService userService;

    AuthService authService;

    TicketRepository ticketRepository;

    @Transactional
    public TicketEntity buyTicket(TicketCreationRequest request) {
        EventEntity event = eventService.findByTitle(request.getEventTitle()).orElseThrow(
                () -> new IllegalArgumentException("Нет события с таким названием"));

        UserEntity user = userService.findByUsername(authService.getCurrentAuthUser().getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Косяк"));

        if (event.getFreeSeats()<=0)
            throw new IllegalArgumentException("Кончились билеты!");

        event.setFreeSeats(event.getFreeSeats() - 1);
        TicketEntity ticket = TicketEntity.builder()
                .event(event)
                .user(user)
                .build();

        return ticketRepository.save(ticket);
    }
}
