
CREATE TABLE urls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE,
    createdAt timestamp
);