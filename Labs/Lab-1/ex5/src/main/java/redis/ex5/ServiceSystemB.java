package redis.ex5;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

// in this exercise, the limit is quantity of products that can be ordered in a certain timeslot
// instead of the number of orders that can be placed in a certain timeslot
public class ServiceSystemB {
    private final Jedis jedis;

    // set with the users
    public static String USER_SET = "users";

    // store users and their timeslot
    public static String USER_TIMESLOT = "timeslot";

    // store how many products a user ordered in the current timeslot
    public static String PRODUCT_COUNT = "product_count";

    // store the orders for each user
    public static String USER_ORDERS = "orders"; // will store like --> orders:username = [product1:X, product2:Y, ...] (X and Y are the quantities)

    // error message displayed when the limit is about to be exceeded
    public static String ERROR_MESSAGE = "You cannot order more products. Please wait for the restriction to be over. " +
            "Time left: "; // will display the time that the user needs to wait

    // timeslot
    public static int TIMESLOT = 5 * 60; // 5 minutes
    // limit of products that can be ordered
    public static int LIMIT = 10;

    public ServiceSystemB() {
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
            jedis.del(PRODUCT_COUNT + ":" + username);
            return false;
        }
        return true;
    }

    // order a product
    public void orderProduct(String username, String product) {
        // check if the user exceeded the limit
        if (checkTimeslot(username)) {
            // get the order count
            int orderCount = Integer.parseInt(jedis.get(PRODUCT_COUNT + ":" + username));
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
            Scanner sc = new Scanner(System.in);
            System.out.print("How many would you like to order? ");
            int quantity = sc.nextInt();
            if (quantity + orderCount > LIMIT) { // check if the limit is exceeded with the new order
                System.out.println("You cannot order more than " + LIMIT + " products!");
                System.out.println("Try again.");
                return;
            }
            jedis.incrBy(PRODUCT_COUNT + ":" + username, quantity);
            // add the order to the user's orders
            jedis.rpush(USER_ORDERS + ":" + username, product + ":" + quantity);
            System.out.println("You ordered " + quantity + " - " + product);
        } else {
            // add the user to the timeslot set
            jedis.set(USER_TIMESLOT + ":" + username, (System.currentTimeMillis() / 1000) + "");

            Scanner sc = new Scanner(System.in);
            System.out.print("How many would you like to order? ");
            int quantity = sc.nextInt();
            if (quantity > LIMIT) {
                System.out.println("You cannot order more than " + LIMIT + " products!");
                System.out.println("Try again.");
                return;
            }
            if (quantity < 1) {
                System.out.println("Invalid quantity!");
                System.out.println("Try again.");
                return;
            }
            jedis.set(PRODUCT_COUNT + ":" + username, quantity + "");
            jedis.rpush(USER_ORDERS + ":" + username, product + ":" + quantity);
            System.out.println("You ordered " + quantity + " - " + product);
        }
    }

    public static void main(String[] args) {
        String[] products = {"sweatshirt", "t-shirt", "jeans", "jacket", "shoes", "socks", "scarf", "hat", "sunglasses"};
        ServiceSystemB serviceSystem = new ServiceSystemB();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to our Service System!\n");
        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        serviceSystem.addUser(username);

        System.out.println("You can order up to " + LIMIT + " products in " + TIMESLOT + " seconds.\n");

        int choice;
        while (true) {
            System.out.println("\nWhat would you like to do?\n");
            System.out.println("1. Order products");
            System.out.println("2. Show available products");
            System.out.println("3. Show my orders");
            System.out.println("4. Check my timeslot");
            System.out.println("5. Exit\n");

            choice = sc.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    if (serviceSystem.checkTimeslot(username) && Integer.parseInt(serviceSystem.jedis.get(PRODUCT_COUNT + ":" + username)) >= LIMIT) {
                        // get the time left
                        long currentTime = System.currentTimeMillis() / 1000;
                        long initTime = Long.parseLong(serviceSystem.jedis.get(USER_TIMESLOT + ":" + username));
                        long timeLeft = TIMESLOT - (currentTime - initTime);
                        System.out.println(ERROR_MESSAGE + timeLeft + " seconds");
                        break;
                    }
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
                    break;
                case 2:
                    System.out.println("Available products:");
                    for (int i = 0; i < products.length; i++)
                        System.out.println((i + 1) + ". " + products[i]);
                    break;
                case 3:
                    if (serviceSystem.jedis.llen(USER_ORDERS + ":" + username) == 0) {
                        System.out.println("You have no orders yet.");
                        break;
                    }
                    System.out.println("Your orders:");
                    int i = 1;
                    for (String order : serviceSystem.jedis.lrange(USER_ORDERS + ":" + username, 0, -1))
                        System.out.println(i++ + ". " + order.substring(0, order.indexOf(":")) + " - " + order.substring(order.indexOf(":") + 1));
                    break;
                case 4:
                    if (serviceSystem.checkTimeslot(username) && Integer.parseInt(serviceSystem.jedis.get(PRODUCT_COUNT + ":" + username)) >= LIMIT) {
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
