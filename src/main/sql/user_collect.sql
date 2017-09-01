DROP TABLE user_collect_info;
CREATE TABLE user_collect_info(
    id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_user_id BIGINT NOT NULL,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)