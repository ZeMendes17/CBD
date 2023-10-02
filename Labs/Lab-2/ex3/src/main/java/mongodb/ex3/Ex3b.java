package mongodb.ex3;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static mongodb.ex3.Ex3a.find;

public class Ex3b {
    public static void main(String[] args) {
        // we need to connect to the mongodb server
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            // before creating the indexes, we need to test how much time it takes to find a restaurant
            // by localidade, gastronomia and nome
            long start = System.nanoTime();
            find(restaurants, eq("localidade", "Manhattan"));
            long localidadeTime = System.nanoTime() - start;
            find(restaurants, eq("gastronomia", "American"));
            long gastronomiaTime = System.nanoTime() - start - localidadeTime;
            find(restaurants, eq("nome", "Harriet'S Kitchen"));
            long nomeTime = System.nanoTime() - start - localidadeTime - gastronomiaTime;
            long totalTime = System.nanoTime() - start;

            System.out.println("Before creating the indexes:");
            System.out.println("Time to find by localidade: " + localidadeTime + "ns");
            System.out.println("Time to find by gastronomia: " + gastronomiaTime + "ns");
            System.out.println("Time to find by nome: " + nomeTime + "ns");
            System.out.println("Total time: " + totalTime + "ns\n");

            // create the indexes
            System.out.println("Creating indexes...\n");
            restaurants.createIndex(Indexes.ascending("localidade"));
            restaurants.createIndex(Indexes.ascending("gastronomia"));
            restaurants.createIndex(Indexes.text("nome"));

            // after creating the indexes, we need to test how much time it takes to find the same
            start = System.nanoTime();
            find(restaurants, eq("localidade", "Manhattan"));
            localidadeTime = System.nanoTime() - start;
            find(restaurants, eq("gastronomia", "American"));
            gastronomiaTime = System.nanoTime() - start - localidadeTime;
            find(restaurants, eq("nome", "Harriet'S Kitchen"));
            nomeTime = System.nanoTime() - start - localidadeTime - gastronomiaTime;
            totalTime = System.nanoTime() - start;

            System.out.println("After creating the indexes:");
            System.out.println("Time to find by localidade: " + localidadeTime + "ns");
            System.out.println("Time to find by gastronomia: " + gastronomiaTime + "ns");
            System.out.println("Time to find by nome: " + nomeTime + "ns");
            System.out.println("Total time: " + totalTime + "ns");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

// Results with milliseconds:
// Before creating the indexes:
// Time to find by localidade: 3ms
// Time to find by gastronomia: 0ms
// Time to find by nome: 0ms
// Total time: 3ms

// Creating indexes...

// After creating the indexes:
// Time to find by localidade: 0ms
// Time to find by gastronomia: 0ms
// Time to find by nome: 0ms
// Total time: 0ms

// Not much difference, but it's still faster.
// Let's try with nanoseconds:

// Before creating the indexes:
// Time to find by localidade: 3135780ns
// Time to find by gastronomia: 4495ns
// Time to find by nome: 5255ns
// Total time: 3145607ns
//
// Creating indexes...
//
// After creating the indexes:
// Time to find by localidade: 4586ns
// Time to find by gastronomia: 1168ns
// Time to find by nome: 1027ns
// Total time: 6847ns


// The difference is much more noticeable now.
