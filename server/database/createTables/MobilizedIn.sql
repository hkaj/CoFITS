CREATE TABLE MobilizedIn
(
	id BIGSERIAL,
	name varchar(20) REFERENCES Projects(name),
	date timestamp(0),
	PRIMARY KEY (id,name,date),
	FOREIGN KEY (name,date) REFERENCES Sessions(name,date),
	FOREIGN KEY (id) REFERENCES Documents(id)
	
);
