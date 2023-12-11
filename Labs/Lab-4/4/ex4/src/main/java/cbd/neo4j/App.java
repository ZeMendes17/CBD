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

        // Insert data
        insertData(session, file);

        // Write output file
        FileWriter out = new FileWriter("CBD_L44c_output.txt");

        // Queries here
        // Example query
        String query = "MATCH (n) return n";
        out.write("Query 1:\n");
        out.write(query + "\n");
        Result result = session.run(query);
        while (result.hasNext()) {
            Record record = result.next();
            Node node = record.get("n").asNode(); // Get the node
            Map<String, Object> properties = node.asMap(); // Get node properties

            // Print specific properties you want to display
            out.write("Node Properties: " + properties.toString() + "\n");
        }

        // Delete data
        deleteData(session);
        driver.close();
        out.close();
    }
}