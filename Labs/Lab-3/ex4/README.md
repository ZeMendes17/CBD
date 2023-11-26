# Exercício 4

Desenvolvimento de uma pequena base de dados
que tire partido do modelo de dados de Cassandra e cuja temática é livre.

**Tema:** Base de dados para um banco.

## Alínea a) Um keyspace com, pelo menos, 4 tabelas;

### Criação do keyspace

```sql
CREATE KEYSPACE IF NOT EXISTS bank_system WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}
```

### Criação das tabelas

```sql
CREATE TABLE IF NOT EXISTS bank_system.client (
    id int,
    name text,
    address text,
    phone text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_system.account (
    id int,
    client_id int,
    balance decimal,
    acc_number text,
    transactions_list list<int>,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bank_system.transaction (
    id int,
    acc_id int,
    amount decimal,
    date timestamp,
    transaction_type text,
    PRIMARY KEY ((acc_id), date)
) WITH CLUSTERING ORDER BY (date DESC);

CREATE TABLE IF NOT EXISTS bank_system.transaction_by_type (
    id int,
    acc_id int,
    amount decimal,
    date timestamp,
    transaction_type text,
    PRIMARY KEY ((acc_id), transaction_type, date)
) WITH CLUSTERING ORDER BY (transaction_type ASC, date DESC);

CREATE TABLE IF NOT EXISTS bank_system.loans (
    id int,
    acc_id int,
    amount decimal,
    interest_rate decimal,
    duration int,
    date timestamp,
    PRIMARY KEY ((acc_id), date)
) WITH CLUSTERING ORDER BY (date DESC);

CREATE TABLE IF NOT EXISTS bank_system.loans_by_duration (
    id int,
    acc_id int,
    amount decimal,
    interest_rate decimal,
    duration int,
    date timestamp,
    PRIMARY KEY ((acc_id), duration)
) WITH CLUSTERING ORDER BY (duration DESC);

CREATE TABLE IF NOT EXISTS bank_system.manager (
    id int,
    name text,
    address text,
    phone text,
    managed_accounts set<int>,
    performance map<text, decimal>,
    PRIMARY KEY (id)
);
```

> **Client:** Clientes do banco.

> **Account:** Contas bancárias dos clientes.

> **Transaction:** Transações realizadas em cada conta.

> **Loans:** Empréstimos realizados em cada conta.

> **Manager:** Gestores de conta, que podem gerir uma ou mais contas.

## Alínea b) Inserção de uma média de 12 registos por tabela;

### Client
    
```sql
INSERT INTO bank_system.client (id, name, address, phone) VALUES (1, 'João', 'Rua do João', '912345678');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (2, 'Maria', 'Rua da Esquina', '943009123');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (3, 'José', 'Travessa 7', '912345397');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (4, 'Ana', 'Rua do Sol', '932143876');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (5, 'Rui', 'Largo dos Patos', '90891444');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (6, 'Marta', 'Rua do Mar', '919475678');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (7, 'Carlos', 'Rua do Carlitos', '943345678');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (8, 'Sofia', 'Rua do João', '988777666');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (9, 'Pedro', 'Rua do João', '901432661');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (10, 'Inês', 'Viela dos Amores', '945668123');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (11, 'Ricardo', 'Rua Qualquer', '912349000');
INSERT INTO bank_system.client (id, name, address, phone) VALUES (12, 'Mariana', 'Travessa Da Escola', '914424989');
```

### Account

```sql
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (1, 1, 1000, '123456789', [2]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (2, 2, 2000, '987654321', [1, 3]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (3, 3, 3000, '123123123', []);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (4, 4, 40, '456456456', [4, 7]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (5, 5, 60000, '789789789', [5, 6]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (6, 6, 12.99, '321321321', []);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (7, 7, 9000, '654654654', [8]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (8, 8, 800, '987987987', [12]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (9, 9, 100, '159159159', [9, 10, 11]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (10, 10, 9000000, '753753753', [14]);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (11, 11, 100000, '951951951', []);
INSERT INTO bank_system.account (id, client_id, balance, acc_number, transactions_list) VALUES (12, 12, 9.13, '357357357', [13, 15]);
```

