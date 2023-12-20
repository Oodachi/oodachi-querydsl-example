CREATE TABLE post
(
    id        SERIAL8      NOT NULL,
    title     VARCHAR(255) NOT NULL,
    content   VARCHAR(255) NOT NULL,
    date      DATE         NOT NULL,
    timestamp TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
)