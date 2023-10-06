package mongodb.ex3;

import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex3d {
    // function that returns the number of distinct localidades
    public static int countLocalidades(){
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            return restaurants.distinct("localidade", String.class).into(new ArrayList<>()).size();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    // function to count the restaurants in all the localidades
    public static Map<String, Integer> countRestByLocalidade() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            Map<String, Integer> result = new HashMap<>();

            for (Document doc : restaurants.aggregate(List.of(
                    Aggregates.group("$localidade", Accumulators.sum("count", 1))
            ))) {
                result.put(doc.getString("_id"), doc.getInteger("count"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // function to get the restaurants that have a given String in their name
    public static List<String> getRestWithNameCloserTo(String name){
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            List<String> result = new ArrayList<>();

            for (Document doc : restaurants.find(
                    Filters.regex("nome", name)
            )){
                result.add(doc.getString("nome"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            // 1. Apresente o número de localidades distintas
            int count = countLocalidades();
            if (count == -1) {
                System.out.println("Error counting localidades");
                return;
            }
            System.out.println("Numero de localidades distintas: " + countLocalidades());

            // 2. Apresente o número de restaurantes existentes em cada localidade
            Map<String, Integer> result = countRestByLocalidade();
            if (result == null) {
                System.out.println("Error counting restaurants by localidade");
                return;
            }
            System.out.println("\nNumero de restaurantes por localidade:");
            for(String localidade : result.keySet()){
                System.out.println(" -> " + localidade + " - " + result.get(localidade));
            }

            // 3. Nome de restaurantes contendo 'Park' no nome
            String regex = "Park";
            List<String> restaurantsRegex = getRestWithNameCloserTo(regex);
            if (restaurantsRegex == null) {
                System.out.println("Error getting restaurants with name closer to " + regex);
                return;
            }
            System.out.println("\nNome de restaurantes contendo '" + regex + "' no nome:");
            for(String rest : restaurantsRegex){
                System.out.println(" -> " + rest);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
