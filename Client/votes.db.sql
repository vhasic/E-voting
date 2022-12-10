BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS vote
(
    id           integer not null
        constraint vote_pk
            primary key autoincrement
);


END TRANSACTION;