# Exercício 4

## Dataset

O dataset utilizado foi retirado do site https://www.kaggle.com/datasets/ddosad/auto-sales-data/. Este dataset contém dados sobre vendas de uma companhia de automóveis, contendo 589 nodes.

## Entidades

### Encomenda (Order)

- **orderNumber**: número único de identificação da encomenda
- **orderDate**: data da encomenda
- **status**: estado da encomenda, pode ser "Shipped", "Resolved", "Cancelled", "On Hold", "Disputed" ou "In Process"
- **priceEach**: preço de cada item na encomenda

### Produto (Product)

- **productCode**: código único de identificação do produto
- **productLine**: linha de produtos a que pertence o produto
- **msrp**: Manufacturer's Suggested Retail Price, ou seja, o preço de venda sugerido pelo fabricante

### Cliente (Customer)

- **customerName**: nome do cliente
- **phone**: número de telefone do cliente
- **addressLine1**: primeira linha do endereço do cliente
- **postalCode**: código postal do cliente
- **contactLastName**: apelido do contacto do cliente
- **contactFirstName**: primeiro nome do contacto do cliente
- **daysSinceLastOrder**: dias desde a última encomenda do cliente

### País (Country)

- **country**: nome do país

### Cidade (City)

- **city**: nome da cidade

### Negócio (Deal)

- **dealSize**: tamanho do negócio, pode ser "Small", "Medium" ou "Large"

## Relações

### Comprou (Purchased)

Conecta a encomenda ao produto que foi comprado nesta.

### Encomenda Realizada (PLACED_ORDER)

Conecta o cliente à encomenda que este fez.

### No País (LOCATED_IN_COUNTRY)

Conecta o cliente ao país onde este está localizado.

### Na Cidade (LOCATED_IN_CITY)

Conecta o cliente à cidade onde este está localizado.

### Pertence ao Negócio (BELONGS_TO)

Conecta a encomenda ao tido de negócio a que esta pertence.