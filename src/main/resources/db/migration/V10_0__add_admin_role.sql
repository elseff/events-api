INSERT INTO role_entity(name)
VALUES ('ROLE_ADMIN');

INSERT INTO user_entity(id, username, first_name, last_name, password)
VALUES (0, 'admin', 'admin', 'admin', 'admin');

INSERT INTO user_entity_role_entity
VALUES (0, 4);
