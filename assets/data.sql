INSERT INTO users (username, password, authority) VALUES ('Admin1', '$2a$10$CJNLaQiaItzVV2v5ZmQH3.nDYohoZEg5MrUgFdlKjI1IeKxE./WRG', 1);
INSERT INTO users (username, password, authority) VALUES ('Admin2', '$2a$10$CJNLaQiaItzVV2v5ZmQH3.nDYohoZEg5MrUgFdlKjI1IeKxE./WRG', 1);


INSERT INTO users (username, password, authority) VALUES ('Player1', '$2a$10$UhRcQWnxgZ1cZF5H76yE7eJGEcF1WDpqWI8RpKYnL.kbmKHZNf0EW', 2);
INSERT INTO users (username, password, authority) VALUES ('Player2', '$2a$10$UhRcQWnxgZ1cZF5H76yE7eJGEcF1WDpqWI8RpKYnL.kbmKHZNf0EW', 2);


INSERT INTO players (firstname, lastname, email, image, pointsM, pointsR, user_id)
VALUES ('Feyre', 'Archeron', 'player1@example.com', 'default_image.png', 0, 0, 3);
INSERT INTO players (firstname, lastname, email, image, pointsM, pointsR, user_id)
VALUES ('Nesta', 'Archeron', 'player2@example.com', 'Perfil_2.png', 0, 0, 4);


INSERT INTO news (id, name, text, image)
VALUES
(3, 'La violencia que no cesa: 2024 acaba con 47 mujeres y nueve menores asesinados en crímenes machistas', 'Ana fue asesinada por su pareja el 12 de enero de 2024, tenía 57 años; Diana el 30 de diciembre, con 29 años. También por su pareja. Convivían pese a que él tuviera una orden de alejamiento. En 2024 hubo 47 mujeres asesinadas por violencia machista. Es una cifra aterradora, aunque sea la menor desde 2003, año en el que empezó el recuento oficial de esta violencia estructural que no cesa y que ha acabado con la vida de 1.292 mujeres. En 2024 fueron asesinados también por violencia vicaria nueve menores, hijos e hijas de madres víctimas de sus parejas o exparejas. Es la cifra más alta junto a la que se registró en 2015 (en 2023 fueron, por ejemplo, dos; como en 2022).De las 47 mujeres asesinadas en 2024, un total de 24 eran extranjeras y 23 españolas. De los agresores, solo13 tenían denuncias por maltrato. Once se suicidaron tras cometer los crímenes, por lo que no podrán ser juzgados. En 30 casos, la víctima convivía con el agresor y 19 de ellas se habían separado o habían iniciado los trámites para hacerlo. En seis de los casos falló todo: las víctimas habían denunciado a sus agresores y estaban inscritas en el Sistema VioGén ―el sistema de protección de las víctimas de violencia de género―, pero los mecanismos con los que cuenta el Estado para protegerlas fracasaron.','News3.png'),
(2, 'La Ertzaintza detiene a un hombre acusado de agredir sexualmente a una mujer en su consulta de terapia natural en Guipuzkoa', 'Un hombre ha sido detenido este fin de semana en Getaria (Guipizkoa), acusado de agredir sexualmente a una mujer que acudía a su consulta de terapia natural. La Ertzaintza investiga si puede haber más víctimas. El presunto agresor ha quedado en libertad tras pasar a disposición judicial.El arresto tuvo lugar el pasado viernes después de que una mujer denunciara una presunta agresión sexual ocurrida en una vivienda de Getaria por parte del responsable de una consulta de terapia natural, a la que acudía semanalmente.La Ertzaintza investiga si el conocimiento de la situación de vulnerabilidad de la víctima pudo ser aprovechado para cometer la agresión. Además, los agentes realizan pesquisas para saber si otras pacientes de la consulta han podido ser víctimas de actos similares, por lo que solicitan la colaboración de las posibles afectadas.El detenido, de 51 años de edad, fue trasladado a dependencias de la Ertzaintza para realizar las correspondientes diligencias policiales. Una vez concluidas, fue puesto a disposición judicial y quedó en libertad.', 'News2.png'),
(1, 'News Title 1', 'Detailed content of the first news item.', 'News1.png');




-- "Game base" en la tabla games
INSERT INTO games (name, current_chapter_id, current_scene_id, current_decision_id, player_id, start_date, state, points_m, points_r)
VALUES
('Partida1', 1, 1, 1, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida2', 1, 1, 1, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida3', 1, 1, 1, 2, '1736965255031', 'IN_PROGRESS', 0, 0);


