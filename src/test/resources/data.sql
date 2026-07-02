INSERT INTO USERS (USER_NAME, USER_LOGIN, USER_EMAIL, BIRTHDAY)
VALUES ('dolore', 'Nick Name', 'mail@mail.ru', '1946-08-20'),
       ('friend', 'friend adipisicing', 'friend@mail.ru', '1976-08-20'),
       ('another_friend', 'friend 2', 'friend2@mail.ru', '1985-09-30');

INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_RATE)
VALUES ('labore nulla', 'Duis in consequat esse', '1979-04-17', '100', '3', '1'),
       ('Film Updated', 'New film update decription', '1989-04-17', '190', '5', '5'),
       ('New film', 'New film about friends', '1999-04-30', '120', '8', '3');

INSERT INTO FILMGENRES (FILM_ID, GENRE_ID)
VALUES ('3', '1'),
       ('2', '5'),
       ('3', '2'),
       ('3', '3');

INSERT INTO FILMLIKERS (FILM_ID, USER_ID)
VALUES ('3', '1'),
       ('1', '1'),
       ('1', '2');

INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID)
VALUES ('1', '2'),
       ('1', '3'),
       ('2', '3'),
       ('3', '1');