CREATE TABLE IF NOT EXISTS Admin (
    admin_id INT NOT NULL,
    hash_password VARCHAR(64) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (admin_id)
);

CREATE TABLE IF NOT EXISTS Wallet(
    wallet_id SERIAL PRIMARY KEY NOT NULL,
    balance FLOAT8 NOT NULL DEFAULT 0.00 CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS BasicUser (
    user_id serial primary key not null,
    hash_password VARCHAR(64) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    wallet_id INT UNIQUE NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    gender VARCHAR(30),
    phone_number VARCHAR(20),
    date_of_birth DATE,
    FOREIGN KEY (wallet_id) References Wallet(wallet_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Organization (
    organization_id INT NOT NULL,
    hash_password VARCHAR(64) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    admin_id INT NOT NULL,
    wallet_id INT NOT NULL,
    verification_date DATE,
    verification_status VARCHAR(20) NOT NULL,
    feedback VARCHAR(140),
    organization_name VARCHAR(30) NOT NULL,
    description VARCHAR(140),
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    PRIMARY KEY (organization_id),
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id),
    FOREIGN KEY (wallet_id) REFERENCES Wallet(wallet_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Card (
    card_number CHAR(16) PRIMARY KEY NOT NULL,
    card_owner VARCHAR(64) NOT NULL,
    card_title VARCHAR(100) NOT NULL,
    expiration_month CHAR(2) NOT NULL,
    expiration_year CHAR(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS Event (
     event_id SERIAL PRIMARY KEY NOT NULL,
     admin_id INT NOT NULL,
     feedback VARCHAR(280),
     verification_date TIMESTAMPTZ,
     verification_status VARCHAR(50),
     event_name VARCHAR(50) NOT NULL,
     description VARCHAR(500) NOT NULL,
     start_date TIMESTAMPTZ NOT NULL,
     end_date TIMESTAMPTZ NOT NULL,
     is_online BOOLEAN NOT NULL,
     image_url VARCHAR(250),
     minimum_age INT,
     current_state VARCHAR(20) NOT NULL,
     FOREIGN KEY (admin_id) REFERENCES Admin(admin_id)
);

CREATE TABLE IF NOT EXISTS TicketedEvent (
    event_id INT NOT NULL,
    sales_channel VARCHAR(50) NOT NULL,
    sale_start_time TIMESTAMPTZ NOT NULL CHECK (sale_end_time >= sale_start_time),
    sale_end_time TIMESTAMPTZ NOT NULL CHECK (sale_end_time >= sale_start_time),
    PRIMARY KEY (event_id),
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS UnticketedEvent (
    event_id INT PRIMARY KEY NOT NULL,
    user_id INT NOT NULL,
    capacity INT NOT NULL CHECK (capacity >= 0),
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Category (
    ticketed_event_id INT NOT NULL,
    category_name VARCHAR(50) NOT NULL,
    category_description VARCHAR(200) NOT NULL,
    capacity INT NOT NULL CHECK (capacity >= 0),
    price FLOAT8 NOT NULL DEFAULT 0.00 CHECK (price >= 0),
    PRIMARY KEY (ticketed_event_id, category_name),
    FOREIGN KEY (ticketed_event_id) REFERENCES TicketedEvent(event_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Ticket (
    ticket_id SERIAL PRIMARY KEY NOT NULL,
    ticketed_event_id INT NOT NULL,
    user_id INT NOT NULL,
    transaction_date TIMESTAMP(2) NOT NULL,
    category_name VARCHAR(20) NOT NULL,
    purchase_type VARCHAR(20) NOT NULL,
    FOREIGN KEY (ticketed_event_id) REFERENCES Event(event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser(user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (category_name) REFERENCES Category(category_name)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Location (
    event_id INT NOT NULL,
    location_name VARCHAR(50) NOT NULL,
    latitude FLOAT8 NOT NULL,
    longitude FLOAT8 NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    state VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    address_description VARCHAR(280),
    PRIMARY KEY (event_id, latitude, longitude),
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Rating (
    rating_id SERIAL PRIMARY KEY NOT NULL,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    point INT NOT NULL CHECK (point BETWEEN 1 AND 5),
    topic VARCHAR(50),
    rating_comment VARCHAR(280),
    FOREIGN KEY (event_id) REFERENCES Event(event_id),
    FOREIGN KEY (user_id) REFERENCES BasicUser(user_id)
);

CREATE TABLE IF NOT EXISTS Report (
    report_id SERIAL PRIMARY KEY NOT NULL,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    description VARCHAR(280) NOT NULL,
    report_type VARCHAR(50) NOT NULL,
    report_state VARCHAR(50),
    report_date TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (report_id),
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES BasicUser(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Screenshot (
    report_id INT NOT NULL,
    image_url VARCHAR(240) UNIQUE NOT NULL,
    PRIMARY KEY (report_id, image_url),
    FOREIGN KEY (report_id) REFERENCES Report(report_id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id SERIAL PRIMARY KEY NOT NULL,
    sponsor_name VARCHAR(50) NOT NULL,
    details VARCHAR(50) NOT NULL,
    image_url VARCHAR(240)
);

CREATE TABLE IF NOT EXISTS event_sponsor (
    sponsor_id INT NOT NULL,
    event_id INT NOT NULL,
    PRIMARY KEY (sponsor_id, event_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id)
        ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS org_ticketed(
   event_id INT NOT NULL,
   organization_id INT NOT NULL,
   request_date TIMESTAMPTZ NOT NULL,
   PRIMARY KEY (event_id, organization_id),
   FOREIGN KEY (event_id) REFERENCES Event(event_id)
       ON DELETE CASCADE,
   FOREIGN KEY (organization_id) REFERENCES Account(account_id)
       ON DELETE CASCADE
);



/*INSERT INTO Account (password, email) VALUES ('456', 'berkay123@gmail.com'), ('1234', 'test@gmail.com');
*/
