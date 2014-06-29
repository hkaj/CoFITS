CREATE TABLE MobilizedIn
(
	document BIGSERIAL,
	session_proj varchar(20),
	session_date timestamp(0),
	PRIMARY KEY (document, session_proj, session_date),
	FOREIGN KEY (document) REFERENCES Documents(id),
	FOREIGN KEY (session_proj, session_date) REFERENCES Sessions(project, date)
);
