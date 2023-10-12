package mongodb.ex4;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import redis.clients.jedis.Jedis;

public class Ex4c {

    public static String USERS = "users";
    public static void main(String[] args) {
        // We will test the metrics in order to compare the performance of the Redis and MongoDB databases
        // Redis
        Jedis redis = new Jedis("redis://localhost:6379");
        // Add a user
        long start = System.nanoTime();
        redis.set(USERS, "Ze");
        long end = System.nanoTime();
        System.out.println("Redis, insert a user: " + (end - start) + " ns");

        // Get a user
        start = System.nanoTime();
        redis.get(USERS);
        end = System.nanoTime();
        System.out.println("Redis, get a user: " + (end - start) + " ns");

        // Remove a user
        start = System.nanoTime();
        redis.del(USERS);
        end = System.nanoTime();
        System.out.println("Redis, remove a user: " + (end - start) + " ns");

        System.out.println();

        // MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Add a user
            start = System.nanoTime();
            mongoClient.getDatabase("test").getCollection("users").insertOne(new org.bson.Document("name", "Ze"));
            end = System.nanoTime();
            System.out.println("MongoDB, insert a user: " + (end - start) + " ns");

            // Get a user
            start = System.nanoTime();
            mongoClient.getDatabase("test").getCollection("users").find().first();
            end = System.nanoTime();
            System.out.println("MongoDB, get a user: " + (end - start) + " ns");

            // Remove a user
            start = System.nanoTime();
            mongoClient.getDatabase("test").getCollection("users").deleteOne(new org.bson.Document("name", "Ze"));
            end = System.nanoTime();
            System.out.println("MongoDB, remove a user: " + (end - start) + " ns");
        }

    }
}
