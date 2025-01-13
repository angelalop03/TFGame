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
(1, 'News Title 1', 'This is the content of the first news item.', 'libgdx.png'),
(2, 'News Title 2', 'Content for the second news item.', 'libgdx.png'),
(3, 'News Title 3', 'Detailed content of the third news item.', 'libgdx.png');




