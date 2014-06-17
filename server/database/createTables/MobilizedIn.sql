CREATE TABLE MobilizedIn
(
	document BIGSERIAL,
	session BIGSERIAL,
	PRIMARY KEY (document, session),
	FOREIGN KEY (document) REFERENCES Documents(id),
	FOREIGN KEY (session) REFERENCES Sessions(id)
);
