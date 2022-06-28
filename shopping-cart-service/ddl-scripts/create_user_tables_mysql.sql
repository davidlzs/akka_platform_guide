CREATE TABLE IF NOT EXISTS item_popularity (
    itemid VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL,
    count BIGINT NOT NULL,
    PRIMARY KEY (itemid));
