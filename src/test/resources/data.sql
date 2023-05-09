INSERT INTO `user` (id, email, encrypted_password, name, status, created_at, updated_at)
VALUES (1, 'yeon@dkargo.io', 'yeon1234', '김나연', 1, current_timestamp, current_timestamp),
       (2, 'nayeon@dkargo.io', '1234', '이나연', 1, current_timestamp, current_timestamp);


INSERT INTO category (id, parent_id, name, created_at)
VALUES (1, null, '대분류', current_timestamp),
       (2, 1, '중분류', current_timestamp),
       (3, 2, '소분류', current_timestamp),
       (4, 2, '소분류2', current_timestamp);


INSERT INTO post (id, category_id, user_id, title, body, public_state, view_count, password, comment_active_state,
                  is_deleted, created_at, updated_at)
VALUES (1, 4, 1, '게시글1', '본문 내용', 1, 0, '1234', true, false, '2023-04-28 12:00:00', '2023-04-28 12:00:00'),
       (2, 3, 1, '게시글2', '본문', 1, 3, '1234', false, false, '2023-04-28 13:00:00', '2023-04-28 13:00:00'),
       (3, 3, 1, '게시글3', '본문', 1, 2, '1234', true, false, '2023-04-28 14:00:00', '2023-04-28 14:00:00'),
       (4, 3, 1, '게시글4', '본문', 1, 1, null, true, false, '2023-04-28 15:00:00', '2023-04-28 15:00:00'),
       (5, 3, 1, '삭제된 게시글', '본문', 0, 0, '1234', true, true, '2023-04-28 16:00:00', '2023-04-28 16:00:00');


INSERT INTO post_file (id, post_id, filename, content_type, is_deleted, created_at)
VALUES (1, 4, 'filename1', 'image/png', false, current_timestamp);


INSERT INTO comment (id, post_id, user_id, parent_id, body, is_deleted, created_at, updated_at)
VALUES (1, 1, 1, null, '댓글1', false, current_timestamp, current_timestamp),
       (2, 1, 1, 1, 'depth2 댓글1', false, current_timestamp, current_timestamp),
       (3, 1, 1, null, '댓글2', false, current_timestamp, current_timestamp),
       (4, 1, 1, 1, 'depth2 댓글2', false, current_timestamp, current_timestamp),
       (5, 1, 1, 2, 'depth3 댓글1', false, current_timestamp, current_timestamp);
