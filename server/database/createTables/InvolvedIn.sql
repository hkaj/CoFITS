CREATE TABLE InvolvedIn
(
	login varchar(20),
	project varchar(20),
	admin boolean not null,
	PRIMARY KEY(project,login),
	FOREIGN KEY project REFERENCES Projects
);
