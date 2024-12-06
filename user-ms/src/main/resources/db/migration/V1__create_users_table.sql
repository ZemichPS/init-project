CREATE SCHEMA app;

CREATE TABLE app.users(
    id uuid,
    email varchar(100),
    first_name varchar(100),
    last_name varchar(100),
    password varchar(100),
    role varchar(20),
    CONSTRAINT p_key PRIMARY KEY (id),
    CONSTRAINT uniq_email UNIQUE (email)
);


