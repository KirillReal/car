CREATE TABLE ads_type(
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL UNIQUE
);

create table ads (
                     id SERIAL PRIMARY KEY,
                     created TIMESTAMP NOT NULL,
                     price INT NOT NULL,
                     is_sold BOOLEAN NOT NULL,
                     user_id INT NOT NULL REFERENCES users(id),
                     city_id INT NOT NULL REFERENCES city(id),
                     ads_type_id INT NOT NULL REFERENCES ads_type(id)
);

ALTER TABLE car
    DROP COLUMN created,
    DROP COLUMN price,
    DROP COLUMN is_sold,
    DROP COLUMN user_id,
    DROP COLUMN city_id,
    ADD COLUMN ads_id INT NOT NULL REFERENCES ads(id);

ALTER TABLE users
    ADD UNIQUE (login),
    ADD UNIQUE (phone);

ALTER TABLE car_engine_type
    ADD UNIQUE (name);

ALTER TABLE car_model
    ADD UNIQUE (name);

ALTER TABLE car_body_type
    ADD UNIQUE (name);

ALTER TABLE car_transmission_box_type
    ADD UNIQUE (name);

ALTER TABLE city
    ADD UNIQUE (name);