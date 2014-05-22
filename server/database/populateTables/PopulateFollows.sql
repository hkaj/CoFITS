INSERT INTO Follows(login, id)
(
	SELECT 'cmoulin',(id) FROM Documents WHERE name = 'terminaux minitel'
);

INSERT INTO Follows(login, id)
(
	SELECT 	'teteia03',(id) FROM Documents WHERE name = 'rapport écosystèmes numériques'
);


INSERT INTO Follows(login, id)
(
	SELECT 'narichar',(id) FROM Documents WHERE name = 'schema opml'
);

INSERT INTO Follows(login, id)
(
	SELECT 'titi',(id) FROM Documents WHERE name = 'schema opml'
);

INSERT INTO Follows(login, id)
(
	SELECT 'totosr04',(id) FROM Documents WHERE name = 'publication RFID'
);

INSERT INTO Follows(login, id)
(
	SELECT 'totosr04',(id) FROM Documents WHERE name = 'whitepaper tags RFID'
);
