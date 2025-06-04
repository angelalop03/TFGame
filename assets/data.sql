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
('Partida1',1, 2, 5, 19, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
('Partida2',1, 2, 5, 19, 1, '1736965255031', 'IN_PROGRESS', 0, 0),
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
--INTRO H1
(5,2, 'Living_Room.jpg', 1),

--CAP1
(6,3, 'Living_Room.jpg', 1),
(7,3, 'Kitchen.png', 2),
(8,3, 'Living_Room.jpg', 3),
--CAP2
(9,4, 'Living_Room.jpg', 1 ),
(10,4,'Disco.png',2),
(11,4, 'Living_Room_Nigth.png',3);

--(9,3, 'Living_Room.jpg', 4);
--INTRO H2
--(10,28, 'Living_Room.jpg', 1);



INSERT INTO characters (name, image_path)
VALUES
('David', 'David.png'),
('Angela', 'Angela.png'),
('Alex', 'Alex.png');


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

--INTRO H1
(19,5, 1, 2, '¡Bienvenido a Bella’s Heart, una experiencia narrativa inmersiva que te invita a explorar las complejidades de una relación y las decisiones que afectan profundamente la dinámica entre dos personas!'),
(20,5, 2, 2, 'En esta historia, encarnarás a Bella, una joven que ha decidido dar un gran paso al mudarse con su pareja, David'),
(21,5, 3, 2, ' Tu misión será tomar decisiones en momentos clave de la vida de Bella'),
(22,5, 4, 2, 'Cada decisión tendrá consecuencias, algunas inmediatas y otras a largo plazo'),
(23,5, 5, 2, 'Eres la voz interna de Bella, su guía y conciencia. A medida que avanzas por los capítulos, serás quien la ayude a interpretar sus emociones, analizar los comportamientos de David y decidir el mejor camino para ella. '),
(24,5, 6, 2, 'Esta no es solo una historia de amor; es una exploración de los límites, la confianza y la búsqueda del bienestar personal. Toma decisiones con sabiduría, y recuerda: cada elección cuenta.'),
(25,5, 7, 2, '¿Empezamos?'),
 --CAP 1
(26,6, 1, 1, 'Bueno, aquí estamos, en nuestro primer hogar juntos. ¿Qué te parece? ¿Te imaginas todas las cosas increíbles que vamos a vivir aquí?'),
(27,6, 2, 1, 'Claro que sí. Todo es nuevo, pero estoy seguro de que juntos podemos con esto. Este lugar será nuestro refugio, ¿no te parece?'),
(28,6, 3, 1, 'Sabía que lo verías así. Estar aquí contigo hace que todo sea más fácil y emocionante'),
(29,6, 4, 1, 'Lo entiendo, Bella. Este paso es grande para ambos. Pero estoy aquí para que lo enfrentemos juntos. Confío en que todo saldrá bien.'),
(30,6, 5, 1, 'No puedo dejar de pensar en lo afortunado que soy. Tenerte aquí conmigo... significa todo para mí. Te quiero, Bella.'),
(31,6, 6, 1, '(David y Bella están en la sala del apartamento. Una mesa de centro nueva, de vidrio y metal, está colocada en el centro. Bella, quien había imaginado un diseño diferente, se detiene frente a ella, sorprendida.)'),
(32,6, 7, 1, 'Sí, me pareció perfecta para el espacio. Sabía que teníamos que decidirlo juntos, pero la vi en oferta y no pude resistirme. ¿A que se ve increíble?' ),
(33,6, 8, 1, '(Con una sonrisa tranquilizadora.)—Entiendo lo que dices, Bella, pero pensé que sería más fácil si me adelantaba con esto. Además, estoy seguro de que te acostumbrarás a ella. Solo quiero que todo esté perfecto para los dos.'),
(34,6, 9, 1, 'Sé que no es lo que imaginabas, pero créeme, una mesa rústica no habría quedado bien aquí. Además, no quería que te estresaras con más decisiones.'),
(35,6, 10,1, ' Y las tomaremos , no te preocupes , solo pensaba que podia quitar estres si esto nos lo quitabamos de encima rapido'),
(36,6, 11,1, 'Bella, entiendo que querías algo diferente, pero ¿no crees que es mejor centrarnos en disfrutar de nuestro espacio en lugar de preocuparnos por detalles? Quiero que esto sea un hogar para los dos, no que se convierta en una fuente de estrés.'),
(37,6, 12,1, '¿Vale?'),
(38,6, 13,1, 'Sabes, Bella, lo último que quiero es que te sientas mal por esto. Tal vez no siempre lo haga perfecto, pero quiero que sepas que siempre pienso en lo mejor para los dos.'),
(39,6, 14,NULL, '(La mañana pasa entre quehaceres y otras cosas)'),
(40,6, 15,NULL, '(Un rato más tarde, abres la puerta de casa. Un delicioso aroma flota en el aire, envolviendo el ambiente con una calidez inesperada.)'),
(41,7, 1, 1, '(David aparece en la cocina, con una sonrisa tímida.)— ¡Sorpresa!'),
(42,7, 2, 1, '(Acercándose despacio) — Quería hacerte una pequeña sorpresa… para compensar el pequeño rifirrafe de esta mañana. No podía dejarlo así.'),
(43,7, 3, 1, 'Sabía que te gustaría. Quiero que sepas cuánto valoro que estés aquí conmigo. No quiero que nada nos afecte, ni siquiera esas pequeñas diferencias.'),
(44,7, 4, 1, 'Eso es todo lo que quiero, Bella. Que te sientas feliz y segura aquí conmigo. Sé que no siempre acierto, pero lo importante es que estamos juntos, ¿verdad?' ),
(45,7, 5, 1, '(Con una ligera sonrisa tensa.)Entiendo lo que dices, Bella. No quiero que pienses que no valoro tus opiniones. Es solo que a veces siento que tiendes a preocuparte demasiado por cosas pequeñas. Quiero que confíes en que siempre buscaré lo mejor para nosotros.'),
(46,7, 6, 1, 'Por supuesto que ambos tenemos voz, Bella. No quiero que sientas lo contrario. Solo fue una tonteria. Vamos a dejar esto atrás, ¿sí?'),
(47,7, 7, 1, '(Sonríe con alivio, relajando la tensión en su expresión.) —Exactamente, Bella. No quiero que te preocupes por cosas que yo puedo manejar. Confía en mí, todo lo que hago es pensando en nosotros.'),
(48,7, 8, 1, 'Entiendo que es importante para ti, Bella. Quizás soy yo quien necesita relajarse un poco. Estoy acostumbrado a tomar decisiones rápidas, pero puedo intentar ser más considerado contigo. Asi que olvidemoslo '),
(49,8, 1, NULL, '(El día sigue avanzando '),
(50,8, 2, NULL, '(Y Bella acaba de volver de comprar unas cosas )'),
(51,8, 2, 1, '(Bella entra al apartamento con una sonrisa, cargando la revista. David levanta la vista al verla y sonríe, pero sus ojos reflejan una mezcla de sorpresa y ligera molestia.)' ),
(52,8 ,2, 1, '¡Vaya, mira quién ha llegado! ¿Dónde te habías metido? Pensé que todavía estabas por aquí.'),
(53,8 ,3, 1, 'Sí, lo sé, pero... ¿qué pasa si algo te ocurre? Es una ciudad nueva para nosotros, y no siempre es seguro andar sola. Tal vez deberías avisarme cada vez que vayas a salir. Así no estaré aquí pensando en si estás bien o no.'),
(54,8, 4, 1, '(En tono de broma)Lo sé, lo sé. Pero imagínate si pasa algo y no sé dónde estás... Tal vez deberías avisarme cada vez que salgas, ¿no crees? Así me evitas preocupaciones.'),
(55,8, 5, 1, 'No es para tanto. Solo quiero asegurarme de que estés bien. Sabes que no podría soportar que algo le pasara a esa cara bonita.'),
(56,8, 6, 1, 'Lo sé, Bella, pero no cuesta nada enviar un mensaje rápido. No es que quiera controlarte, es solo que me preocupo por ti. ¿No te gustaría saber dónde estoy si fuera al revés?'),
(57,8, 7, 1, 'Exacto. No quiero que esto se sienta como una obligación, pero si me envías un mensaje rápido, me sentiré más tranquilo.'),
(58,8, 8, 1, 'Entiendo. No quiero que sientas que es una obligación, pero me hace sentir más tranquilo saber que estás bien. No es tanto pedir, ¿verdad?'),
(59,8, 9, 1, 'Bueno, ahora que estás aquí, ¿por qué no me enseñas esa revista? Seguro que encontramos algo interesante para decorar.'),
(60,8, 10,NULL,'¡Enhorabuena!'),
--CAP 2
(61,9, 1 , NULL, '(Bella sale de la habitación vestida con jeans ajustados y una camiseta escotada. David, sentado en el sofá, levanta la vista hacia ella, sonriendo con calma.)'),
(62,9,2,1, 'Vaya, te ves muy bien, guapa. Aunque… me acordé de esa blusa que te regalé hace poco. La azul, ¿te acuerdas? Creo que te queda espectacular.'),
(63,9,3,1, '(Se levanta del sofá y se acerca a ella con un tono cariñoso.)Lo sé, lo sé, pero esa blusa que te di te hace ver aún más increíble. Tiene ese toque elegante que siempre me encanta. ¿No te gustaría ponértela?'),
(64,9,4,1,'(Ríe suavemente y la abraza por la cintura.)—Claro que me gusta. Siempre te ves hermosa, pero es que la otra blusa tiene algo especial. No sé, siento que te hace resaltar más, como la reina que eres.'),
(65,9,5,1, '(Sonríe con satisfacción.)—Sabía que me entenderías. Ya verás, vas a deslumbrar a todos.'),
(66,9,6,1, '—¡No es eso, para nada! Me encanta cómo te ves siempre. Solo que a veces pienso que ciertas prendas resaltan aún más tu belleza. Quiero verte brillar.'),
(67,9,7,1, '— No quiero que lo tomes a mal. Es solo que pensé que te gustaba mucho… pero si prefieres ponerte otra cosa, está bien.'),
(68,9,8,1, '(Asiente con una sonrisa, intentando parecer comprensivo.)—Lo sé. No quiero que sientas presión. Es solo una sugerencia. Tú decides lo que quieras.'),
(69,9,9,NULL,'(Bella vuelve con la blusa que David le regaló. Él sonríe con satisfacción, observándola con admiración.)'),
(70,9,10,1, 'Ahora sí. Te ves espectacular, como siempre.'),
(71, 9, 11, 1, '(Con tono animado, buscando cerrar la tensión.)—Venga, que vamos a llegar tarde a la fiesta. Y ya verás, vas a ser la más guapa de todas.'),
(72, 10,1,NULL, '(David y Bella han acudido a una fiesta de varios amigos , La música suena de fondo, y el ambiente está lleno de risas y charlas.)'),
(73, 10,2,NULL, '(Bella se encuentra conversando con Alex, un viejo amigo que no había visto en mucho tiempo.)'),
(74, 10,3,3,'¡Bella! ¡Cuánto tiempo sin verte! Ni me acordaba de la última vez que coincidimos. Creo que fue en aquella reunión en casa de Julia, ¿verdad?' ),
(75, 10,4,3, 'No me puedo quejar. Trabajo, proyectos... ya sabes, lo típico. Pero y tú, ¿qué tal? Escuché que te mudaste con tu pareja. ¿Cómo va eso?'),
(76,10,5,3, 'Eso suena emocionante, pero también agotador. Mudarse siempre es un caos. Espero que te esté yendo bien con todo. ¿Y David? ¿Está por aquí?'),
(77,10,6,3, 'No te preocupes. Seguro que en algún momento nos cruzamos. Me alegra verte tan bien, Bella. Siempre me pareciste una persona llena de energía.'),
(78,11,1,NULL, '(Más tarde llegais a casa, David cierra la puerta detrás de ellos y se quita la chaqueta. Bella, todavía animada por la fiesta, se dirige a la cocina por un vaso de agua.)'),
(79,11,2,1,'(Con una sonrisa algo tensa.)—Sí, estuvo bien... aunque hubo algunas cosas que me hicieron sentir un poco incómodo.'),
(80,11,3,1, 'Bueno... No es nada serio, pero noté que hablaste mucho con ese chico, Alex, ¿no? Parecían bastante cercanos.'),
(81,11,4,1,'Lo sé, Bella, pero… ya sabes cómo son algunas personas. No siempre tienen buenas intenciones,y no me gustaría que nadie malinterpretara las cosas.'),
(82,11,5,1,'No es que tú lo hayas hecho. Pero hay hombres que, cuando ven a una chica simpática, pueden confundirse. Solo quiero evitar que te metas en situaciones incómodas.'),
(83,11,6,1,'Lo sé, Bella, pero es diferente ahora. Estamos juntos, y la gente puede percibir las cosas de forma distinta. Me preocupo porque eres importante para mí. No es para controlarte, es solo un consejo.'),
(84,11,7,1,'Gracias, Bella. Eso es todo lo que quiero. No es para controlarte, es solo para que evitemos situaciones incómodas. Sé que a veces soy demasiado protector.'),
(85,11,8,1,'No es para tanto, Bella. Es solo un consejo. Yo confío en ti, pero también quiero evitar situaciones que puedan ser incómodas para los dos.'),
(86,11,9,1,'(Se acerca, la toma de la mano y le da un beso en la frente.) —Sabía que me entenderías. Todo lo que hago es porque me importas.'),
(87,11,10,1,'Claro, no te estoy pidiendo que cambies, Bella. Solo que seas un poco más consciente de estas situaciones. No es para limitarte, es solo para evitar incomodidades innecesarias.'),
(88,11,11,1,'(Sonríe aliviado y la toma suavemente de la mano.) —Eso es todo lo que quiero, Bella. Confío en ti. Gracias por entenderme.'),
(89,11,12,1,'Bueno, dejando eso de lado, ¿qué tal si nos relajamos un poco? ¿Vemos algo en la televisión antes de dormir?'),
(90,11,13,1,'(Ríe ligeramente.)—¿Esa? Sí, claro. Lo que tú quieras, guapa. Esta noche es tuya.'),
(91,11,14,1, 'Y, oye, me alegra que podamos hablar de estas cosas. Lo importante es que siempre estemos bien.');
--INTRO H2
--(26,13, 1, NULL, 'Historia2');


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
--INTRO H1
(19,'Continuar', 0, 0, 20, NULL),
(20,'Continuar', 0, 0, 21, NULL),
(21,'Continuar', 0, 0, 22, NULL),
(22,'Continuar', 0, 0, 23, NULL),
(23,'Continuar', 0, 0, 24, NULL),
(24,'Continuar', 0, 0, 25, NULL),
(25,'¡Si! Vamos allá', 0, 0, NULL, NULL),
(25,'No, ¿Puedes repetir?', 0, 0, 19, NULL),
--CAP 1
(26,'Sí, es increíble. Es un gran paso para los dos. Aunque siento que hay tanto por hacer aún...', 0, 0, 27, NULL),
(27,'Sí, tienes razón. Ya puedo imaginar todos los momentos felices que viviremos aquí. Estoy tan feliz de haber tomado esta decisión contigo.', 3, 0, 28, NULL),
(27,'Espero que sí... es un gran cambio, pero estoy dispuesta a intentarlo contigo. Necesitaré un poco de tiempo para adaptarme', 0, 3, 29, NULL),
(28,'(Continuar)', 0, 0, 30, NULL),
(29,'Si... tienes razón', 0, 0, 30, NULL),
(30,'Yo también te quiero, David.', 0, 0, 31, NULL),
(31, '¿Es esta la mesa que mencionaste ayer?',0,0,32,NULL ),
(32, 'Es una mesa bonita, pero me hubiera gustado que me consultaras antes de comprarla.', 0,3,33, NULL ),
(32, 'Sí, se ve bien, aunque no estoy segura de que sea lo que imaginaba para este espacio.', 3,0,34, NULL),
(33, 'Lo entiendo, pero creo que es importante que tomemos estas decisiones juntos.' ,0,0,35, NULL),
(34, 'Tal vez tengas razón, aunque sigo pensando que una mesa más cálida habría funcionado mejor.', 0,0,36, NULL),
(35, '(Continuar)', 0,0, 38, NULL),
(36, '(Continuar)', 0,0, 37, NULL),
(37, '(Asiente)', 0,0, 38, NULL),
(38, 'Lo sé, David. Aprecio que intentes cuidar de nosotros. ', 0,0, 39, NULL),
(39, '(Continua)', 0,0,40, NULL),
(40, '(mirando alrededor, intrigada) — ¿David?', 0,0 ,NULL, NULL),
(41, '(Cruzándose de brazos pero con una leve sonrisa) — ¿Qué es esto?', 0,0,42,NULL),
(42, '(Con una ceja levantada, claramente intrigada) — Wow. Esto es mucho trabajo. No me esperaba algo así.', 0,0,43, NULL),
(43, ' Gracias, David. Aprecio mucho lo que haces',3,0,44,NULL),
(43, 'Es un gesto muy bonito, pero creo que deberiamos hablar sobre cómo tomaremos decisiones juntos.',0,3,45, NULL),
(44, 'Es importante que estemos juntos, y también lo es que ambos sintamos que tenemos voz en las decisiones.',0,0,46,NULL),
(45, 'Quizás tengas razón. Supongo que es cuestión de confiar más en tus decisiones.' ,3,0,47, NULL),
(45, 'Pero para mí son importantes, y quiero que se refleje en cómo construimos este espacio juntos.',0,3,48,NULL),
(46, ' (Continuar)' ,0,0,NULL,NULL),
(47, '(Continuar)', 0,0,NULL, NULL),
(48, '(Continuar)',0,0,NULL, NULL),
(49, '(Continuar)',0,0,50, NULL),
(50, '(Continuar)',0,0,51, NULL),
(51, 'Salí a comprar una revista para ver ideas de decoración.',0,0,52, NULL),
(52, '(Sonríe, pensando que es solo un comentario.)—David, solo fue a la tienda de la esquina. No estuve fuera mucho tiempo.', 0,0,53,NULL),
(53, '(Ríe ligeramente.)Vaya, qué considerado eres. No sabía que eras tan protector. Supongo que podría hacerlo... aunque suena un poco excesivo.',3,0,55,NULL ),
(54, '(Ríe, pero siente una leve incomodidad.)¿Avisarte cada vez que salga? Vaya, suena un poco exagerado. Estoy bien, David, solo fui a dar una vuelta rápida.' ,0,3,56,NULL),
(55, 'Lo entiendo. Quizá sea bueno que lo haga, para que no te preocupes demasiado.',0,0, 57,NULL),
(56, 'No es que me parezca mal, pero creo que es importante que ambos podamos salir sin sentirnos monitoreados todo el tiempo. ',0,0,58, NULL),
(57, 'Está bien. No quiero que te sientas ansioso por estas cosas', 0,0, 59, NULL),
(58, 'No, no es malo querer cuidarme... Está bien, David. Te enviaré un mensaje rápido cuando salga, para que no te preocupes.', 0,0, 59,NULL),
(59, 'Claro. Hay varias ideas que podrían quedarle bien al apartamento. Vamos a verlas juntos.', 0,0, 60,NULL),
(60, 'FIN CAPITULO 1',0,0,NULL, NULL),
--CAP 2
(61, 'Ya estoy lista ,¿ nos vamos ?', 0,0, 62, NULL),
(62, '(Sonríe mientras busca su bolso.) —Sí, claro que me acuerdo. Es bonita, pero hoy quiero llevar esto. Me gusta cómo se ve. Además, hace calor. ',0,0,63,NULL),
(63, '(Frunce el ceño ligeramente, sin perder la sonrisa.)—¿Por qué? ¿No te gusta cómo me veo con esto? ',0,0,64,NULL),
(64, '(Ríe suavemente, cediendo.) —Bueno, ya que insistes... Supongo que puedo ponerme la blusa que me regalaste. ',5,2,65,NULL),
(64, '(Se cruza de brazos, con un tono más curioso.)—David,parece que no te gusta que me ponga lo que quiero.',0,0,66, NULL),
(65, '(Sonríe) -Venga me cambio rápido que vamos a llegar tarde',0,0, 69,NULL),
(66, 'Entiendo lo que dices, pero también me gusta ponerme lo que me hace sentir bien. Hoy quiero usar esto.',0,0,67, NULL),
(67, '(Suspira)—No es que no me guste la blusa, David. Me la he puesto varias veces. Solo que hoy prefería otra cosa...', 0,0,68,NULL),
(68, '—Está bien… me pondré la blusa. No quiero que te quedes con la idea de que no me gusta. Ya sabes que me encantan todos los regalos que me haces',0,0,69,NULL),
(69, '(Continuar)',0,0,70,null),
(70, '(Sonríe.)—Gracias, David.',0,0,71,NULL),
(71, 'Jajaja, eso es porque tú me ves con buenos ojos.',0,0,NULL,NULL),
(72,'(Continuar)',0,0,73,NULL),
(73, '(Continuar)',0,0,74,NULL),
(74, '(Sonríe animada.)—¡Sí, es verdad! Ha pasado bastante tiempo. Me alegra mucho verte aquí. ¿Cómo te ha ido?',0,0,75,NULL),
(75, 'Sí, me mudé hace poco con David. Ha sido un gran paso, pero la verdad es que estamos adaptándonos bien. Aunque todavía hay mucho por organizar en el apartamento. ',0,0,76,NULL),
(76,'Sí, está por ahí hablando con algunos amigos. Quería presentarte, pero parece que está entretenido.',0,0,77,NULL),
(77, '(Ríe ligeramente.)—Gracias, Alex. Es lindo escuchar eso.',0,0,NULL,NULL),
(78, '¡Fue una noche genial! Hacía mucho que no veía a todos. Me alegra que hayamos ido. ¿Te divertiste?',0,0,79,NULL),
(79,'(Frunce ligeramente el ceño.)—¿Incómodo? ¿Por qué? ¿Pasó algo?',0,0,80,NULL),
(80, '(Ríe ligeramente.)—¿Alex? Es solo un amigo. Nos conocemos desde hace tiempo. Solo estábamos poniéndonos al día. ',0,0,81,NULL),
(81, '¿Malinterpretar? ¿Por qué alguien haría eso? No creo que haya dado motivos para que piensen algo raro.',0,0,82,null),
(82, 'David, sé cuidarme. No voy a cambiar cómo interactúo con la gente solo por miedo a lo que otros puedan pensar.',0,0,83,NULL),
(83, 'David… no quiero sentir que estoy siendo juzgada por socializar con mis amigos. Pero entiendo que te preocupes asi que tal vez pueda ser un poco más reservada en futuras reuniones',5,2,84,NULL),
(83, 'David, entiendo que te preocupes, pero me siento un poco incómoda con esto. Es como si estuviera bajo vigilancia por cómo otros puedan interpretar mis acciones.',2,5,85,NULL),
(84, '(Asiente lentamente, con una sonrisa forzada.)—Está bien. Intentaré tener más cuidado. ',0,0,86,NULL),
(85, 'Entiendo lo que dices, pero tampoco quiero cambiar completamente la forma en que soy. No creo que deba preocuparme tanto por cómo lo vean otros.',0,0,87,NULL),
(86, '(Suspira internamente, intentando convencerse de que está bien.)—Espero que podamos manejar estas cosas juntos, sin que se convierta en un problema. ',0,0, 89, NULL),
(87, 'De acuerdo… tal vez pueda ser un poco más cuidadosa. Pero también necesito que confíes en que sé lo que estoy haciendo. ',0,0,88,NULL),
(88, '(Sonríe ligeramente, aunque todavía siente una pequeña incomodidad interna.)—Espero que podamos encontrar un equilibrio en esto ', 0,0,89,NULL),
(89, 'Sí, me parece bien. De hecho, llevo tiempo queriendo ver esa película que me encanta. ¿Te acuerdas? ',0,0,90,NULL),
(90, ' (David la mira con afecto, tratando de reconectar emocionalmente. Bella siente que el ambiente se ha suavizado y se sientan juntos en el sofá.) ',0,0,91,NULL),
(91, '(David rodea a Bella con un brazo, mientras ella pone la película. Ambos se relajan en el sofá, disfrutando de un momento tranquilo)',0,0,NULL,NULL);






--INTRO H2
--(25,'Capitulo 1 Historia2', 0, 0, NULL, NULL);

INSERT INTO player_decision_state (id, player_id, game_id, decision_id, option_id, completed)
VALUES
(1,1,1,19,20,1),
(2,1,1,20,21,1),
(3,1,1,21,22,1),
(4,1,1,22,24,1),
(5,1,1,23,25,1),
(6,1,1,24,26,1);
