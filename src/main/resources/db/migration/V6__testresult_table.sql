create table if not exists test_result(
    test_id integer,
    erreichte_punkte integer,
    bestanden boolean,
    studi integer references studi(id),
    studi_key integer
);