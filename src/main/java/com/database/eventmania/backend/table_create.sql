drop table if exists event_type cascade;

drop table if exists org_ticketed cascade;

drop table if exists card cascade;

drop table if exists unticketedevent cascade;

drop table if exists ticket cascade;

drop table if exists category cascade;

drop table if exists ticketedevent cascade;

drop table if exists location cascade;

drop table if exists rating cascade;

drop table if exists screenshot cascade;

drop table if exists report cascade;

drop table if exists event_sponsor cascade;

drop table if exists sponsor cascade;

drop table if exists org_unticketed cascade;

drop table if exists friends cascade;

drop table if exists follow_org cascade;

drop table if exists organization cascade;

drop table if exists join_event cascade;

drop table if exists follow_event cascade;

drop table if exists basicuser cascade;

drop table if exists wallet cascade;

drop table if exists event cascade;

drop table if exists admin cascade;

drop view if exists account_with_type cascade;

drop sequence if exists global_role_seq cascade;

drop sequence if exists wallet_wallet_id_seq cascade;

drop sequence if exists event_event_id_seq cascade;

drop sequence if exists ticket_ticket_id_seq cascade;

drop sequence if exists rating_rating_id_seq cascade;

drop sequence if exists report_report_id_seq cascade;

drop sequence if exists sponsor_sponsor_id_seq cascade;

CREATE SEQUENCE global_role_seq;

CREATE TABLE IF NOT EXISTS Admin
(
    admin_id      INT PRIMARY KEY DEFAULT nextval('global_role_seq') NOT NULL,
    hash_password VARCHAR(64)                                        NOT NULL,
    email         VARCHAR(255) UNIQUE                                NOT NULL
);

CREATE TABLE IF NOT EXISTS Wallet
(
    wallet_id SERIAL PRIMARY KEY NOT NULL,
    balance   FLOAT8             NOT NULL DEFAULT 0.00 CHECK (balance >= 0)
);


