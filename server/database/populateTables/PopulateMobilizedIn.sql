INSERT INTO MobilizedIn(document, project)
(
	SELECT id, 'projet_nf26_0'
	FROM Documents WHERE name = 'terminaux minitel'
);

INSERT INTO MobilizedIn(document, project)
(
	SELECT id, 'projet_nf26_0'
	FROM Documents WHERE name = 'rapport écosystèmes numériques'
);

INSERT INTO MobilizedIn(document, project)
(
	SELECT id, 'projet_ia04_0'
	FROM Documents WHERE name = 'schema opml'
);

INSERT INTO MobilizedIn(document, project)
(
	SELECT id, 'projet_ia04_0'
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(document, project)
(
	SELECT id, 'projet_nf28_0'
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(document, project)
(
	SELECT id, 'projet_nf28_1'
	FROM Documents WHERE name = 'whitepaper tags RFID'
);
