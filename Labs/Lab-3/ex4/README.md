# Exercício 2

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