### Transaction

```sql
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (1, 2, 65, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (2, 1, 15, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (3, 2, 200, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (4, 4, 10, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (5, 5, 100, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (6, 5, 500, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (7, 4, 26.04, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (8, 7, 4, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (9, 9, 10, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (10, 9, 10000, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (11, 9, 100, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (12, 8, 29.87, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (13, 12, 10, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (14, 10, 1000000, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (15, 12, 19.13, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction (id, acc_id, amount, date, transaction_type) VALUES (16, 12, 10, toTimestamp(now()), 'withdraw');

INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (1, 2, 65, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (2, 1, 15, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (3, 2, 200, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (4, 4, 10, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (5, 5, 100, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (6, 5, 500, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (7, 4, 26.04, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (8, 7, 4, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (9, 9, 10, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (10, 9, 10000, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (11, 9, 100, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (12, 8, 29.87, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (13, 12, 10, toTimestamp(now()), 'withdraw');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (14, 10, 1000000, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (15, 12, 19.13, toTimestamp(now()), 'deposit');
INSERT INTO bank_system.transaction_by_type (id, acc_id, amount, date, transaction_type) VALUES (16, 12, 10, toTimestamp(now()), 'withdraw');
```

### Loans

```sql
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (1, 1, 1000, 0.2, 6, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (2, 2, 2000, 0.2, 10, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (3, 3, 2000, 0.3, 12, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (4, 10, 6000, 0.4, 6, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (5, 10, 3000, 0.27, 6, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (6, 12, 10000, 0.5, 24, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (7, 4, 5000, 0.2, 6, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (8, 7, 20000, 0.76, 36, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (9, 9, 1000, 0.2, 6, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (10, 9, 2000, 0.35, 12, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (11, 10, 1000000, 1, 24, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (12, 10, 500000, 0.75, 12, toTimestamp(now()));
INSERT INTO bank_system.loans (id, acc_id, amount, interest_rate, duration, date) VALUES (13, 1, 1000, 0.2, 6, toTimestamp(now()));

INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (1, 1, 1000, 0.2, 6, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (2, 2, 2000, 0.2, 10, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (3, 3, 2000, 0.3, 12, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (4, 10, 6000, 0.4, 6, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (5, 10, 3000, 0.27, 6, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (6, 12, 10000, 0.5, 24, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (7, 4, 5000, 0.2, 6, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (8, 7, 20000, 0.76, 36, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (9, 9, 1000, 0.2, 6, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (10, 9, 2000, 0.35, 12, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (11, 10, 1000000, 1, 24, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (12, 10, 500000, 0.75, 12, toTimestamp(now()));
INSERT INTO bank_system.loans_by_duration (id, acc_id, amount, interest_rate, duration, date) VALUES (13, 1, 1000, 0.2, 6, toTimestamp(now()));
```

### Manager

```sql
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (1, 'Marco', 'Rua do Marco', '999898979', {1, 2}, {'2021': 0.2, '2022': 0.3});
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (2, 'Ricardo', 'Rua do Ricardo', '914664132', {4, 5}, {'2021': 0.2, '2022': 0.3});
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (3, 'Joana', 'Largo da St Joana', '918097635', {7}, {'2020': 0.2, '2021': 0.2, '2022': 0.1});
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (4, 'Miguel', 'Rua do Miguel', '982123867', {10, 11, 12}, {'2019': 0.8, '2020': 0.2, '2021': 0.2, '2022': 0.3});
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (5, 'Ana', 'Rua da Ana', '932143876', {3}, {'2021': 1, '2022': 0.1});
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (6, 'Vitor', 'Travessa 7', '90891555', {6, 8}, {'2020': 0.6,'2021': 0.2, '2022': 0.2});
INSERT INTO bank_system.manager (id, name, address, phone, managed_accounts, performance) VALUES (7, 'Sara', 'Largo do Canto', '919191919', {9}, {'2021': 0.8, '2022': 1.2});
```