CREATE TABLE IF NOT EXISTS BasicUser
(
    user_id       INT PRIMARY KEY DEFAULT nextval('global_role_seq') NOT NULL,
    hash_password VARCHAR(64)                                        NOT NULL,
    email         VARCHAR(255) UNIQUE                                NOT NULL,
    wallet_id     INT,
    first_name    VARCHAR(30)                                        NOT NULL,
    last_name     VARCHAR(30)                                        NOT NULL,
    gender        VARCHAR(30),
    phone_number  VARCHAR(20),
    date_of_birth DATE,
    FOREIGN KEY (wallet_id) References Wallet (wallet_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Organization
(
    organization_id     INT PRIMARY KEY DEFAULT nextval('global_role_seq') NOT NULL,
    hash_password       VARCHAR(64)                                        NOT NULL,
    email               VARCHAR(255) UNIQUE                                NOT NULL,
    admin_id            INT,
    wallet_id           INT,
    verification_date   DATE,
    verification_status VARCHAR(20),
    feedback            VARCHAR(140),
    organization_name   VARCHAR(30)                                        NOT NULL,
    description         VARCHAR(140),
    phone_number        VARCHAR(20) UNIQUE                                 NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin (admin_id),
    FOREIGN KEY (wallet_id) REFERENCES Wallet (wallet_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Card
(
    card_number      CHAR(16) PRIMARY KEY NOT NULL,
    card_owner       VARCHAR(64)          NOT NULL,
    card_title       VARCHAR(100)         NOT NULL,
    expiration_month CHAR(2)              NOT NULL,
    expiration_year  CHAR(4)              NOT NULL
);

CREATE TABLE IF NOT EXISTS Event
(
    event_id            SERIAL PRIMARY KEY NOT NULL,
    admin_id            INT,
    feedback            VARCHAR(280),
    verification_date   TIMESTAMPTZ,
    verification_status VARCHAR(50),
    event_name          VARCHAR(50)        NOT NULL,
    description         VARCHAR(500)       NOT NULL,
    start_date          TIMESTAMPTZ        NOT NULL,
    end_date            TIMESTAMPTZ        NOT NULL,
    is_online           BOOLEAN            NOT NULL,
    image_url           VARCHAR(250),
    minimum_age         INT,
    current_state       VARCHAR(20)        NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin (admin_id)
);

CREATE TABLE IF NOT EXISTS TicketedEvent
(
    event_id        INT         NOT NULL,
    sales_channel   VARCHAR(50) NOT NULL,
    sale_start_time TIMESTAMPTZ NOT NULL CHECK (sale_end_time >= sale_start_time),
    sale_end_time   TIMESTAMPTZ NOT NULL CHECK (sale_end_time >= sale_start_time),
    organization_id INT         NOT NULL,
    PRIMARY KEY (event_id),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES Organization (organization_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS UnticketedEvent
(
    event_id INT PRIMARY KEY NOT NULL,
    user_id  INT             NOT NULL,
    capacity INT             NOT NULL CHECK (capacity >= 0),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Category
(
    ticketed_event_id    INT          NOT NULL,
    category_name        VARCHAR(50)  NOT NULL,
    category_description VARCHAR(200) NOT NULL,
    capacity             INT          NOT NULL CHECK (capacity >= 0),
    price                FLOAT8       NOT NULL DEFAULT 0.00 CHECK (price >= 0),
    PRIMARY KEY (ticketed_event_id, category_name),
    FOREIGN KEY (ticketed_event_id) REFERENCES TicketedEvent (event_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Ticket
(
    ticket_id         SERIAL PRIMARY KEY NOT NULL,
    ticketed_event_id INT                NOT NULL,
    user_id           INT                NOT NULL,
    transaction_date  TIMESTAMP(2),
    category_name     VARCHAR(20)        NOT NULL,
    purchase_type     VARCHAR(20)        NOT NULL,
    FOREIGN KEY (ticketed_event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (category_name, ticketed_event_id) REFERENCES Category (category_name, ticketed_event_id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Location
(
    event_id            INT          NOT NULL,
    location_name       VARCHAR(500) NOT NULL,
    latitude            FLOAT8       NOT NULL,
    longitude           FLOAT8       NOT NULL,
    postal_code         VARCHAR(20)  NOT NULL,
    state               VARCHAR(50)  NOT NULL,
    city                VARCHAR(50)  NOT NULL,
    country             VARCHAR(50)  NOT NULL,
    address_description VARCHAR(500),
    PRIMARY KEY (event_id, latitude, longitude),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Rating
(
    rating_id      SERIAL PRIMARY KEY NOT NULL,
    event_id       INT                NOT NULL,
    user_id        INT                NOT NULL,
    point          INT                NOT NULL CHECK (point BETWEEN 1 AND 5),
    topic          VARCHAR(50),
    rating_comment VARCHAR(280),
    FOREIGN KEY (event_id) REFERENCES Event (event_id),
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
);

CREATE TABLE IF NOT EXISTS Report
(
    report_id    SERIAL PRIMARY KEY NOT NULL,
    event_id     INT                NOT NULL,
    user_id      INT                NOT NULL,
    description  VARCHAR(280)       NOT NULL,
    report_type  VARCHAR(50)        NOT NULL,
    report_state VARCHAR(50),
    report_date  TIMESTAMPTZ        NOT NULL,
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Screenshot
(
    report_id INT                 NOT NULL,
    image_url VARCHAR(240) UNIQUE NOT NULL,
    PRIMARY KEY (report_id, image_url),
    FOREIGN KEY (report_id) REFERENCES Report (report_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Sponsor
(
    sponsor_id   SERIAL PRIMARY KEY NOT NULL,
    sponsor_name VARCHAR(50)        NOT NULL,
    details      VARCHAR(50)        NOT NULL,
    image_url    VARCHAR(240)
);

CREATE TABLE IF NOT EXISTS event_sponsor
(
    sponsor_id INT NOT NULL,
    event_id   INT NOT NULL,
    PRIMARY KEY (sponsor_id, event_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor (sponsor_id)
        ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE
);
/*
CREATE TABLE IF NOT EXISTS org_ticketed
(
    event_id        INT         NOT NULL,
    organization_id INT         NOT NULL,
    request_date    TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (event_id, organization_id),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES Organization (organization_id)
        ON DELETE CASCADE
);
*/

CREATE TABLE IF NOT EXISTS org_unticketed
(
    event_id        INT         NOT NULL,
    organization_id INT         NOT NULL,
    request_date    TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (event_id, organization_id),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES Organization (organization_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   INT NOT NULL,
    friend_id INT NOT NULL CHECK (user_id != friend_id),
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS follow_org
(
    user_id         INT NOT NULL,
    organization_id INT NOT NULL,
    PRIMARY KEY (user_id, organization_id),
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (organization_id) REFERENCES Organization (organization_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS join_event
(
    event_id INT NOT NULL,
    user_id  INT NOT NULL,
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS follow_event
(
    event_id INT NOT NULL,
    user_id  INT NOT NULL,
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_type
(
    event_id      INT         NOT NULL,
    type_of_event VARCHAR(50) NOT NULL,
    PRIMARY KEY (event_id, type_of_event),
    FOREIGN KEY (event_id) REFERENCES Event (event_id)
        ON DELETE CASCADE
);



CREATE TRIGGER create_base_wallet
    AFTER INSERT
    ON BasicUser
    FOR EACH ROW
EXECUTE PROCEDURE create_base_wallet_func();

CREATE OR REPLACE FUNCTION create_base_wallet_func()
    RETURNS trigger AS
$$
BEGIN
    INSERT INTO Wallet (balance) VALUES (0);
    UPDATE BasicUser SET wallet_id = currval('wallet_wallet_id_seq') WHERE user_id = currval('global_role_seq');
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql';



CREATE VIEW account_with_type AS
(
SELECT organization_id, email, hash_password, 'Organization' AS account_type
FROM Organization
UNION
SELECT user_id, email, hash_password, 'BasicUser' AS account_type
FROM BasicUser
UNION
SELECT admin_id, email, hash_password, 'Admin' AS account_type
FROM Admin);

CREATE VIEW event_with_type AS
(
WITH joined_event_type_location AS (SELECT E.event_id,
                                           admin_id,
                                           feedback,
                                           verification_date,
                                           verification_status,
                                           event_name,
                                           description,
                                           start_date,
                                           end_date,
                                           is_online,
                                           image_url,
                                           minimum_age,
                                           current_state,
                                           location_name,
                                           latitude,
                                           longitude,
                                           postal_code,
                                           state,
                                           city,
                                           country,
                                           address_description,
                                           type_of_event
                                    FROM Event E
                                             NATURAL JOIN event_type ET
                                             LEFT OUTER JOIN location L ON E.event_id = L.event_id)
     -- LEFT OUTER JOIN location L ON E.event_id = L.event_id)
SELECT joined_event_type_location.event_id,
       admin_id,
       feedback,
       verification_date,
       verification_status,
       event_name,
       description,
       start_date,
       end_date,
       is_online,
       image_url,
       minimum_age,
       current_state,
       location_name,
       latitude,
       longitude,
       postal_code,
       state,
       city,
       country,
       address_description,
       type_of_event,
       'Ticketed' AS ticketed_type
FROM joined_event_type_location
         NATURAL JOIN TicketedEvent
UNION
SELECT joined_event_type_location.event_id,
       admin_id,
       feedback,
       verification_date,
       verification_status,
       event_name,
       description,
       start_date,
       end_date,
       is_online,
       image_url,
       minimum_age,
       current_state,
       location_name,
       latitude,
       longitude,
       postal_code,
       state,
       city,
       country,
       address_description,
       type_of_event,
       'Unticketed' AS ticketed_type
FROM joined_event_type_location
         NATURAL JOIN UnticketedEvent);
/*123 sifre*/
INSERT INTO Admin (hash_password, email)
VALUES ('$2a$10$MPTMyLSfb9jIaC8N1vKh.e8hRDralULHLb9Ax5JwKWLy6FW35XzFy', 'berkayclmz@gmail.com');


