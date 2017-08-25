DROP TABLE user_collect;
CREATE TABLE user_collect(
    id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_user_id BIGINT NOT NULL,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)