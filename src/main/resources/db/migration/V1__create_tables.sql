CREATE TABLE pocket
(
    id   UUID PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE tamagotchi
(
    id        UUID PRIMARY KEY,
    name      TEXT NOT NULL,
    status    TEXT NOT NULL,
    pocket_id UUID REFERENCES pocket (id) ON DELETE CASCADE
);

CREATE TABLE deleted_tamagotchi
(
    id        UUID PRIMARY KEY,
    name      TEXT NOT NULL,
    status    TEXT NOT NULL,
    pocket_id UUID REFERENCES pocket (id) ON DELETE CASCADE
);