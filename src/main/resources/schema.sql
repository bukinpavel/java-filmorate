DELETE FROM USER_FRIENDS ;
ALTER TABLE USER_FRIENDS ALTER COLUMN ID RESTART WITH 1;
DELETE FROM USERS_LIKE ;
ALTER TABLE USERS_LIKE ALTER COLUMN id RESTART WITH 1;
DELETE FROM USERS ;
ALTER TABLE USERS ALTER COLUMN id RESTART WITH 1;
DELETE FROM FILM_GENRE ;
ALTER TABLE FILM_GENRE ALTER COLUMN id RESTART WITH 1;
DELETE FROM GENRES ;
ALTER TABLE GENRES ALTER COLUMN id RESTART WITH 1;
DELETE FROM RATINGS ;
ALTER TABLE RATINGS ALTER COLUMN id RESTART WITH 1;
DELETE FROM FILM_RATING ;
ALTER TABLE FILM_RATING ALTER COLUMN id RESTART WITH 1;
DELETE FROM FILMS ;
ALTER TABLE FILMS ALTER COLUMN id RESTART WITH 1;

create table if not exists users
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    email         varchar(64) not null,
    login         varchar(64),
    name          varchar(64),
    birthday      timestamp
);

create table if not exists user_friends
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    user_id       int,
    friend_id       int,
    CONSTRAINT fr_user_friends
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

create table if not exists user_request
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    user_id       int,
    friend_id       int,
    CONSTRAINT fk_user_friends
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

create table if not exists films
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name          varchar(64) not null,
    description   varchar(600),
    release_date  timestamp,
    duration      int
);

create table if not exists users_like
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    user_id       int,
    film_id       int,
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_film_id
        FOREIGN KEY (film_id)
            REFERENCES films (id)
);

create table if not exists genres
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name          varchar(64) not null
);

create table if not exists film_genre
(
    id        int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    genres_id int,
    film_id   int,
    /*
    CONSTRAINT fk_film_genre
        FOREIGN KEY (genres_id)
            REFERENCES genres (id)
     */
    CONSTRAINT fk_genre_rating
        FOREIGN KEY (film_id)
            REFERENCES films (id)
);
create table if not exists ratings
(
    id            int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    name          varchar(64) not null
);

create table if not exists film_rating
(
    id         int generated by default as identity primary key, -- идентификатор целочисленный, автоинкрементный
    ratings_id int,
    film_id    int,
    /*
    CONSTRAINT fk_film_rating
        FOREIGN KEY (ratings_id)
            REFERENCES ratings (id)
     */
    CONSTRAINT fk_film_rating
        FOREIGN KEY (film_id)
            REFERENCES films (id)
);


