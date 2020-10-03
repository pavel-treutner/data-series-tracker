DROP TABLE IF EXISTS datapoint;
CREATE TABLE datapoint (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL ,
    value DOUBLE NOT NULL,
    device VARCHAR(50) NOT NULL,
    user VARCHAR(50) NOT NULL,
    CONSTRAINT uq_entry UNIQUE (timestamp, device, user)
);
