CREATE TABLE user_entity
(
    id         BIGSERIAL   NOT NULL,
    username   VARCHAR(30) NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uq_username UNIQUE (username)
);