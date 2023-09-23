package redis.ex5;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class ServiceSystem {
    private final Jedis jedis;

    // set with the users
    public static String USER_SET = "users";

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

    public ServiceSystem() {
        this.jedis = new Jedis("redis://localhost:6379");
    }

    // add a new user
    public void addUser(String username) {
        // check if the user already exists
        if (jedis.sismember(USER_SET, username)) {
            System.out.println("User already exists!");
            return;
        }
        jedis.sadd(USER_SET, username);
    }

    // check if the user exceeded the limit
    public boolean checkTimeslot(String username) {
        // if the user is not in the timeslot set, add it
        if (!jedis.exists(USER_TIMESLOT + ":" + username))
            return false;
        // if the user is in the timeslot set, check if the limit is exceeded
        long currentTime = System.currentTimeMillis() / 1000;
        long initTime = Long.parseLong(jedis.get(USER_TIMESLOT + ":" + username));
        if (currentTime - initTime > TIMESLOT) {
            // the timeslot is over, reset the timeslot and the order count
            jedis.del(USER_TIMESLOT + ":" + username);
            jedis.del(ORDER_COUNT + ":" + username);
            return false;
        }
        return true;
    }

    // order a product
    public void orderProduct(String username, String product) {
        // check if the user exceeded the limit
        if (checkTimeslot(username)) {
            // get the order count
            int orderCount = Integer.parseInt(jedis.get(ORDER_COUNT + ":" + username));
            // check if the limit is exceeded
            if (orderCount >= LIMIT) {
                // get the time left
                long currentTime = System.currentTimeMillis() / 1000;
                long initTime = Long.parseLong(jedis.get(USER_TIMESLOT + ":" + username));
                long timeLeft = TIMESLOT - (currentTime - initTime);
                System.out.println(ERROR_MESSAGE + timeLeft + " seconds");
                return;
            }
            // increment the order count otherwise
            jedis.incr(ORDER_COUNT + ":" + username);
        } else {
            // add the user to the timeslot set
            jedis.set(USER_TIMESLOT + ":" + username, (System.currentTimeMillis() / 1000) + "");
            // set the order count to 1
            jedis.set(ORDER_COUNT + ":" + username, "1");
        }
        // add the order to the user's orders
        jedis.rpush(USER_ORDERS + ":" + username, product);
    }

    public static void main(String[] args) {
        String[] products = {"sweatshirt", "t-shirt", "jeans", "jacket", "shoes", "socks", "scarf", "hat", "sunglasses"};
        ServiceSystem serviceSystem = new ServiceSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to our Service System!\n");
        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        serviceSystem.addUser(username);

        System.out.println("You can order up to " + LIMIT + " products in " + TIMESLOT + " seconds.\n");

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
                    serviceSystem.orderProduct(username, products[Integer.parseInt(product) - 1]);
                    System.out.println("You ordered " + products[Integer.parseInt(product) - 1]);
                    break;
                case 2:
                    System.out.println("Available products:");
                    for (String p : products)
                        System.out.println(p);
                    break;
                case 3:
                    System.out.println("Your orders:");
                    for (String order : serviceSystem.jedis.lrange(USER_ORDERS + ":" + username, 0, -1))
                        System.out.println(order);
                    break;
                case 4:
                    if (serviceSystem.checkTimeslot(username) && Integer.parseInt(serviceSystem.jedis.get(ORDER_COUNT + ":" + username)) >= LIMIT) {
                        // get the time left
                        long currentTime = System.currentTimeMillis() / 1000;
                        long initTime = Long.parseLong(serviceSystem.jedis.get(USER_TIMESLOT + ":" + username));
                        long timeLeft = TIMESLOT - (currentTime - initTime);
                        System.out.println("You need to wait " + timeLeft + " seconds.");
                    } else {
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
