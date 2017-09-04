DROP TABLE common_user_logins;
CREATE TABLE common_user_logins (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    token NVARCHAR(255) NOT NULL,
    last_used TIMESTAMP,
    ip_address VARCHAR(255) ,
    user_agent VARCHAR(255),
    expire_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

