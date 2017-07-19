DROP TABLE users_roles;
CREATE TABLE users_roles (user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, role_id BIGINT NOT NULL);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (10070, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (10071, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2);