## Alínea c) Utilização das seguintes estruturas de dados (todas): set, list, map;

Como se pode ver nas tabelas acima criadas, todas estas estruturas de dados foram utilizadas:

- **Set:** *managed_accounts* na tabela *manager*.

- **List:** *transactions_list* na tabela *account*.

- **Map:** *performance* na tabela *manager*.

## Alínea d) Definição de, pelo menos, 2 índices secundários;

```sql
CREATE INDEX ON bank_system.account (client_id);
CREATE INDEX ON bank_system.transaction (acc_id);
CREATE INDEX ON bank_system.loans (acc_id);
```

> **Índice 1:** Permite pesquisar por pelo id do cliente e obter todas as suas contas.

> **Índice 2:** Permite pesquisar por pelo id da conta e obter todas as suas transações.

> **Índice 3:** Permite pesquisar por pelo id da conta e obter todos os seus empréstimos.

## Alínea e) Utilização de, pelo menos, 5 updates e 5 deletes de dados: utilize operações não triviais sobre as estruturas de dados (da alínea c))

### Updates

```sql
-- Adicionar a transação 16 à lista de transações da conta 12
UPDATE bank_system.account SET transactions_list = transactions_list + [16] WHERE id = 12;

-- Adicionar a conta 12 à lista de contas geridas pelo gestor 5
UPDATE bank_system.manager SET managed_accounts = managed_accounts + {12} WHERE id = 5;

-- Atualizar a performance do gestor 5 no ano 2022
UPDATE bank_system.manager SET performance['2022'] = 0.5 WHERE id = 5;

-- Atualizar a performance do gestor 5 no ano 2023
UPDATE bank_system.manager SET performance = performance + {'2023': 0.2} WHERE id = 5;

-- Atualizar a morada do cliente 1
UPDATE bank_system.client SET address = 'Rua do João e Antonieta' WHERE id = 1;

-- Atualizar o interesse do empréstimo 8
UPDATE bank_system.loans SET interest_rate = 0.8 WHERE id = 8;

-- Remover o cliente 8 da lista de contas geridas pelo gestor 6
UPDATE bank_system.manager SET managed_accounts = managed_accounts - {8} WHERE id = 6;
```

### Deletes

```sql
-- Remover o cliente 8
DELETE FROM bank_system.client WHERE id = 8;

-- Remover a conta 8
DELETE FROM bank_system.account WHERE id = 8;

-- Remover a transação 12
DELETE FROM bank_system.transaction WHERE id = 12;

-- Remover o empréstimo 1
DELETE FROM bank_system.loans WHERE id = 1;

-- Remover última transação
DELETE FROM bank_system.transaction WHERE id = 16;
```


## Alínea f) Criação de 10 queries expressivas do seu domínio de conhecimento da cláusula SELECT: use WHERE, ORDER BY, LIMIT, etc.

### 1. Selecionar todos os clientes

```sql
SELECT * FROM bank_system.client;
```

```sql
 id | address                 | name    | phone
----+-------------------------+---------+-----------
  5 |         Largo dos Patos |     Rui |  90891444
 10 |        Viela dos Amores |    Inês | 945668123
 11 |            Rua Qualquer | Ricardo | 912349000
  1 | Rua do João e Antonieta |    João | 912345678
  2 |          Rua da Esquina |   Maria | 943009123
  4 |              Rua do Sol |     Ana | 932143876
  7 |         Rua do Carlitos |  Carlos | 943345678
  6 |              Rua do Mar |   Marta | 919475678
  9 |             Rua do João |   Pedro | 901432661
 12 |      Travessa Da Escola | Mariana | 914424989
  3 |              Travessa 7 |    José | 912345397
```


