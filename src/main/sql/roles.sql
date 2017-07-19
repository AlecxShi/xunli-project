DROP TABLE roles;
CREATE TABLE roles (id BIGINT NOT NULL  PRIMARY KEY AUTO_INCREMENT, role_code VARCHAR(50) NOT NULL, role_name VARCHAR(50) NOT NULL, role_desc VARCHAR(200) , is_sys BIT, created_by VARCHAR(50), created_date TIMESTAMP, last_modified_by VARCHAR(50) , last_modified_date TIMESTAMP);
INSERT INTO roles (role_code, role_name, role_desc, is_sys, created_by, created_date, last_modified_by, last_modified_date) VALUES ( 'admin', '管理员', '管理员', true, 'system', '2016-05-03 17:43:14', null, null);
INSERT INTO roles (role_code, role_name, role_desc, is_sys, created_by, created_date, last_modified_by, last_modified_date) VALUES ( 'user', '普通用户', '普通用户', true, 'system', '2016-05-03 17:43:14', null, null);
