CREATE TABLE MobilizedIn
(
	document BIGSERIAL,
	project varchar(20) REFERENCES Projects(id),
	PRIMARY KEY (document, project),
	FOREIGN KEY (document) REFERENCES Documents(id)
);
