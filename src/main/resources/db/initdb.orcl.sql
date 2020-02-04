-- docker exec -it OracleDB bash -c "source /home/oracle/.bashrc; sqlplus /nolog"
-- connect sys as sysdba          -- password: Oradoc_db1
-- alter session set "_ORACLE_SCRIPT"=true;
-- create user internetshop identified by password;
-- GRANT ALL PRIVILEGES to internetshop;

DROP SEQUENCE AUTO_SEQ;
DROP TABLE authorities;
DROP TABLE order_details;
DROP TABLE orders;
DROP TABLE photo_goods;
DROP TABLE goods;
DROP TABLE goods_categories;
DROP TABLE users;

CREATE SEQUENCE auto_seq
 START WITH 100000
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;

create table users
(
    ID              NUMBER(20) DEFAULT auto_seq.NEXTVAL,
    USERNAME        VARCHAR2(50)   NOT NULL UNIQUE,
    PASSWORD        VARCHAR2(1024),
    GENDER          VARCHAR2(50),
    LOCALE          VARCHAR2(50),
    ADDRESS         VARCHAR2(256),
    FULL_NAME       VARCHAR2(256),
    ENABLED         NUMBER(1) DEFAULT 1 NOT NULL check (ENABLED in (0, 1)),
    AUTH_PROVIDER   VARCHAR2(256),
    PROVIDER_ID     VARCHAR2(256),
    PICTURE         VARCHAR2(512),
    CONSTRAINT users_pk PRIMARY KEY (ID)
);

create table goods_categories
(
    ID   NUMBER(20) DEFAULT auto_seq.NEXTVAL,
    NAME VARCHAR2(50) NOT NULL UNIQUE,
    CONSTRAINT categories_pk PRIMARY KEY (ID)
);


create table goods
(
    ID          NUMBER(20) DEFAULT auto_seq.NEXTVAL,
    CATEGORY_ID NUMBER(20) NOT NULL,
    NAME        VARCHAR2(128)  NOT NULL,
    DESCRIPTION VARCHAR2(1024) NOT NULL,
    COST        NUMBER(10, 2)  NOT NULL,
    COUNT       NUMBER(5)      NOT NULL,
    CONSTRAINT goods_pk PRIMARY KEY (ID),
    FOREIGN KEY (CATEGORY_ID) REFERENCES goods_categories (ID) ON DELETE CASCADE
);

create table photo_goods
(
    GOODS_ID    NUMBER(20) NOT NULL,
    PHOTO       CLOB,
    FOREIGN KEY (GOODS_ID) REFERENCES goods (ID) ON DELETE CASCADE
);

create table orders
(
    ID          NUMBER(20) DEFAULT auto_seq.NEXTVAL,
    USER_ID     NUMBER(20) NOT NULL,
    CREATED     TIMESTAMP DEFAULT SYSDATE,
    STATUS      NUMBER(1)  DEFAULT 0 NOT NULL check (STATUS in (0, 1)),
    CONSTRAINT order_pk PRIMARY KEY (ID),
    FOREIGN KEY (USER_ID) REFERENCES users (ID) ON DELETE CASCADE
);

create table order_details
(
    ID          NUMBER(20) DEFAULT auto_seq.NEXTVAL,
    ORDER_ID    NUMBER(20) NOT NULL,
    GOODS_ID    NUMBER(20) NOT NULL,
    COUNT       NUMBER(5),
    COST        NUMBER(10, 2)  NOT NULL,
    FOREIGN KEY (ORDER_ID) REFERENCES orders (ID) ON DELETE CASCADE,
    FOREIGN KEY (GOODS_ID) REFERENCES goods (ID) ON DELETE CASCADE
);

create table authorities
(
    USER_ID     NUMBER(20) NOT NULL,
    AUTHORITY   VARCHAR2(10) check (AUTHORITY in ('ROLE_USER', 'ROLE_ADMIN')),
    FOREIGN KEY (USER_ID) REFERENCES users (ID) ON DELETE CASCADE
)