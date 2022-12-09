CREATE TABLE IF NOT EXISTS Admin (
                                     admin_id INT NOT NULL,
                                     hash_password VARCHAR(64) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (admin_id)
    );

CREATE TABLE IF NOT EXISTS Wallet(
                                     wallet_id SERIAL PRIMARY KEY NOT NULL,
                                     balance FLOAT8 NOT NULL DEFAULT 0.00
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
    );

CREATE TABLE IF NOT EXISTS Organization (
                                            organization_id INT NOT NULL,
                                            hash_password VARCHAR(64) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    admin_id INT NOT NULL,
    wallet_id INT NOT NULL,
    verification_date date,
    verification_status VARCHAR(20) NOT NULL,
    feedback VARCHAR(140),
    organization_name VARCHAR(30) NOT NULL,
    description VARCHAR(140),
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    PRIMARY KEY (organization_id),
    FOREIGN KEY (admin_id) REFERENCES Admin(admin_id) ,
    FOREIGN KEY (wallet_id) REFERENCES Wallet(wallet_id)
    );

/*INSERT INTO Account (password, email) VALUES ('456', 'berkay123@gmail.com'), ('1234', 'test@gmail.com');
*/
