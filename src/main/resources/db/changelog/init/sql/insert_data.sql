INSERT INTO role (rol_id, rol_name, rol_created, rol_modified)
VALUES (1, 'USER', current_timestamp, current_timestamp);

INSERT INTO role (rol_id, rol_name, rol_created, rol_modified)
VALUES (2, 'ADMIN', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (1, 'Ukraine', 'Kyiv', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (2, 'Ukraine', 'Lviv', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (3, 'Ukraine', 'Kharkiv', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (4, 'Ukraine', 'Rivne', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (5, 'Ukraine', 'Chernigov', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (6, 'Belarus', 'Minsk', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (7, 'Belarus', 'Grodno', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (8, 'Belarus', 'Gomel', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (9, 'Belarus', 'Berestia', current_timestamp, current_timestamp);

INSERT INTO location (loc_id, loc_country, loc_city, loc_created, loc_modified)
VALUES (10, 'Belarus', 'Mogilev', current_timestamp, current_timestamp);

INSERT INTO category (cat_id, cat_title, cat_image_url, cat_created, cat_modified)
VALUES (1, 'Sports and fitness', 'http://localhost/images/sport_image.jpeg', current_timestamp, current_timestamp);

INSERT INTO category (cat_id, cat_title, cat_image_url, cat_created, cat_modified)
VALUES (2, 'Food and entertainment', 'http://localhost/images/food_and_ent_category_image.jpeg', current_timestamp, current_timestamp);

INSERT INTO category (cat_id, cat_title, cat_image_url, cat_created, cat_modified)
VALUES (3, 'Tourism and leisure', 'http://localhost/images/tourism_and_leisure.jpeg', current_timestamp, current_timestamp);

INSERT INTO "user" (usr_id, usr_first_name, usr_last_name, usr_email, usr_password, usr_created, usr_modified, loc_id, rol_id)
VALUES (1, 'Admin', 'Admin', 'admin@gmail.com', 'admin', current_timestamp, current_timestamp, 1, 2);

INSERT INTO "user" (usr_id, usr_first_name, usr_last_name, usr_email, usr_password, usr_created, usr_modified, loc_id, rol_id)
VALUES (2, 'User', 'User', 'test_user@gmail.com', 'test', current_timestamp, current_timestamp, 1, 1);

INSERT INTO "user" (usr_id, usr_first_name, usr_last_name, usr_email, usr_password, usr_created, usr_modified, loc_id, rol_id)
VALUES (3, 'Ivan', 'Ivanov', 'ivan_ivanov@gmail.com', 'test', current_timestamp, current_timestamp, 3, 2);