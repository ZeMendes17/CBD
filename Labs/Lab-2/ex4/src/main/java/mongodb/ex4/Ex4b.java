package mongodb.ex4;

import com.mongodb.BasicDBList;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Scanner;


public class Ex4b {

    // set with the users
    public static String USERS = "users";

    // store users and their timeslot
    public static String USER_TIMESLOT = "timeslot";

    // store how many products a user ordered in the current timeslot
    public static String PRODUCT_COUNT = "product_count";

    // store the orders for each user
    public static String USER_ORDERS = "orders";


    // error message displayed when the limit is about to be exceeded
    public static String ERROR_MESSAGE = "You cannot order more products. Please wait for the restriction to be over. " +
            "Time left: "; // will display the time that the user needs to wait

    // timeslot
    public static int TIMESLOT = 5 * 60; // 5 minutes
    // limit of products that can be ordered
    public static int LIMIT = 10;




    private static void insertUser(String username){
        // check if user already exists
        //        if (db.find(Filters.eq("username", username)).first() != null) {
        //            System.err.println("User already exists");
        //            return;
        //        }

        Document user = new Document("username", username)
                .append(PRODUCT_COUNT, 0)
                .append(USER_ORDERS, new BasicDBList());

        try(MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            db.getCollection("atendimento2").insertOne(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean checkTimeslot(String username) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = db.getCollection("atendimento2");
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
                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(PRODUCT_COUNT, 0)));
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean orderProduct(String username, String product, int quantity) {
        // if user is in a timeslot
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = db.getCollection("atendimento2");
            if (checkTimeslot(username)) {
                // get the order count
                FindIterable<Document> user = collection.find(Filters.eq("username", username));
                MongoCursor<Document> cursor = user.iterator();
                Document doc = cursor.next();
                int productCount = doc.getInteger(PRODUCT_COUNT);

                // if the user has reached the limit
                if (productCount >= LIMIT) {
                    // get the current time
                    long currentTime = System.currentTimeMillis() / 1000;
                    long initTime = doc.getLong(USER_TIMESLOT);
                    long timeLeft = TIMESLOT - (currentTime - initTime);
                    if (timeLeft <= 0) {
                        // set user productCount to 0
                        collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(PRODUCT_COUNT, 0)));
                        // update the timeslot
                        collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(USER_TIMESLOT, System.currentTimeMillis() / 1000)));
                    } else {
                        System.err.println(ERROR_MESSAGE + timeLeft + " seconds");
                        return false;
                    }
                }
                else if (productCount + quantity > LIMIT) {
                    System.err.println("You can only order " + (LIMIT - productCount) + " products.");
                    return false;
                }

                // increment the order count
                collection.updateOne(Filters.eq("username", username), new Document("$inc", new Document(PRODUCT_COUNT, quantity)));
            } else {
                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(USER_TIMESLOT, System.currentTimeMillis() / 1000)));
                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(PRODUCT_COUNT, quantity)));
            }
            // add the order
            collection.updateOne(Filters.eq("username", username), Updates.push(USER_ORDERS, product + "__" + quantity));
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
                    System.out.print("Please enter the quantity: ");
                    int quantity = sc.nextInt();
                    if (quantity < 1 || quantity > LIMIT) {
                        System.out.println("Invalid quantity!");
                        break;
                    }

                    boolean result = orderProduct(username, products[Integer.parseInt(product) - 1], quantity);
                    if (result) {
                        System.out.println("You ordered " + quantity + " " + products[Integer.parseInt(product) - 1]);
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
                        MongoCollection<Document> collection = db.getCollection("atendimento2");
                        // get the orders
                        MongoCursor<Document> cursor = collection.find(Filters.eq("username", username)).iterator();
                        Document doc = cursor.next();
                        ArrayList<Object> orders = (ArrayList<Object>) doc.get(USER_ORDERS);

                        System.out.println("Your orders:");
                        for (Object order : orders) {
                            String[] orderSplit = order.toString().split("__");
                            System.out.println(orderSplit[1] + " - " + orderSplit[0]);
                        }
                    } catch (NullPointerException e) {
                        System.out.println("You have no orders");
                        break;
                    }
                    break;
                case 4:
                    try(MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                        MongoDatabase db = mongoClient.getDatabase("cbd");
                        MongoCollection<Document> collection = db.getCollection("atendimento2");
                        if (checkTimeslot(username) && collection.find(Filters.eq("username", username)).first().getInteger(PRODUCT_COUNT) >= LIMIT) {
                            long currentTime = System.currentTimeMillis() / 1000;
                            long initTime = collection.find(Filters.eq("username", username)).first().getLong(USER_TIMESLOT);
                            long timeLeft = TIMESLOT - (currentTime - initTime);
                            if (timeLeft <= 0) { // timeslot is over
                                collection.updateOne(Filters.eq("username", username), new Document("$set", new Document(PRODUCT_COUNT, 0)));
                                collection.updateOne(Filters.eq("username", username), new Document("$unset", new Document(USER_TIMESLOT, "")));
                                System.out.println("You can order products.");
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