DELETE FROM goods WHERE 1=1;
DELETE FROM goods_categories WHERE 1=1;
DELETE FROM authorities WHERE 1=1;
DELETE FROM users WHERE 1=1;
DELETE FROM orders WHERE 1=1;
DELETE FROM order_details WHERE 1=1;

commit ;

DROP SEQUENCE AUTO_SEQ;

CREATE SEQUENCE AUTO_SEQ
    START WITH 100000
    INCREMENT BY   1
    NOCACHE
    NOCYCLE;

-- users --
INSERT INTO users (USERNAME, FULL_NAME, PASSWORD)
VALUES ('Admin@mail.ru', 'Admin', '$2a$10$7bXkLy8dosKAibaHY2.9cOk5l9.ODFj7LwqKRqGdQKCs4Im1iwVXG');            -- 100 000                                                         -- 100 000

INSERT INTO users (USERNAME, FULL_NAME, PASSWORD)
VALUES ('User@mail.ru', 'User', '$2a$10$9Mt/IEWzMB9tQ0myxb0PZ.4GIdNg5EfeJsV.RZ1XYkR1zigXlQEvq');        -- 100 001                                                -- 100 001

-- authorities --
INSERT INTO authorities (USER_ID, AUTHORITY)
VALUES (100000, 'ROLE_ADMIN');

INSERT INTO authorities (USER_ID, AUTHORITY)
VALUES (100001, 'ROLE_USER');

-- goods_categories --
INSERT INTO goods_categories (NAME)
VALUES('Детский мир');                                                               -- 100 002

INSERT INTO goods_categories (NAME)
VALUES('Книги');                                                                     -- 100 003

INSERT INTO goods_categories (NAME)
VALUES('Красота и здоровье');                                                        -- 100 004

INSERT INTO goods_categories (NAME)
VALUES('Категория А');                                                              -- 100 005

INSERT INTO goods_categories (NAME)
VALUES('Категория Б');                                                              -- 100 006

INSERT INTO goods_categories (NAME)
VALUES('Квтегория В');                                                              -- 100 007

INSERT INTO goods_categories (NAME)
VALUES('Категория Г');                                                              -- 100 008

INSERT INTO goods_categories (NAME)
VALUES('Категория Д');                                                              -- 100 009

-- goods --
INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100002, 'кубики', 'коробка 30х20, разноцветные, 12 штук', 1500.00, 25);     -- 100 010

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100002, 'пазл Нью-Йорк', 'коробка 35х40, 1500 деталей', 2700.00, 55);       -- 100 011

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100002, 'пазл Москва', 'коробка 35х40, 2500 деталей', 3700.00, 55);         -- 100 012

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100002, 'кукла Барби', 'упаковка 50х20', 5500.00, 105);                     -- 100 013

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100002, 'кукла Кен', 'упаковка 50х20', 5500.00, 125);                       -- 100 014

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100003, 'книга А', 'упаковка 50х20', 1500.00, 125);                         -- 100 015

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100003, 'книга Б', 'упаковка 50х20', 2500.00, 125);                         -- 100 016

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100003, 'книга В', 'упаковка 50х20', 500.00, 125);                          -- 100 017

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100004, 'красота А', 'упаковка 50х20', 5500.00, 125);                       -- 100 018

INSERT INTO goods (CATEGORY_ID, NAME, DESCRIPTION, COST, COUNT)
VALUES (100004, 'красота Б', 'упаковка 50х20', 5500.00, 125);                       -- 100 019

-- orders --
INSERT INTO orders (USER_ID, CREATED, STATUS)
VALUES (100001, systimestamp - 7, 0);                                          -- 100 020

INSERT INTO orders (USER_ID, CREATED, STATUS)
VALUES (100001, systimestamp - 5, 1);                                          -- 100 021

INSERT INTO orders (USER_ID, CREATED, STATUS)
VALUES (100001, systimestamp - 66, 1);                                         -- 100 022

-- order_details --
INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100020, 100010, 1, 1450.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100020, 100011, 1, 2500.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100020, 100013, 1, 5500.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100021, 100012, 1, 3700.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100021, 100016, 1, 2500.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100022, 100017, 1, 500.00);

INSERT INTO order_details (ORDER_ID, GOODS_ID, COUNT, COST)
VALUES (100022, 100018, 1, 5500.00);

commit ;