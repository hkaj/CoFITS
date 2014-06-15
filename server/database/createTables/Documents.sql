CREATE TABLE Documents
(
	id BIGSERIAL,
	name VARCHAR(255) NOT NULL,
	type VARCHAR(255) NOT NULL,
	owner VARCHAR(10),
	last_modified timestamp(0),
	PRIMARY KEY(id)
);
