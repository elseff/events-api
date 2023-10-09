INSERT INTO user_entity(first_name, last_name, username, password)
VALUES
('User 1', 'User 1', 'user1', '$2a$12$BVCXmfFzj5kp9m0cKb.L8OTRFGEvWnrU6aQYRj2geyxMJ4xocAzYy'),
('Organizer 1', 'Organizer 1', 'organizer1', '$2a$12$BVCXmfFzj5kp9m0cKb.L8OTRFGEvWnrU6aQYRj2geyxMJ4xocAzYy'),
('Moderator 1', 'Moderator 1', 'moder1', '$2a$12$BVCXmfFzj5kp9m0cKb.L8OTRFGEvWnrU6aQYRj2geyxMJ4xocAzYy');

INSERT INTO user_entity_role_entity
VALUES
(1, 1),
(2, 2);
(3, 3);

INSERT INTO place_entity(address, capacity)
VALUES
('Городская площадь', 5000),
('Сквер имени Пушкина', 500),
('Школа номер 3', 1500),
('Городской парк', 2000);

INSERT INTO event_entity (title, date, place_id, organizer_id, free_seats)
VALUES
('Событие 1', '2023-10-10',1, 2, 5000),
('Событие 2', '2023-10-11',2, 2, 500),
('Событие 3', '2023-10-12',3, 2, 1500),
('Событие 4', '2023-10-13',4, 2, 2000);