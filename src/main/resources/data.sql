
INSERT INTO public.app_user (id, username, password, email, created_at, updated_at, updated_by)
VALUES (1, 'admin', '{bcrypt}$2a$12$9RjZXWuniHIVo04ilQYLnOcEnNrrAHedD1h0NJgntx1MV4hT8RQOa', 'admin@gmail.com',
        '2024-08-09 19:44:43.000000', null, null);
INSERT INTO public.app_user (id, username, password, email, created_at, updated_at, updated_by)
VALUES (2, 'user', '{bcrypt}$2a$10$iE3oH0iKUshcxXsU62gfp.tbY3pz1RwSvzPYw87g3l6jNqgb9eTQG', 'user@gmail.com',
        '2024-08-14 00:09:26.058490', null, null);

INSERT INTO public.authority (id, name, user_id)
VALUES (1, 'ADMIN', 1);
INSERT INTO public.authority (id, name, user_id)
VALUES (2, 'USER', 1);
INSERT INTO public.authority (id, name, user_id)
VALUES (3, 'USER', 2);