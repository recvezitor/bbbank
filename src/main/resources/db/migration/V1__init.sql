CREATE SEQUENCE IF NOT EXISTS SEQ_USER_ID;
CREATE SEQUENCE IF NOT EXISTS SEQ_ACCOUNT_ID;
CREATE SEQUENCE IF NOT EXISTS SEQ_EMAIL_DATA_ID;
CREATE SEQUENCE IF NOT EXISTS SEQ_PHONE_DATA_ID;

CREATE TABLE "user"
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('SEQ_USER_ID' :: REGCLASS),
    name          VARCHAR(500) NOT NULL,
    password      VARCHAR(500) NOT NULL,
    date_of_birth date
);

CREATE TABLE "account"
(
    id        BIGINT PRIMARY KEY      DEFAULT nextval('SEQ_ACCOUNT_ID' :: REGCLASS),
    user_id   BIGINT         NOT NULL,
    balance   decimal(10, 2) NOT NULL default 0.0,
    "version" BIGINT         NOT NULL default 0,
    CONSTRAINT account_user_fk FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE TABLE "email_data"
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('SEQ_EMAIL_DATA_ID' :: REGCLASS),
    user_id BIGINT       NOT NULL,
    email   varchar(500) NOT NULL,
    CONSTRAINT email_data_user_fk FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT email_data_email_uk UNIQUE (email)
);

CREATE TABLE "phone_data"
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('SEQ_PHONE_DATA_ID' :: REGCLASS),
    user_id BIGINT       NOT NULL,
    phone   varchar(500) NOT NULL,
    CONSTRAINT phone_data_user_fk FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT phone_data_phone_uk UNIQUE (phone)
);
