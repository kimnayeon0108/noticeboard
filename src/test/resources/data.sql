INSERT INTO `user` (id, login_id, password, name, status, created_at, updated_at)
VALUES (1, 'yeon', 'yeon1234', '김나연', 1, current_timestamp, current_timestamp);


INSERT INTO category (id, parent_id, name, created_at)
VALUES (1, null, '대분류', current_timestamp),
       (2, 1, '중분류', current_timestamp),
       (3, 2, '소분류', current_timestamp);


INSERT INTO post (id, category_id, user_id, title, body, public_state, password, created_at, updated_at)
VALUES (1, 3, 1, '제목', '본문', 0, '1234', current_timestamp, current_timestamp);


INSERT INTO post_file (id, post_id, path, created_at)
VALUES (1, 1, '/path/path2', current_timestamp);


INSERT INTO comment (id, post_id, user_id, parent_id, body, created_at, updated_at)
VALUES (1, 1, 1, null, '댓글 내용', current_timestamp, current_timestamp);
