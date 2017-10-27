DROP TABLE persistent_logins;
CREATE TABLE persistent_logins (series VARCHAR(255) NOT NULL, user_id BIGINT NOT NULL , token NVARCHAR(255) NOT NULL, last_used TIMESTAMP, ip_address VARCHAR(255) , user_agent VARCHAR(255) );
DROP TABLE robot_user_logins;
CREATE TABLE robot_user_logins (user_id BIGINT NOT NULL PRIMARY KEY,msg_count INT NOT NULL DEFAULT 0,last_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)