### 2. Todas as transações realizadas na conta 2, ordenadas por ordem decrescente de data

```sql
SELECT * FROM bank_system.transaction WHERE acc_id = 2 ORDER BY date DESC;
```

```sql
  acc_id | date                            | amount | id | transaction_type
--------+---------------------------------+--------+----+------------------
      2 | 2023-11-26 17:32:29.822000+0000 |    200 |  3 |         withdraw
      2 | 2023-11-26 17:32:29.813000+0000 |     65 |  1 |          deposit
```

### 3. Os 3 últimos empréstimos realizados na conta 10 ordenados por ordem decrescente de data

```sql
SELECT * FROM bank_system.loans WHERE acc_id = 10 ORDER BY date DESC LIMIT 3;
```

```sql
 acc_id | date                            | amount  | duration | id | interest_rate
--------+---------------------------------+---------+----------+----+---------------
     10 | 2023-11-26 17:37:53.241000+0000 |  500000 |       12 | 12 |          0.75
     10 | 2023-11-26 17:37:53.236000+0000 | 1000000 |       24 | 11 |             1
     10 | 2023-11-26 17:37:53.205000+0000 |    3000 |        6 |  5 |          0.27
```

### 4. O id das contas geridas pelo gestor 5

```sql
SELECT managed_accounts FROM bank_system.manager WHERE id = 5;
```

```sql
  managed_accounts
------------------
          {3, 12}
```

### 5. O id, nome e o número dos clientes 3 e 10

```sql
SELECT id, name, phone FROM bank_system.client WHERE id IN (3, 10);
```

```sql
 id | name | phone
----+------+-----------
  3 | José | 912345397
 10 | Inês | 945668123
```

### 6. Todos os levantamentos realizados na conta 9

```sql
SELECT * FROM bank_system.transaction_by_type WHERE acc_id = 9 AND transaction_type = 'withdraw';
```

```sql
 acc_id | transaction_type | date                            | amount | id
--------+------------------+---------------------------------+--------+----
      9 |         withdraw | 2023-11-26 18:03:51.604000+0000 |  10000 | 10
      9 |         withdraw | 2023-11-26 18:03:51.600000+0000 |     10 |  9
```

### 7. O saldo da conta 2

```sql
SELECT balance FROM bank_system.account WHERE id = 2;
```

```sql
 balance
---------
    2000
```

### 8. Todas as transações realizadas pela conta 9 no mês de novembro de 2023

```sql
SELECT * FROM bank_system.transaction WHERE acc_id = 9 AND date >= '2023-11-01' AND date < '2023-12-01';
```

```sql
 acc_id | date                            | amount | id | transaction_type
--------+---------------------------------+--------+----+------------------
      9 | 2023-11-26 18:03:51.534000+0000 |    100 | 11 |          deposit
      9 | 2023-11-26 18:03:51.529000+0000 |  10000 | 10 |         withdraw
      9 | 2023-11-26 18:03:51.523000+0000 |     10 |  9 |         withdraw
```

### 9. O id e a performance de 2021 de todos os gestores

```sql
SELECT id, performance['2021'] AS performance_2021 FROM bank_system.manager;
```

```sql
 id | performance_2021
----+------------------
  5 |                1
  1 |              0.2
  2 |              0.2
  4 |              0.2
  7 |              0.8
  6 |              0.2
  3 |              0.2
```

### 10. Os empréstimos realizados na conta 10 com uma duração superior a 12 meses

```sql
SELECT * FROM bank_system.loans_by_duration WHERE acc_id = 10 AND duration > 12;
```

```sql
 acc_id | duration | amount  | date                            | id | interest_rate
--------+----------+---------+---------------------------------+----+---------------
     10 |       24 | 1000000 | 2023-11-26 22:25:06.196000+0000 | 11 |             1
```