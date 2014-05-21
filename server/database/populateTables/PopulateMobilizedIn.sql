INSERT INTO MobilizedIn(id,name,date)
(
	 SELECT (id),
		'projet ia03',
		to_timestamp('12-11-2013 10:36:38', 'dd-mm-yyyy hh24:mi:ss') 
	FROM Documents WHERE name = 'terminaux minitel'
);

INSERT INTO MobilizedIn(id,name,date)
(
	SELECT (id),
		'projet ia03',
		to_timestamp('11-11-2013 18:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'rapport écosystèmes numériques'
);

INSERT INTO MobilizedIn(id,name,date)
(
	SELECT (id),
		'projet nf29',
		to_timestamp('18-09-2013 15:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'schema opml'
);

INSERT INTO MobilizedIn(id,name,date)
(
	SELECT (id),
		'projet sr04',
		to_timestamp('03-12-2013 13:36:38', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(id,name,date)
(
	SELECT (id),
		'projet sr04',
		to_timestamp('02-12-2013 10:44:39', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO MobilizedIn(id,name,date)
(
	SELECT (id),
		'projet sr04',
		to_timestamp('02-12-2013 10:44:39', 'dd-mm-yyyy hh24:mi:ss')
	FROM Documents WHERE name = 'whitepaper tags RFID'
);
