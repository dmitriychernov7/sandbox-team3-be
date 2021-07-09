INSERT INTO location
VALUES (1, 'Kyiv', 'Ukraine', '2021-06-06 17:22:21', '2021-06-06 17:22:21'),
       (2, 'Lviv', 'Ukraine', '2021-06-06 17:22:21', '2021-06-06 17:22:21'),
       (3, 'Belarus', 'Minsk', '2021-06-06 17:22:21', '2021-06-06 17:22:21');

INSERT INTO vendor
VALUES (1, '2021-06-06 17:22:21', 'Sport Life - a chain of casual fitness centers', 'sprort_life@com.ua',
        'sport_life_image_1.jsp', '2021-06-06 17:22:21', 'Sport Life'),
       (2, '2022-06-06 17:22:21', 'Domino`s Pizza - an American multinational pizza restaurant chain founded in 1960',
        'dominos.com', 'dominos.com_image_1.jsp', '2022-06-06 17:22:21', 'Domino`s Pizza'),
       (3, '2023-06-06 17:22:21', 'TUI AG - travel and tourism company', 'tuigroup.com', 'tui_image_1.jsp',
        '2023-06-06 17:22:21', 'TUI');

INSERT INTO location_vendor
VALUES (2, 2),
       (1, 1),
       (3, 2);

