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
INSERT INTO games (name, current_chapter, current_scene, player_id, start_date, last_activity_date, state, total_score)
VALUES
('Aventura Inicial', 1, 1, 3, '2025-01-01', '2025-01-01', 'IN_PROGRESS', 0),
('El Misterio Oscuro', 2, 5, 4, '2025-01-05', '2025-01-06', 'PAUSED', 100);

-- Capítulos base
INSERT INTO chapters (id, name, chapter_number,game_id)
VALUES
(1, 'El Inicio de la Aventura', 1,1),
(2, 'El Misterio se Profundiza', 2,1);

-- Escenas base, sustituir por las de verdad
INSERT INTO scenes (id, name, description, image_url, chapter_id)
VALUES
(1, 'La Llegada al Pueblo', 'El héroe llega a un pueblo tranquilo, pero algo extraño sucede.', 'TFG-A.png', 1),
(2, 'La Casa Misteriosa', 'Una casa abandonada en el bosque esconde secretos.', 'TFG-A.png', 1),
(5, 'La Sombra del Pasado', 'El protagonista enfrenta recuerdos oscuros.', 'TFG-A.png', 2);

-- Opciones base
INSERT INTO options (id, text, points_impact, relationship_impact, next_scene_id, scene_id)
VALUES
(1, 'Entrar a la casa', 5, 0, 2, 1),
(2, 'Volver al pueblo', -3, 0, 1, 1);

