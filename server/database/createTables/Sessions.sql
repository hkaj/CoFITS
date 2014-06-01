CREATE TABLE Sessions
(
	project varchar(20) REFERENCES Projects(id),
	date timestamp(0),
	PRIMARY KEY(project, date) 
);
