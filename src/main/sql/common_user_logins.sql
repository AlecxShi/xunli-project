DROP TABLE common_user_logins;
CREATE TABLE common_user_logins (
    series VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL ,
    token NVARCHAR(255) NOT NULL,
    code NVARCHAR(6) NOT NULL,
    code_expire TIMESTAMP,
    last_used TIMESTAMP,
    ip_address VARCHAR(255) ,
    user_agent VARCHAR(255)
);

