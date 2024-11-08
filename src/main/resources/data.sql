-- Вставка данных в таблицу mpa_rating
INSERT INTO mpa_rating (mpa_rating_id, name) VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

-- Вставка данных в таблицу friend_status
INSERT INTO friendship_status (friendship_status_id, name) VALUES
(1, 'Pending'),
(2, 'Accepted'),
(3, 'Declined');

-- Вставка данных в таблицу genre
INSERT INTO genre (genre_id, name) VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');
