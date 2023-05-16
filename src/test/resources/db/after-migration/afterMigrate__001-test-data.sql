SET search_path = bbbank;

INSERT INTO "user"(id, "name", date_of_birth, password)
VALUES (1, 'user1', '01-01-1993', '123'),
       (2, 'user2', '01-01-1994', '124'),
       (3, 'user3', '01-01-1995', '125')
;

INSERT INTO "account"(id, user_id, balance)
VALUES (1, 1, 100.22),
       (2, 2, 101.22),
       (3, 3, 102.22)
;

INSERT INTO "email_data"(id, user_id, email)
VALUES (1, 1, 'email1@user1.com'),
       (2, 1, 'email2@user1.com'),
       (3, 1, 'email3@user1.com'),
       (4, 2, 'email1@user2.com'),
       (5, 3, 'email1@user3.com')
;

INSERT INTO "phone_data"(id, user_id, phone)
VALUES (1, 1, '70000001001'),
       (2, 1, '70000001002'),
       (3, 1, '70000001003'),
       (4, 2, '70000002001'),
       (5, 3, '70000003001')
;

SELECT setval('SEQ_USER_ID', (SELECT MAX(id) FROM "user"));
SELECT setval('SEQ_ACCOUNT_ID', (SELECT MAX(id) FROM "account"));
SELECT setval('SEQ_EMAIL_DATA_ID', (SELECT MAX(id) FROM "email_data"));
SELECT setval('SEQ_PHONE_DATA_ID', (SELECT MAX(id) FROM "phone_data"));
