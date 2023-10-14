package ru.danila.eventsapi.persistense.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danila.eventsapi.persistense.TicketEntity;
import ru.danila.eventsapi.persistense.UserEntity;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findAllByUser(UserEntity user);
}
