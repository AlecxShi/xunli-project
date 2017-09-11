DROP TABLE article_info;
CREATE TABLE article_info(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    icon_path VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    read_count INT NOT NULL DEFAULT 0,
    share_count INT NOT NULL DEFAULT 0,
    if_publish CHAR(1) NOT NULL DEFAULT 'N',
    create_user VARCHAR(255),
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)