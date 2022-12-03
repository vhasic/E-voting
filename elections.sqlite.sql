BEGIN TRANSACTION;

-- Only admin (CIK) can add new political party, candidate and ballot
create table if not exists Candidate
(
    id           integer not null
        constraint Candidates_pk
            primary key autoincrement,
    list_number  integer not null unique,
    name_surname TEXT    not null,
    political_party_id integer not null
        constraint Candidate_PoliticalParty_fk
        references PoliticalParty (id)
        on delete restrict
);

create table PoliticalParty
(
    id         integer not null
        constraint PoliticalParty_pk
            primary key autoincrement,
    number     integer not null unique,
    party_name TEXT    not null unique
);

-- Ballot is predefined and one Elections have only dozens of Ballots, for example: Predstavnički dom, Za predsjednika, ...
-- CREATE TABLE Ballot
-- (
--     id                 integer not null
--         constraint Ballot_pk
--             primary key autoincrement,
--     political_party_id integer not null
--         constraint Votes_PoliticalParty_fk
--             references PoliticalParty (id)
--             on delete restrict,
--     candidate_id       integer not null
--         constraint Votes_Candidates_fk
--             references Candidate (id)
--             on delete restrict,
--     ballot_number      integer not null
--         constraint Ballot_number
--             unique
-- );

create table Vote
(
    id            integer not null
        constraint Vote_pk
            primary key autoincrement,
    voting_ballot integer not null
        constraint Vote_Ballot_null_fk
            references Ballot (id)
);


INSERT INTO PoliticalParty (number, party_name) VALUES (1, 'Partija 1');
INSERT INTO PoliticalParty (number, party_name) VALUES (2, 'Partija 2');
INSERT INTO PoliticalParty (number, party_name) VALUES (3, 'Partija 3');

INSERT INTO Candidate (list_number, name_surname, political_party_id) VALUES (1, 'Kandidat 1', 1);
INSERT INTO Candidate (list_number, name_surname, political_party_id) VALUES (2, 'Kandidat 2', 1);
INSERT INTO Candidate (list_number, name_surname, political_party_id) VALUES (3, 'Kandidat 3', 1);
INSERT INTO Candidate (list_number, name_surname, political_party_id) VALUES (4, 'Kandidat 4', 2);
INSERT INTO Candidate (list_number, name_surname, political_party_id) VALUES (5, 'Kandidat 5', 3);
INSERT INTO Candidate (list_number, name_surname, political_party_id) VALUES (6, 'Kandidat 6', 3);

-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (1, 1, abs(random()));
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (1, 1, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (1, 2, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (1, 3, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (2, 4, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (2, 5, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (3, 6, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (3, 7, 1);
-- INSERT INTO Ballot (political_party_id, candidate_id, ballot_number) VALUES (3, 8, 1);

COMMIT;