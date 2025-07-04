create table if not exists test(
    id serial primary key,
    name varchar(100),
    start_zeit_punkt timestamp,
    end_zeit_punkt timestamp,
    ergebnis_zeit_punkt timestamp
);

create table if not exists frage(
    fragestellung varchar(500),
    punktzahl integer,
    erklaerung varchar(500),
    antwort_moeglichkeiten varchar(200),
    korrekte_antworten varchar(200),
    test integer references test(id),
    test_key integer
);