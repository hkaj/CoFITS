INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 1
	FROM Documents WHERE name = 'terminaux'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 2
	FROM Documents WHERE name = 'image'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 4
	FROM Documents WHERE name = 'idee'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 5
	FROM Documents WHERE name = 'publication.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 6
	FROM Documents WHERE name = 'publication.pdf'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 6
	FROM Documents WHERE name = 'RFID'
);
