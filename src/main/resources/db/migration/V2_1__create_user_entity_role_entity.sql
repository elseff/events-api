CREATE TABLE user_entity_role_entity(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_entity(id),
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES role_entity(id),
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id)
);