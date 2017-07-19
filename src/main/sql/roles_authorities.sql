DROP TABLE roles_authorities;
CREATE TABLE roles_authorities (role_id BIGINT NOT NULL, authority_id BIGINT NOT NULL);
INSERT INTO roles_authorities (role_id, authority_id) VALUES (1, 1);
INSERT INTO roles_authorities (role_id, authority_id) VALUES (1, 2);
INSERT INTO roles_authorities (role_id, authority_id) VALUES (2, 2);
