CREATE TABLE ticket_entity(
    id BIGSERIAL NOT NULL,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_ticket_id PRIMARY KEY (id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES event_entity(id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_entity(id)
);