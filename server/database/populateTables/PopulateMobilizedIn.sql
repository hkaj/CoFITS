INSERT INTO MobilizedIn(document, session_proj, session_date)
(
	SELECT id, 'projet_nf26_0', to_timestamp('16-09-2013 15:36:38', 'dd-mm-yyyy hh24:mi:ss') 
	FROM Documents WHERE name = 'terminaux minitel'
);

INSERT INTO MobilizedIn(document, session_proj, session_date)
(
	SELECT id, 'projet_nf26_0', to_timestamp('16-09-2013 15:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'rapport écosystèmes numériques'
);

INSERT INTO MobilizedIn(document, session_proj, session_date)
(
	SELECT id, 'projet_ia04_0', to_timestamp('12-11-2013 10:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'schema opml'
);

INSERT INTO MobilizedIn(document, session_proj, session_date)
(
	SELECT id, 'projet_ia04_0', to_timestamp('12-11-2013 10:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(document, session_proj, session_date)
(
	SELECT id, 'projet_nf28_0', to_timestamp('02-12-2013 10:44:39', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(document, session_proj, session_date)
(
	SELECT id, 'projet_nf28_1', to_timestamp('03-12-2013 13:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'whitepaper tags RFID'
);
