CREATE TABLE Sessions
(
	id BIGSERIAL,
	project varchar(20) REFERENCES Projects(id),
	date timestamp(0),
	PRIMARY KEY(id) 
);
