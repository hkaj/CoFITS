INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 1
	FROM Documents WHERE name = 'terminaux.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 2
	FROM Documents WHERE name = 'image.jpg'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 4
	FROM Documents WHERE name = 'idee.png'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 5
	FROM Documents WHERE name = 'publication.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 6
	FROM Documents WHERE name = 'RFID.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 3
	FROM Documents WHERE name = 'abstract.jpg'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 4
	FROM Documents WHERE name = 'chat.jpg'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 2
	FROM Documents WHERE name = 'pikachu.jpg'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 2
	FROM Documents WHERE name = 'tarte-tatin-aux-pommes.jpg'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 5
	FROM Documents WHERE name = 'android-pie.png'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 1
	FROM Documents WHERE name = 'C1-ia04-introduction.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 1
	FROM Documents WHERE name = 'C6-ia04-sparql.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 7
	FROM Documents WHERE name = 'nf11-analyse-lexicale-ascendante.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 7
	FROM Documents WHERE name = 'nf11-analyse-syntaxique-descendante.pdf'
);
