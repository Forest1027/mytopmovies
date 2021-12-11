EXEC SQL CONNECT TO MTM@localhost:5432 USER forest;

CREATE TABLE IF NOT EXISTS mtm_users (
	id varchar(10) PRIMARY KEY,
	username varchar(15) NOT NULL UNIQUE,
	password varchar(50)NOT NULL,
	email varchar(50),
	is_verified boolean DEFAULT FALSE,
	is_active boolean DEFAULT TRUE 
)