CREATE TABLE users (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE ,
                       username VARCHAR(64) UNIQUE NOT NULL,
                       password VARCHAR(128) NOT NULL,
                       email VARCHAR(64) not null unique,
                       role VARCHAR(50) not null ,
                       created_at TIMESTAMPTZ DEFAULT current_timestamp,
                       updated_at TIMESTAMPTZ DEFAULT current_timestamp
);

CREATE TABLE tokens(
                       username VARCHAR(255),
                       refresh_token VARCHAR(255) NOT NULL UNIQUE
)