package mongodb.ex3;

import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mongodb.client.model.Updates;
import java.util.List;

import com.mongodb.MongoException;

public class Ex3a {
    public static void main(String[] args) {
        // first we need to connect to the mongodb server
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            // insert a new restaurant
            Document restaurant = new Document("nome", "Tasca do Zé")
                    .append("restaurant_id", "123456789")
                    .append("stars", 5)
                    .append("gastronomia", "Portuguese")
                    .append("localidade", "Fafe")
                    .append("address", new Document("rua", "Rua da Comida")
                            .append("zipcode", "4820")
                            .append("building", "1")
                            .append("coord", List.of(-9.1234, 38.1234)))
                    .append("grades", List.of(new Document("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date()))
                    .append("grade", "A")
                    .append("score", 10),
                    new Document("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date()))
                    .append("grade", "B")
                    .append("score", 20)));

            if (insertRestaurant(restaurants, restaurant)) {
                System.out.println("Restaurant inserted successfully");
            } else {
                System.out.println("Error inserting restaurant");
            }

            // update the restaurant inserted
            Bson filter = new Document("nome", "Tasca do Zé");
            Bson update = Updates.set("gastronomia", "Nortenha");

            if (updateRestaurant(restaurants, filter, update)) {
                System.out.println("Restaurant updated successfully");
            } else {
                System.out.println("Error updating restaurant");
            }

            // find the restaurant inserted
            FindIterable<Document> result = find(restaurants, filter);
            System.out.println("Restaurant found:");
            for (Document doc : result) {
                System.out.println(doc.toJson());
            }

        } catch (Exception e) {
            System.err.println("Error connecting to the mongodb server");
            System.out.println("Make sure the server is running and listening on port 27017");
            System.out.println("Exiting...");
            e.printStackTrace();
        }
    }

    // function to insert a new restaurant
    public static boolean insertRestaurant(MongoCollection<Document> restaurants, Document restaurant) {
        try {
            restaurants.insertOne(restaurant);
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
            return false;
        }
    }

    // function to update a restaurant
    public static boolean updateRestaurant(MongoCollection<Document> restaurants, Bson filter, Bson update) {
        try {
            UpdateResult result = restaurants.updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
    }

    // function to find in the restaurants collection
    public static FindIterable<Document> find(MongoCollection<Document> restaurants, Bson filter) {
        return restaurants.find(filter);
    }
}
