CREATE TABLE role_entity
(
    id   SERIAL      NOT NULL,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT pk_role_id PRIMARY KEY (id),
    CONSTRAINT uq_role_name UNIQUE (name)
);

INSERT INTO role_entity(name)
VALUES ('ROLE_USER');

INSERT INTO role_entity(name)
VALUES ('ROLE_ORGANIZER');