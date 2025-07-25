insert into game(id, name, category) values (1, 'Sid Meier''s Civilization VI', 'RTS');
insert into game(id, name, category) values (2, 'The Elder Scrolls III: Morrowind', 'RPG');
insert into game(id, name, category) values (3, 'Warframe', 'FPS');

alter sequence games_id_seq RESTART WITH 4;
