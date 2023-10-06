CREATE TABLE place_entity
(
    id       BIGSERIAL    NOT NULL,
    address  VARCHAR(100) NOT NULL,
    capacity BIGINT       NOT NULL,
    CONSTRAINT pk_place_id PRIMARY KEY (id),
    CONSTRAINT capacity_greater_than_zero CHECK (capacity > 0)
);