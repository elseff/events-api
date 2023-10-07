CREATE TABLE event_entity(
    id BIGSERIAL NOT NULL,
    title VARCHAR(100) NOT NULL,
    date TIMESTAMP NOT NULL,
    place_id BIGINT NOT NULL,
    organizer_id BIGINT NOT NULL,
    CONSTRAINT pk_event_id PRIMARY KEY (id),
    CONSTRAINT fk_place_id FOREIGN KEY (place_id) REFERENCES place_entity(id),
    CONSTRAINT fk_organizer_id FOREIGN KEY (organizer_id) REFERENCES user_entity(id)
);