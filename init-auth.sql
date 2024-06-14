CREATE TABLE IF NOT EXISTS "users"
(
    "id" SERIAL PRIMARY KEY,
    "nickname" VARCHAR(50) NOT NULL,
    "email" VARCHAR(100) UNIQUE NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS "session"
(
    "id" SERIAL PRIMARY KEY,
    "user_id" int NOT NULL,
    "token" VARCHAR(255) NOT NULL,
    "expires" TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "users"(id)
);
