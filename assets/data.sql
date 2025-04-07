INSERT INTO users (username, password, authority) VALUES ('Admin1', '$2a$10$CJNLaQiaItzVV2v5ZmQH3.nDYohoZEg5MrUgFdlKjI1IeKxE./WRG', 1);
INSERT INTO users (username, password, authority) VALUES ('Admin2', '$2a$10$CJNLaQiaItzVV2v5ZmQH3.nDYohoZEg5MrUgFdlKjI1IeKxE./WRG', 1);


INSERT INTO users (username, password, authority) VALUES ('Player1', '$2a$10$UhRcQWnxgZ1cZF5H76yE7eJGEcF1WDpqWI8RpKYnL.kbmKHZNf0EW', 2);
INSERT INTO users (username, password, authority) VALUES ('Player2', '$2a$10$UhRcQWnxgZ1cZF5H76yE7eJGEcF1WDpqWI8RpKYnL.kbmKHZNf0EW', 2);


INSERT INTO players (firstname, lastname, email, image, user_id)
VALUES ('Feyre', 'Archeron', 'player1@example.com', 'default_image.png', 3);
INSERT INTO players (firstname, lastname, email, image, user_id)
VALUES ('Nesta', 'Archeron', 'player2@example.com', 'Perfil_2.png', 4);


INSERT INTO news (id, name, text)
VALUES
(3, 'La violencia que no cesa: 2024 acaba con 47 mujeres y nueve menores asesinados en crímenes machistas', 'Ana fue asesinada por su pareja el 12 de enero de 2024, tenía 57 años; Diana el 30 de diciembre, con 29 años. También por su pareja. Convivían pese a que él tuviera una orden de alejamiento. En 2024 hubo 47 mujeres asesinadas por violencia machista. Es una cifra aterradora, aunque sea la menor desde 2003, año en el que empezó el recuento oficial de esta violencia estructural que no cesa y que ha acabado con la vida de 1.292 mujeres. En 2024 fueron asesinados también por violencia vicaria nueve menores, hijos e hijas de madres víctimas de sus parejas o exparejas. Es la cifra más alta junto a la que se registró en 2015 (en 2023 fueron, por ejemplo, dos; como en 2022).De las 47 mujeres asesinadas en 2024, un total de 24 eran extranjeras y 23 españolas. De los agresores, solo13 tenían denuncias por maltrato. Once se suicidaron tras cometer los crímenes, por lo que no podrán ser juzgados. En 30 casos, la víctima convivía con el agresor y 19 de ellas se habían separado o habían iniciado los trámites para hacerlo. En seis de los casos falló todo: las víctimas habían denunciado a sus agresores y estaban inscritas en el Sistema VioGén ―el sistema de protección de las víctimas de violencia de género―, pero los mecanismos con los que cuenta el Estado para protegerlas fracasaron.'),
(2, 'La Ertzaintza detiene a un hombre acusado de agredir sexualmente a una mujer en su consulta de terapia natural en Guipuzkoa', 'Un hombre ha sido detenido este fin de semana en Getaria (Guipizkoa), acusado de agredir sexualmente a una mujer que acudía a su consulta de terapia natural. La Ertzaintza investiga si puede haber más víctimas. El presunto agresor ha quedado en libertad tras pasar a disposición judicial.El arresto tuvo lugar el pasado viernes después de que una mujer denunciara una presunta agresión sexual ocurrida en una vivienda de Getaria por parte del responsable de una consulta de terapia natural, a la que acudía semanalmente.La Ertzaintza investiga si el conocimiento de la situación de vulnerabilidad de la víctima pudo ser aprovechado para cometer la agresión. Además, los agentes realizan pesquisas para saber si otras pacientes de la consulta han podido ser víctimas de actos similares, por lo que solicitan la colaboración de las posibles afectadas.El detenido, de 51 años de edad, fue trasladado a dependencias de la Ertzaintza para realizar las correspondientes diligencias policiales. Una vez concluidas, fue puesto a disposición judicial y quedó en libertad.'),
(1, 'News Title 1', 'Detailed content of the first news item.');


INSERT INTO historys (id, title, published,image_path,description) VALUES
(1, 'Bella y David', true,'Default_History.png','Descripcion1'),
(2, 'History 2', false,'Default_History.png','Descripcion2');


-- "Game base" en la tabla games
INSERT INTO games (name,history_id, current_chapter_id, current_scene_id, current_decision_id, player_id, start_date, state, points_m, points_r)
VALUES
('Partida1',1, 1, 1, 1, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida2',1, 1, 1, 1, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida3',2, 1, 1, 1, 2, '1736965255031', 'IN_PROGRESS', 0, 0);


