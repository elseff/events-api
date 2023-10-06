package ru.danila.eventsapi.persistense.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danila.eventsapi.persistense.OrganizerEntity;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, Long> {
}
