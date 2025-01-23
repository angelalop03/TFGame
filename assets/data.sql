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
('Partida1', 2, 2, 8, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida2', 2, 2, 8, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida3', 2, 2, 8, 2, '1736965255031', 'IN_PROGRESS', 0, 0);


-- Capítulos base
INSERT INTO chapters (id,name, chapter_number, image_path, required_relationship_state)
VALUES
    (1,'Introducción', '0', 'Chapter1.png', NULL),
    (2,'Capitulo', '1', 'Chapter1.png', NULL),
    (3,'Capitulo', '2', 'Default_Chapter.png', NULL),
    (4,'Capitulo', '3', 'Default_Chapter.png', NULL),
    (5,'Capitulo', '4', 'Default_Chapter.png', NULL);
    --('Capitulo', '5', 'Default_Chapter.png', NULL),
    --('Capitulo', '6.1', 'Default_Chapter.png', 'CONFIDENCE'),
    --('Capitulo', '6.2', 'Default_Chapter.png', 'LOW_TENSION'),
    --('Capitulo', '6.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    --('Capitulo', '6.4', 'Default_Chapter.png', 'CRITICAL'),
    --('Capitulo', '7.1', 'Default_Chapter.png', 'CONFIDENCE'),
    --('Capitulo', '7.2', 'Default_Chapter.png', 'LOW_TENSION'),
    --('Capitulo', '7.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    --('Capitulo', '7.4', 'Default_Chapter.png', 'CRITICAL'),
    --('Capitulo', '8.1', 'Default_Chapter.png', 'CONFIDENCE'),
    --('Capitulo', '8.2', 'Default_Chapter.png', 'LOW_TENSION'),
    --('Capitulo', '8.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    --('Capitulo', '8.4', 'Default_Chapter.png', 'CRITICAL'),
    --('Capitulo', '9.1', 'Default_Chapter.png', 'CONFIDENCE'),
    --('Capitulo', '9.2', 'Default_Chapter.png', 'LOW_TENSION'),
    --('Capitulo ','9.3', 'Default_Chapter.png', 'HIGH_TENSION'),
    --('Capitulo', '9.4', 'Default_Chapter.png', 'CRITICAL'),
    --('Final', '1', 'Default_Chapter.png', 'CONFIDENCE'),
    --('Final', '2', 'Default_Chapter.png', 'LOW_TENSION'),
    --('Final', '3', 'Default_Chapter.png', 'HIGH_TENSION'),
    --('Final ','4', 'Default_Chapter.png', 'CRITICAL');


INSERT INTO player_chapter_state (player_id, chapter_id, game_id, played, unlocked)
VALUES
    (1, 2,1, false, true),
    (1, 3,1, false, true),
    (1, 4,1, false, true),
    (1, 5,1, false, true),
    (1, 6,1, false, true),
    (1,10,1, false, true),
    (1, 1,3, false, true);


INSERT INTO scenes (id,chapter_id, background_image, scene_order)
VALUES
(1,1, 'Living_Room.jpg', 1),
(2,2, 'Living_Room.jpg', 1),
--(3,2, 'Living_Room.jpg', 2),
--(4,2, 'Living_Room.jpg', 3),
--(5,2, 'Living_Room.jpg', 4),
(6,3, 'Living_Room.jpg', 1),
(7,3, 'Living_Room.jpg', 2),
(8,3, 'Living_Room.jpg', 3),
(9,3, 'Living_Room.jpg', 4);


INSERT INTO characters (name, image_path)
VALUES
('David', 'David.png');


INSERT INTO decisions (id,scene_id, decision_order, character_id, dialogue)
VALUES
(1,1, 1, NULL, '¡Bienvenido a Bella’s Heart, una experiencia narrativa inmersiva que te invita a explorar las complejidades de una relación y las decisiones que afectan profundamente la dinámica entre dos personas!'),
(2,1, 2, NULL, 'En esta historia, encarnarás a Bella, una joven que ha decidido dar un gran paso al mudarse con su pareja, David.'),
(3,1, 3, NULL, 'Tu misión será tomar decisiones en momentos clave de la vida de Bella'),
(4,1, 4, NULL, 'Cada decisión tendrá consecuencias, algunas inmediatas y otras a largo plazo'),
(5,1, 5, NULL, 'Eres la voz interna de Bella, su guía y conciencia. A medida que avanzas por los capítulos, serás quien la ayude a interpretar sus emociones, analizar los comportamientos de David y decidir el mejor camino para ella. '),
(6, 1, 6, NULL, 'Esta no es solo una historia de amor; es una exploración de los límites, la confianza y la búsqueda del bienestar personal. Toma decisiones con sabiduría, y recuerda: cada elección cuenta. '),
(7, 1, 7, NULL, '¿Estás listo para comenzar ?'),
(8, 2, 1, 1, 'Bueno, aquí estamos, en nuestro primer hogar juntos. ¿Qué te parece? ¿Te imaginas todas las cosas increíbles que vamos a vivir aquí?'),
(9, 2, 2, 1, 'Claro que sí. Todo es nuevo, pero estoy seguro de que juntos podemos con esto. Este lugar será nuestro refugio, ¿no te parece?'),
(10,2,3, 1, 'Sabía que lo verías así. Estar aquí contigo hace que todo sea más fácil y emocionante'),
(11,2,4, 1, 'Lo entiendo, Bella. Este paso es grande para ambos. Pero estoy aquí para que lo enfrentemos juntos. Confío en que todo saldrá bien. '),
(12,2,5, 1, 'No puedo dejar de pensar en lo afortunado que soy. Tenerte aquí conmigo... significa todo para mí. Te quiero, Bella. ');





INSERT INTO options (decision_id, text, points_m, points_r, next_decision_id, required_psychological_state)
VALUES
(1, '(Continuar)', 0, 0, 2, NULL),
(2, '(Continuar)', 0, 0, 3, NULL),
(3, '(Continuar)', 0, 0, 4, NULL),
(4, '(Continuar)', 0, 0, 5, NULL),
(5, '(Continuar)', 0, 0, 6, NULL),
(6, '(Continuar)', 0, 0, 7, NULL),
(7, 'No, ¿Podrias repetirlo otra vez?', 0, 0, 1, NULL),
(7, 'Sii, Vamos allá', 0, 0, NULL, NULL),
(8, 'Sí, es increíble. Es un gran paso para los dos. Aunque siento que hay tanto por hacer aún...',0,0, 9, NULL),
(9, 'Sí, tienes razón. Ya puedo imaginar todos los momentos felices que viviremos aquí. Estoy tan feliz de haber tomado esta decisión contigo.',0,0,10, NULL),
(9, 'Espero que sí... es un gran cambio, pero estoy dispuesta a   intentarlo contigo. Necesitaré un poco de tiempo para adaptarme',5,5,11, NULL),
(10, '(Continuar)',0,0,12, NULL),
(11, 'Si... tienes razón',0,0,12,NULL),
(12, 'Yo también te quiero, David.',0,0,NULL,NULL);



--INSERT INTO player_decision_state (player_id, game_id, decision_id, completed)
--VALUES
