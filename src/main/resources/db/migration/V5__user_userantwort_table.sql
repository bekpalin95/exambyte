create table if not exists studi(
    id serial primary key,
    username varchar(30)
);

create table if not exists user_antwort(
    test_id integer,
    frage_id integer,
    antwort varchar(250),
    studi integer references studi(id),
    studi_key integer
);