-- Capítulos base
INSERT INTO chapters (name, chapter_number, image_path, required_relationship_state)
VALUES
    ('Capitulo', '1', 'Chapter1.png', NULL),
    ('Capitulo', '2', 'Default_Chapter.png', NULL),
    ('Capitulo', '3', 'Default_Chapter.png', NULL),
    ('Capitulo', '4', 'Default_Chapter.png', NULL),
    ('Capitulo', '5', 'Default_Chapter.png', NULL),
    ('Capitulo', '6.1', 'Default_Chapter.png', 'CONFIDENCE'),
    ('Capitulo', '6.2', 'Default_Chapter.png', 'LOW_TENSION'),
    ('Capitulo', '6.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    ('Capitulo', '6.4', 'Default_Chapter.png', 'CRITICAL'),
    ('Capitulo', '7.1', 'Default_Chapter.png', 'CONFIDENCE'),
    ('Capitulo', '7.2', 'Default_Chapter.png', 'LOW_TENSION'),
    ('Capitulo', '7.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    ('Capitulo', '7.4', 'Default_Chapter.png', 'CRITICAL'),
    ('Capitulo', '8.1', 'Default_Chapter.png', 'CONFIDENCE'),
    ('Capitulo', '8.2', 'Default_Chapter.png', 'LOW_TENSION'),
    ('Capitulo', '8.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    ('Capitulo', '8.4', 'Default_Chapter.png', 'CRITICAL'),
    ('Capitulo', '9.1', 'Default_Chapter.png', 'CONFIDENCE'),
    ('Capitulo', '9.2', 'Default_Chapter.png', 'LOW_TENSION'),
    ('Capitulo ','9.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    ('Capitulo', '9.4', 'Default_Chapter.png', 'CRITICAL'),
    ('Final', '1', 'Default_Chapter.png', 'CONFIDENCE'),
    ('Final', '2', 'Default_Chapter.png', 'LOW_TENSION'),
    ('Final', '3', 'Default_Chapter.png', 'HIGH_TENSION'),
    ('Final ','4', 'Default_Chapter.png', 'CRITICAL');


INSERT INTO player_chapter_state (player_id, chapter_id, game_id, played, unlocked)
VALUES
    (1, 1,1, false, true),
    (1, 2,1, false, true),
    (1, 3,1, false, true),
    (1, 4,1, false, true),
    (1, 5,1, false, true),
    (1, 6,1, false, true),
    (1,10,1, false, true),
    (1, 1,2, false, true),
    (1, 1,3, false, true);


INSERT INTO scenes (id,chapter_id, background_image, scene_order)
VALUES
(1,1, 'Living_Room.jpg', 1),
(2,1, 'Living_Room.jpg', 2),
(3,1, 'Living_Room.jpg', 3),
(4,1, 'Living_Room.jpg', 4),
(5,2, 'Living_Room.jpg', 1),
(6,2, 'Living_Room.jpg', 2),
(7,2, 'Living_Room.jpg', 3),
(8,2, 'Living_Room.jpg', 4);


INSERT INTO characters (name, image_path)
VALUES
('David', 'David.png');


INSERT INTO decisions (id,scene_id, decision_order, character_id, dialogue)
VALUES
(1,1, 1, 1, '¡Hola! Que tal(Escena 1)'),
(2,1, 2, 1, 'Soy David(Escena 1)'),
(3,1, 3, 1, '¿Que hacemos hoy?(Escena 1)'),
(4,2, 1, 1, 'Escena 2'),
(5,3, 1, 1, 'Escena 3'),
(6,4, 1, NULL, 'Escena 4');

INSERT INTO options (decision_id, text, points_m, points_r, next_decision_id, required_psychological_state)
VALUES
(1, 'Bien, gracias. ¿Y tú?', 0, 0, 2, NULL),
(2,'Yo soy Bella', 0, 0, 3, NULL),
(3,'Salgamos a explorar la ciudad.(Pasa a la escena2)', 10, 5, NULL, NULL),
(3,'Mejor quedémonos en casa.(Pasa a la escena2)', -5, -10, NULL, NULL),
(4,'Pasa a la decision 5 (Escena 3)', 0, 0, NULL, NULL),
(5,'Pasa a la decision 6 (Escena 4)', 0, 0, NULL, NULL),
(6,'Has terminado el capitulo 1!', 0, 0, NULL, NULL);

--INSERT INTO player_decision_state (player_id, game_id, decision_id, completed)
--VALUES