-- Capítulos base
INSERT INTO chapters (name, chapter_number, image_path, required_relationship_state,history_id)
VALUES
    ('Tutorial', '0', 'Chapter1.png', NULL,NULL),
    ('Introducción', '0', 'Chapter1.png', NULL,1),
    ('Capitulo', '1', 'Chapter1.png', NULL,1),
    ('Capitulo', '2', 'Default_Chapter.png', NULL,1),
    ('Capitulo', '3', 'Default_Chapter.png', NULL,1),
    ('Capitulo', '4', 'Default_Chapter.png', NULL,1),
    ('Capitulo', '5', 'Default_Chapter.png', NULL,1),
    ('Capitulo', '6.1', 'Default_Chapter.png', 'CONFIDENT',1),
    ('Capitulo', '6.2', 'Default_Chapter.png', 'LOW_TENSION',1),
    ('Capitulo', '6.3', 'Default_Chapter.png', 'HIGH_TENSION',1),
    ('Capitulo', '6.4', 'Default_Chapter.png', 'CRITICAL',1),
    ('Capitulo', '7.1', 'Default_Chapter.png', 'CONFIDENT',1),
    ('Capitulo', '7.2', 'Default_Chapter.png', 'LOW_TENSION',1),
    ('Capitulo', '7.3', 'Default_Chapter.png', 'HIGH_TENSION',1),
    ('Capitulo', '7.4', 'Default_Chapter.png', 'CRITICAL',1),
    ('Capitulo', '8.1', 'Default_Chapter.png', 'CONFIDENT',1),
    ('Capitulo', '8.2', 'Default_Chapter.png', 'LOW_TENSION',1),
    ('Capitulo', '8.3', 'Default_Chapter.png', 'HIGH_TENSION',1),
    ('Capitulo', '8.4', 'Default_Chapter.png', 'CRITICAL',1),
    ('Capitulo', '9.1', 'Default_Chapter.png', 'CONFIDENT',1),
    ('Capitulo', '9.2', 'Default_Chapter.png', 'LOW_TENSION',1),
    ('Capitulo ','9.3', 'Default_Chapter.png', 'HIGH_TENSION',1),
    ('Capitulo', '9.4', 'Default_Chapter.png', 'CRITICAL',1),
    ('Final', '10.1', 'Default_Chapter.png', 'CONFIDENT',1),
    ('Final', '10.2', 'Default_Chapter.png', 'LOW_TENSION',1),
    ('Final', '10.3', 'Default_Chapter.png', 'HIGH_TENSION',1),
    ('Final ','10.4', 'Default_Chapter.png', 'CRITICAL',1),
    ('Introducción', '0', 'Chapter1.png', NULL,2),
    ('Capitulo', '1', 'Chapter1.png', NULL,2);


INSERT INTO player_chapter_state (player_id, chapter_id, game_id, played, unlocked)
VALUES
    (1, 2,1, false, true),
    (1, 3,1, false, true),
    (1, 4,1, false, true),
    (1, 5,1, false, true),
    (1, 6,1, false, true),
    (1, 7,1, false, true),
    (1,11,1, false, true),
    (1, 2,2, false, true),
    (1, 2,3, false, true);


INSERT INTO scenes (id,chapter_id, background_image, scene_order)
VALUES
--TUTORIAL
(1,1, 'RoomAngela.png', 1),
(2,1, 'Park.png', 2),
(3,1, 'Living_Room.jpg', 3),
(4,1, 'RoomAngela.png', 4),
--CAP1
(5,2, 'Living_Room.jpg', 1),
(6,2, 'Living_Room.jpg', 2),
(7,2, 'Living_Room.jpg', 3),
(8,2, 'Living_Room.jpg', 4),

--CAP2
(9,3, 'Living_Room.jpg', 5),
(10,3, 'Living_Room.jpg', 6),
(11,3, 'Living_Room.jpg', 1),
(12,3, 'Living_Room.jpg', 2),
--CAP1 H2
(13,28, 'Living_Room.jpg', 1);



INSERT INTO characters (name, image_path)
VALUES
('David', 'David.png'),
('Angela', 'Angela.png');


