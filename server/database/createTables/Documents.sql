CREATE TABLE Documents
(
	id BIGSERIAL,
	name VARCHAR(255) NOT NULL,
	type VARCHAR(255) NOT NULL,
	owner VARCHAR(10),
	PRIMARY KEY(id),
	FOREIGN KEY(owner) REFERENCES Users(login)
);