CREATE TABLE InvolvedIn
(
	login varchar(20),
	name varchar(20),
	date timestamp(0),
	FOREIGN KEY(login) REFERENCES Users,
	FOREIGN KEY(name,date) REFERENCES Sessions,
	PRIMARY KEY(name,date,login)
)
