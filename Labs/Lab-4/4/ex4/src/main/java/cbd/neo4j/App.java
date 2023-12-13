package cbd.neo4j;

import org.neo4j.driver.Session;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.neo4j.driver.AuthTokens;

public class App {

    public static void insertData(Session session, String fileName) {
        String query = "LOAD CSV WITH HEADERS FROM 'file:///" + fileName + "' AS row\n" ; // Load CSV

        // Create nodes
        // Order
        query += "MERGE (o:Order {orderNumber: toInteger(row.ORDERNUMBER)})\n";
        query += "SET o.orderDate = row.ORDERDATE,\n" +
                "    o.status = row.STATUS,\n" +
                "    o.sales = toFloat(row.SALES),\n" +
                "    o.priceEach = toFloat(row.PRICEEACH)\n";

        // Product
        query += "MERGE (p:Product {productCode: row.PRODUCTCODE})\n";
        query += "SET p.productLine = row.PRODUCTLINE,\n" +
                "    p.msrp = toFloat(row.MSRP)";

        // Customer
        query += "MERGE (c:Customer {customerName: row.CUSTOMERNAME})\n";
        query += "SET c.contactLastName = row.CONTACTLASTNAME,\n" +
        "c.contactFirstName = row.CONTACTFIRSTNAME,\n" +
        "c.phone = row.PHONE,\n" +
        "c.addressLine1 = row.ADDRESSLINE1,\n" +
        "c.postalCode = row.POSTALCODE,\n" +
        "c.daysSinceLastOrder = toInteger(row.DAYS_SINCE_LASTORDER)\n";

        // Country
        query += "MERGE (co:Country {country: row.COUNTRY})\n";

        // City
        query += "MERGE (ci:City {city: row.CITY})\n";

        // Deal
        query += "MERGE (d:DealSize {size: row.DEALSIZE})\n";

        
        // Create relationships
        query += "MERGE (o)-[:PURCHASED {quantityOrdered: toInteger(row.QUANTITYORDERED)}]->(p)\n";
        query += "MERGE (c)-[:PLACED_ORDER]->(o)\n";
        query += "MERGE (c)-[:LOCATED_IN_COUNTRY]->(co)\n";
        query += "MERGE (c)-[:LOCATED_IN_CITY]->(ci)\n";
        query += "MERGE (o)-[:BELONGS_TO]->(d)\n";

        System.out.println("Inserting data from " + fileName + "...");
        session.run(query);
        System.out.println("Data inserted!");
    }

    public static void deleteData(Session session) {
        String query = "MATCH (n)\n";
        query += "DETACH DELETE n";

        System.out.println("Deleting data...");
        session.run(query);
        System.out.println("Data deleted!");
    }