INSERT INTO decisions (id,scene_id, decision_order, character_id, dialogue)
VALUES
--TUTORIAL
(1,1, 1, 2, '¡Hola!,Yo soy Ángela, la creadora de este juego, y estoy aquí para enseñarte cómo funciona.'),
(2,1, 2, 2, 'Este juego consiste en explorar historias que giran en torno a la violencia machista.'),
(3,1, 3, 2, 'Queremos ayudarte a entender mejor este tema a través de diferentes situaciones y decisiones.'),
(4,1, 4, 2, 'Cuando pulses en las opciones que tienes abajo, la historia irá avanzando. '),
(5,1, 5, 2, 'A lo largo de la historia, habrá momentos en los que tendrás que tomar decisiones importantes... ¡Como ahora!'),
(6,1, 6, 2, '¿Ves esas barritas que acaban de cambiar ? Son la representación del estado psicologico de la protagonista y el estado de su relación '),
(7,1, 7, 2, 'Shh , no te voy a decir cuál es cuál, para que no hagas trampas'),
(8,1, 8, 2, 'Estas decisiones afectan al futuro de la historia.'),
(9,1, 9, 2, 'Cada opción que elijas llevará a resultados distintos, así que piensa bien antes de decidir.'),
(10,1, 10, 2, 'También vamos a movernos entre diferentes escenarios ¿Vamos?'),
(11,2, 1, 2, 'Bualá, estamos en el parque, es la magia de la programación'),
(12,2, 2, 2, 'Y te voy a ahorrar todos los trayectos'),
(13,3, 1, 2, '¡Como ahora!, ¿Chulo no?'),
(14,3, 2, 2, 'Quedate con esta habitación, que creo que va a ser importante'),
(15,3, 3, 2, '¡Vamonos de vuelta a mi cuarto!'),
(16,4, 1, 2, 'Pues nada, creo que eso es todo por mi parte'),
(17,4, 2, 2, 'Y recuerda, no hay respuestas perfectas. Lo importante es que reflexiones y aprendas del proceso.'),
(18,4, 3, 2, 'Pues, ¡A jugar!'),

--CAPITULO1
(19,5, 1, 1, '¡Hola! Que tal(Escena 1)'),
(20,5, 2, 1, 'Soy David(Escena 1)'),
(21,5, 3, 1, '¿Que hacemos hoy?'),
(22,6, 1, 1, 'Escena 2'),
(23,7, 1, 1, 'Escena 3'),
(24,8, 1, NULL, 'Escena 4'),

--CAPITULO 1 H2
(25,13, 1, NULL, 'Historia2');


INSERT INTO options (decision_id, text, points_m, points_r, next_decision_id, required_psychological_state)
VALUES
(1, 'PULSA AQUI',0,0,2,NULL),
(2, 'PULSA AQUI',0,0,3,NULL),
(3, 'PULSA AQUI',0,0,4,NULL),
(4, '¡Hola!,yo soy la opción',0,0,5,NULL),
(5, 'Elígeme a mí',10,25,6,NULL),
(5, '¡No!, eligeme a mi',25,10,6,NULL),
(6, 'PULSA AQUI',0,0,7,NULL),
(7, 'PULSA AQUI',0,0,8,NULL),
(8, 'PULSA AQUI',0,0,9,NULL),
(9, 'PULSA AQUI',0,0,10,NULL),
(10, '¡Vamonos!',0,0,NULL,NULL),
(11, 'PULSA AQUI',0,0,12,NULL),
(12, 'PULSA AQUI',0,0,NULL,NULL),
(13, 'PULSA AQUI',0,0,14,NULL),
(14, '¡Vamonos!',0,0,15,NULL),
(15, 'PULSA AQUI',0,0,NULL,NULL),
(16, 'PULSA AQUI',0,0,17,NULL),
(17, 'PULSA AQUI',0,0,18,NULL),
(18, '¡Sí!',0,0,NULL,NULL),
--CAPITULO1
(19, 'Bien, gracias. ¿Y tú?', 0, 0, 20, NULL),
(20,'Yo soy Bella', 0, 0, 21, NULL),
(21,'Salgamos a explorar la ciudad.(Pasa a la escena2)', 0, 0, NULL, NULL),
(21,'Mejor quedémonos en casa.(Pasa a la escena2)', 0, 0, NULL, NULL),
(22,'Pasa a la decision 5 (Escena 3)', 0, 0, NULL, NULL),
(23,'Pasa a la decision 6 (Escena 4)', 0, 0, NULL, NULL),
(24,'Has terminado el capitulo 1!', 0, 0, NULL, NULL),

--CAPITULO1 H2
(25,'Capitulo 1 Historia2', 0, 0, NULL, NULL);


