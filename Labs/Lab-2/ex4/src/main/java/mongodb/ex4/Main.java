package mongodb.ex4;

import com.mongodb.BasicDBList;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Scanner;


public class Main {

    // set with the users
    public static String USERS = "users";

    // store users and their timeslot
    public static String USER_TIMESLOT = "timeslot";

    // store how many products a user ordered in the current timeslot
    public static String ORDER_COUNT = "order_count";

    // store the orders for each user
    public static String USER_ORDERS = "orders";


    // error message displayed when the limit is about to be exceeded
    public static String ERROR_MESSAGE = "You cannot order more products. Please wait for the restriction to be over. " +
            "Time left: "; // will display the time that the user needs to wait

    // timeslot
    public static int TIMESLOT = 2 * 60; // 2 minutes
    // limit of products that can be ordered
    public static int LIMIT = 3;




    private static void insertUser(String username){
        // check if user already exists
        //        if (db.find(Filters.eq("username", username)).first() != null) {
        //            System.err.println("User already exists");
        //            return;
        //        }

        Document user = new Document("username", username)
                .append(ORDER_COUNT, 0)
                .append(USER_ORDERS, new BasicDBList());

        try(MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            db.getCollection("atendimento").insertOne(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean checkTimeslot(String username) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = db.getCollection("atendimento");
            FindIterable<Document> user = collection.find(Filters.eq("username", username));
            if (user.first() == null) {
                System.err.println("User not found");
                return false;
            }

            MongoCursor<Document> cursor = user.iterator();
            Document doc = cursor.next();

            if (doc.getLong(USER_TIMESLOT) == null) {
                return false;
            }

            long currentTime = System.currentTimeMillis() / 1000;
            long timeLeft = doc.getLong(USER_TIMESLOT) - currentTime;
            if (timeLeft > TIMESLOT) {
                // remove the timeslot
                collection.updateOne(Filters.eq("username", username), new Document("$unset", new Document(USER_TIMESLOT, "")));
                // make the order count 0
                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(ORDER_COUNT, 0)));
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean orderProduct(String username, String product) {
        // if user is in a timeslot
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = db.getCollection("atendimento");
            if (checkTimeslot(username)) {
                // get the order count
                FindIterable<Document> user = collection.find(Filters.eq("username", username));
                MongoCursor<Document> cursor = user.iterator();
                Document doc = cursor.next();
                int orderCount = doc.getInteger(ORDER_COUNT);

                // if the user has reached the limit
                if (orderCount >= LIMIT) {
                    // get the current time
                    long currentTime = System.currentTimeMillis() / 1000;
                    long initTime = doc.getLong(USER_TIMESLOT);
                    long timeLeft = TIMESLOT - (currentTime - initTime);
                    if (timeLeft <= 0) {
                        // set user orderCount to 0
                        collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(ORDER_COUNT, 0)));
                        // update the timeslot
                        collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(USER_TIMESLOT, System.currentTimeMillis() / 1000 + TIMESLOT)));
                    } else {
                        System.err.println(ERROR_MESSAGE + timeLeft + " seconds");
                        return false;
                    }
                }

                // increment the order count
                collection.updateOne(Filters.eq("username", username), new Document("$inc", new Document(ORDER_COUNT, 1)));
            } else {
                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(USER_TIMESLOT, System.currentTimeMillis() / 1000 + TIMESLOT)));
                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(ORDER_COUNT, 1)));
            }
            // add the order
            collection.updateOne(Filters.eq("username", username), Updates.push(USER_ORDERS, product));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static void main(String[] args) {
        String[] products = {"sweatshirt", "t-shirt", "jeans", "jacket", "shoes", "socks", "scarf", "hat", "sunglasses"};
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to our Service System!\n");
        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        insertUser(username);

        System.out.println("You can make " + LIMIT + " orders in " + TIMESLOT + " seconds.\n");

        int choice;
        while (true) {
            System.out.println("\nWhat would you like to do?\n");
            System.out.println("1. Order a product");
            System.out.println("2. Show available products");
            System.out.println("3. Show my orders");
            System.out.println("4. Check my timeslot");
            System.out.println("5. Exit\n");

            choice = sc.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println("Available products:");
                    for (int i = 0; i < products.length; i++)
                        System.out.println((i + 1) + ". " + products[i]);
                    System.out.print("\nPlease enter the number of the product you want to order: ");
                    String product = sc.next();

                    try {
                        Integer.parseInt(product);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid product number!");
                        break;
                    }
                    if (Integer.parseInt(product) < 1 || Integer.parseInt(product) > products.length) {
                        System.out.println("Invalid product number!");
                        break;
                    }

                    boolean result = orderProduct(username, products[Integer.parseInt(product) - 1]);
                    if (result) {
                        System.out.println("You ordered " + products[Integer.parseInt(product) - 1]);
                    }
                    break;
                case 2:
                    System.out.println("Available products:");
                    for (int i = 0; i < products.length; i++)
                        System.out.println((i + 1) + ". " + products[i]);
                    break;
                case 3:
                    // check if user has orders
                    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                        MongoDatabase db = mongoClient.getDatabase("cbd");
                        MongoCollection<Document> collection = db.getCollection("atendimento");
                        String orders = collection.find(Filters.eq("username", username)).first().get(USER_ORDERS).toString();

                        System.out.println("Your orders:");
                        System.out.println((orders));
                    } catch (NullPointerException e) {
                        System.out.println("You have no orders");
                        break;
                    }
                    break;
                    case 4:
                        try(MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                            MongoDatabase db = mongoClient.getDatabase("cbd");
                            MongoCollection<Document> collection = db.getCollection("atendimento");
                            if (checkTimeslot(username) && collection.find(Filters.eq("username", username)).first().getInteger(ORDER_COUNT) >= LIMIT) {
                                long currentTime = System.currentTimeMillis() / 1000;
                                long initTime = collection.find(Filters.eq("username", username)).first().getLong(USER_TIMESLOT);
                                long timeLeft = TIMESLOT - (currentTime - initTime);
                                if (timeLeft <= 0) { // timeslot is over
                                    collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(ORDER_COUNT, 0)));
                                    collection.updateOne(Filters.eq("username", username), new Document("$unset", new Document(USER_TIMESLOT, "")));
                                } else {
                                    System.out.println("You are in a timeslot. Time left: " + timeLeft + " seconds");
                                }
                            } else {
                                System.out.println("You can order products.");
                            }
                        } catch (NullPointerException e) {
                            System.out.println("You can order products.");
                        }
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option!");

            }
        }
    }
}