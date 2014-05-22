CREATE TABLE Follows
(
	id BIGSERIAL,
	login VARCHAR(20),
	PRIMARY KEY(login,id),
	FOREIGN KEY (id) REFERENCES Documents(id),
	FOREIGN KEY (login) REFERENCES Users(login)
);
