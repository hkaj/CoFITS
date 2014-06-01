CREATE TABLE Projects
(
	id varchar(20),
	name varchar(20),
	description text,
	creator varchar(10) REFERENCES Users(login),
	PRIMARY KEY(id)
);
