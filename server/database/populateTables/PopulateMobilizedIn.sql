INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 1
	FROM Documents WHERE name = 'terminaux minitel'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 2
	FROM Documents WHERE name = 'rapport écosystèmes numériques'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 4
	FROM Documents WHERE name = 'schema opml'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 5
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 6
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(document, session)
(
	SELECT id, 6
	FROM Documents WHERE name = 'whitepaper tags RFID'
);
