CREATE KEYSPACE IF NOT EXISTS bank_system WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}

CREATE TABLE IF NOT EXISTS bank_system.client (
    id int,
    name text,
    address text,
    phone text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_system.account (
    id uuid,
    client_id int,
    balance decimal,
    acc_number text,
    transactions_list list<uuid>,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_system.transaction (
    id uuid,
    acc_id uuid,
    amount decimal,
    date timestamp,
    transaction_type text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_system.loans (
    id uuid,
    acc_id uuid,
    amount decimal,
    interest_rate decimal,
    duration int,
    date timestamp,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_system.manager (
    id int,
    name text,
    address text,
    phone text,
    managed_accounts set<uuid>,
    performance map<text, decimal>,
    PRIMARY KEY (id)
);