    public static void main(String[] args) throws IOException {
        // Connect to Neo4j
        Driver driver;
        String file = "auto_sales_data.csv";
        String address = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "password";

        driver = GraphDatabase.driver(address, AuthTokens.basic(user, password));
        Session session = driver.session();

        // Delete data
        deleteData(session);

        // Insert data
        insertData(session, file);

        // Write output file
        FileWriter out = new FileWriter("../CBD_L44c_output.txt");

        // Queries
        // 1. O nome do cliente e as respetivo número de todas as encomendas que realizou.
        String query = "MATCH (c:Customer)-[:PLACED_ORDER]->(o:Order)\n";
        query += "WITH c, COLLECT(o.orderNumber) AS orders\n";
        query += "RETURN c.customerName AS Cliente, orders AS Encomendas";
        out.write("1. O nome do cliente e as respetivo número de todas as encomendas que realizou.\n");
        out.write(query + "\n\n");
        Result result = session.run(query);
        out.write("Cliente ---> Encomendas\n");
        out.write("-----------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String cliente = record.get("Cliente").asString();
            String encomendas = record.get("Encomendas").toString();
            out.write(cliente + " ---> " + encomendas + "\n");
        }
        out.write("\n\n");

        // 2. O nome do cliente e número de encomendas que realizou, ordenado por ordem decrescente do número de encomendas e o total gasto por cada cliente, com 2 casas decimais.
        query = "MATCH (c:Customer)-[:PLACED_ORDER]->(o:Order)\n";
        query += "WITH c, COLLECT(o.orderNumber) AS orders, SUM(o.priceEach) AS total\n";
        query += "RETURN c.customerName AS Cliente, SIZE(orders) AS NumEncomendas, ROUND(total, 2) AS TotalGasto\n";
        query += "ORDER BY NumEncomendas DESC";
        out.write("2. O nome do cliente e número de encomendas que realizou, ordenado por ordem decrescente do número de encomendas e o total gasto por cada cliente, com 2 casas decimais.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Cliente ---> NumEncomendas; TotalGasto\n");
        out.write("--------------------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String cliente = record.get("Cliente").asString();
            String numEncomendas = record.get("NumEncomendas").toString();
            String totalGasto = record.get("TotalGasto").toString();
            out.write(cliente + " ---> " + numEncomendas + "; " + totalGasto + "\n");
        }
        out.write("\n\n");

        // 3. Listar todos os países e o número correspondente de clientes que realizaram encomendas nesse país, ordenado por ordem decrescente do número de clientes.
        query = "MATCH (c:Customer)-[:LOCATED_IN_COUNTRY]->(co:Country)\n";
        query += "WITH co, COUNT(c) AS numClientes\n";
        query += "RETURN co.country AS Pais, numClientes AS NumClientes\n";
        query += "ORDER BY NumClientes DESC";
        out.write("3. Listar todos os países e o número correspondente de clientes que realizaram encomendas nesse país, ordenado por ordem decrescente do número de clientes.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("País ---> Número de clientes\n");
        out.write("----------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String pais = record.get("Pais").asString();
            String numClientes = record.get("NumClientes").toString();
            out.write(pais + " ---> " + numClientes + "\n");
        }
        out.write("\n\n");

        // 4. Listar o nome dos clientes que realizaram encomendas no país “USA”, de acordo com a cidade onde se localizam, ordenado por ordem alfabética do nome da cidade.
        query = "MATCH (c:Customer)-[:LOCATED_IN_COUNTRY]->(co:Country)\n";
        query += "WHERE co.country = 'USA'\n";
        query += "WITH c\n";
        query += "MATCH (c)-[:LOCATED_IN_CITY]->(ci:City)\n";
        query += "WITH ci, COLLECT(c.customerName) AS Clientes\n";
        query += "RETURN ci.city AS Cidade, Clientes\n";
        query += "ORDER BY Cidade";
        out.write("4. Listar o nome dos clientes que realizaram encomendas no país “USA”, de acordo com a cidade onde se localizam, ordenado por ordem alfabética do nome da cidade.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Cidade ---> Clientes\n");
        out.write("--------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String cidade = record.get("Cidade").asString();
            String clientes = record.get("Clientes").toString();
            out.write(cidade + " ---> " + clientes + "\n");
        }
        out.write("\n\n");

        // 5. Listar o número de encomendas em cada status, quando o tamanho do negócio (“DealSize”) é grande (“Large”).
        query = "MATCH (o:Order)-[:BELONGS_TO]->(d:DealSize)\n";
        query += "WHERE d.size = 'Large'\n";
        query += "WITH o.status AS stat, COUNT(o) AS numEncomendas\n";
        query += "RETURN stat, numEncomendas\n";
        out.write("5. Listar o número de encomendas em cada status, quando o tamanho do negócio (“DealSize”) é grande (“Large”).\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Status ---> Número de encomendas\n");
        out.write("--------------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String status = record.get("stat").asString();
            String numEncomendas = record.get("numEncomendas").toString();
            out.write(status + " ---> " + numEncomendas + "\n");
        }
        out.write("\n\n");

        // 6. A média de dinheiro gerado por cada produto ao longo de todas as encomendas, ordenado por ordem decrescente da média.
        query = "MATCH (o:Order)-[:PURCHASED]->(p:Product)\n";
        query += "WITH p.productCode AS code, AVG(o.sales) AS avgSales\n";
        query += "RETURN code, avgSales\n";
        query += "ORDER BY avgSales DESC";
        out.write("6. A média de dinheiro gerado por cada produto ao longo de todas as encomendas, ordenado por ordem decrescente da média.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Código do produto ---> Média de dinheiro gerado\n");
        out.write("------------------------------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String code = record.get("code").asString();
            String avgSales = record.get("avgSales").toString();
            out.write(code + " ---> " + avgSales + "\n");
        }
        out.write("\n\n");

        // 7. Top 5 dos produtos que foram mais vezes comprados, com o nome do produto e o número de vezes que foi comprado.
        query = "MATCH (o:Order)-[:PURCHASED]->(p:Product)\n";
        query += "WITH p, COUNT(o) AS numOrders\n";
        query += "RETURN p.productCode AS productCode, p.productLine AS productLine, numOrders AS NumOrders\n";
        query += "ORDER BY numOrders DESC\n";
        query += "LIMIT 5";
        out.write("7. Top 5 dos produtos que foram mais vezes comprados, com o nome do produto e o número de vezes que foi comprado.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Código do produto ---> Linha do produto; Número de vezes que foi comprado\n");
        out.write("-------------------------------------------------------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String productCode = record.get("productCode").asString();
            String productLine = record.get("productLine").asString();
            String numOrders = record.get("NumOrders").toString();
            out.write(productCode + " ---> " + productLine + "; " + numOrders + "\n");
        }
        out.write("\n\n");

        // 8. Listar o MSRP (Manufacturer's Suggested Retail Price) por linha de produto.
        query = "MATCH (p:Product)\n";
        query += "RETURN p.productLine AS productLine, AVG(p.msrp) AS msrp\n";
        out.write("8. Listar o MSRP (Manufacturer's Suggested Retail Price) por linha de produto.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Linha do produto ---> MSRP\n");
        out.write("---------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String productLine = record.get("productLine").asString();
            String msrp = record.get("msrp").toString();
            out.write(productLine + " ---> " + msrp + "\n");
        }
        out.write("\n\n");

        // 9. Listar os 10 produtos mais vendidos de em uma única encomenda.
        query = "MATCH (p:Product)<-[r:PURCHASED]-(:Order)\n";
        query += "WITH p, MAX(r.quantityOrdered) AS maxQuantityOrdered\n";
        query += "RETURN p.productCode, maxQuantityOrdered\n";
        query += "ORDER BY maxQuantityOrdered DESC\n";
        query += "LIMIT 10";
        out.write("9. Listar os 10 produtos mais vendidos de em uma única encomenda.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("Código do produto ---> Número de produtos vendidos\n");
        out.write("---------------------------------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String productCode = record.get("p.productCode").asString();
            String maxQuantityOrdered = record.get("maxQuantityOrdered").toString();
            out.write(productCode + " ---> " + maxQuantityOrdered + "\n");
        }
        out.write("\n\n");

        // 10. Listar o número de produtos vendidos (código e linha do produto) a cada país, ordenado por ordem decrescente do número de produtos vendidos.
        query = "MATCH (co:Country)<-[:LOCATED_IN_COUNTRY]-(:Customer)-[:PLACED_ORDER]->(:Order)-[r:PURCHASED]->(p:Product)\n";
        query += "WITH co, p, SUM(r.quantityOrdered) AS totalQuantityOrdered\n";
        query += "ORDER BY co.country, totalQuantityOrdered DESC\n";
        query += "WITH co, COLLECT({product: p, totalQuantityOrdered: totalQuantityOrdered})[0] AS topProductPerCountry\n";
        query += "RETURN co.country AS Country, topProductPerCountry.product.productCode AS ProductCode, topProductPerCountry.product.productLine AS ProductLine, topProductPerCountry.totalQuantityOrdered AS TotalQuantityOrdered\n";
        query += "ORDER BY TotalQuantityOrdered DESC";
        out.write("10. Listar o número de produtos vendidos (código e linha do produto) a cada país, ordenado por ordem decrescente do número de produtos vendidos.\n");
        out.write(query + "\n\n");
        result = session.run(query);
        out.write("País ---> Código do produto; Linha do produto; Número de produtos vendidos\n");
        out.write("--------------------------------------------------------------------------\n");
        while (result.hasNext()) {
            Record record = result.next();
            String country = record.get("Country").asString();
            String productCode = record.get("ProductCode").asString();
            String productLine = record.get("ProductLine").asString();
            String totalQuantityOrdered = record.get("TotalQuantityOrdered").toString();
            out.write(country + " ---> " + productCode + "; " + productLine + "; " + totalQuantityOrdered + "\n");
        }

        driver.close();
        out.close();
    }
}