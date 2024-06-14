CREATE TABLE IF NOT EXISTS "station"
(
    "id" SERIAL PRIMARY KEY,
    "station" VARCHAR(50) NOT NULL
);

INSERT INTO "station" ("station") VALUES ('Station1');
INSERT INTO "station" ("station") VALUES ('Station2');
INSERT INTO "station" ("station") VALUES ('Station3');
INSERT INTO "station" ("station") VALUES ('Station4');

CREATE TABLE IF NOT EXISTS "orders"
(
    "id" SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    "from_station_id" INT NOT NULL,
    "to_station_id" INT NOT NULL,
    "status" INT NOT NULL,
    "created" TIMESTAMP DEFAULT now(),
    FOREIGN KEY (from_station_id) REFERENCES station(id),
    FOREIGN KEY (to_station_id) REFERENCES station(id)
);