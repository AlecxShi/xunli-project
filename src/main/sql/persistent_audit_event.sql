DROP TABLE persistent_audit_event;
CREATE TABLE persistent_audit_event (event_id BIGINT NOT NULL PRIMARY KEY , principal VARCHAR(255) NOT NULL, event_date TIMESTAMP, event_type VARCHAR(255) );
