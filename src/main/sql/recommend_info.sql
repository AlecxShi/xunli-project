CREATE TABLE recommend_info(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,      --id
    children_id BIGINT NOT NULL,                        --用户子女id
    target_children_id BIGINT NOT NULL,                 --推荐的用户id
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP   --最后更新时间
)