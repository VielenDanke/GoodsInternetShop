DELETE FROM goods WHERE 1=1;
DELETE FROM users WHERE 1=1;
DELETE FROM orders WHERE 1=1;
DELETE FROM order_details WHERE 1=1;
DELETE FROM authorities WHERE 1=1;
commit ;

DROP SEQUENCE AUTO_SEQ;

CREATE SEQUENCE auto_seq
    START WITH 100000
    INCREMENT BY   1
    NOCACHE
    NOCYCLE;

-- users --
INSERT INTO users (USERNAME)
VALUES ('Admin');                                                             -- 100 000

INSERT INTO users (USERNAME)
VALUES ('User');                                                              -- 100 001

-- authorities --
INSERT INTO authorities (USER_ID, AUTHORITY)
VALUES (100000, 'ROLE_ADMIN');

INSERT INTO authorities (USER_ID, AUTHORITY)
VALUES (100001, 'ROLE_USER');

-- goods --
INSERT INTO goods (NAME, DESCRIPTION, COST, COUNT)
VALUES ('кубики', 'коробка 30х20, разноцветные, 12 штук', 1500.00, 25);     -- 100 002

INSERT INTO goods (NAME, DESCRIPTION, COST, COUNT)
VALUES ('пазл Нью-Йорк', 'коробка 35х40, 1500 деталей', 2700.00, 55);       -- 100 003

INSERT INTO goods (NAME, DESCRIPTION, COST, COUNT)
VALUES ('пазл Москва', 'коробка 35х40, 2500 деталей', 3700.00, 55);         -- 100 004

INSERT INTO goods (NAME, DESCRIPTION, COST, COUNT)
VALUES ('кукла Барби', 'упаковка 50х20', 5500.00, 105);                     -- 100 005

INSERT INTO goods (NAME, DESCRIPTION, COST, COUNT)
VALUES ('кукла Кен', 'упаковка 50х20', 5500.00, 125);                       -- 100 006

-- orders --
INSERT INTO orders (USER_ID, CREATED)
VALUES (100001, systimestamp - 7);                                          -- 100 007

INSERT INTO orders (USER_ID, CREATED)
VALUES (100001, systimestamp - 5);                                          -- 100 008

-- order_details --
INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100007, 100002, 1, 1450.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100007, 100006, 1, 5500.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100008, 100004, 1, 3700.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100008, 100005, 1, 5300.00);

commit ;