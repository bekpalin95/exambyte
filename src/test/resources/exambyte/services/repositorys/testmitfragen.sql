insert into test(name) values('test');

INSERT INTO frage (fragestellung, punktzahl, erklaerung, antwort_moeglichkeiten, korrekte_antworten, test, test_key)
VALUES ('hey',2,'erkl','ajaj;kak','aahhh',(SELECT id FROM test WHERE name = 'test'),1);

INSERT INTO frage (fragestellung, punktzahl, erklaerung, antwort_moeglichkeiten, korrekte_antworten, test, test_key)
VALUES ('h233y',55,'erkl','ajaj;kak','aahhh',(SELECT id FROM test WHERE name = 'test'),2);
