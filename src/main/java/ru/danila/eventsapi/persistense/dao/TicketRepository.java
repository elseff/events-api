package ru.danila.eventsapi.persistense.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danila.eventsapi.persistense.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
}
