DROP TABLE persistent_logins;
CREATE TABLE persistent_logins (series VARCHAR(255) NOT NULL, user_id BIGINT NOT NULL , token NVARCHAR(255) NOT NULL, last_used TIMESTAMP, ip_address VARCHAR(255) , user_agent VARCHAR(255) );

