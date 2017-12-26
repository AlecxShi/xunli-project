DROP TABLE app_version_info;
CREATE TABLE app_version_info(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    c_version VARCHAR(125) NOT NULL,
    p_version VARCHAR(125) NOT NULL,
    v_desc VARCHAR (512) ,
    file_name VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    if_use VARCHAR(1) DEFAULT 'N',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(255),
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255)
);