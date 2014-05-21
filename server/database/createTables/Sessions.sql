CREATE TABLE Sessions
(
	name varchar(20) REFERENCES Projects(name),
	date timestamp(0),
	PRIMARY KEY(name, date) 
);
