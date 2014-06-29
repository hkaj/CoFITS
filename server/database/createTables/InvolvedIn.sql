CREATE TABLE InvolvedIn
(
	login varchar(20),
	project varchar(20),
	admin boolean not null,
	FOREIGN KEY(login) REFERENCES Users,
	PRIMARY KEY(project,login)
);
