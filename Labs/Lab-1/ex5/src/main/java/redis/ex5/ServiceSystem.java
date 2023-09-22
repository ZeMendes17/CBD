package redis.ex5;

import redis.clients.jedis.Jedis;

public class ServiceSystem {
    private final Jedis jedis;

    // set to store the products of the service
    public static String PRODUCT_SET = "products";

    // set with the users
    public static String USER_SET = "users";

    // list to store the orders of users
    public static String ORDER_LIST = "orders";

    // SET to store users and their timeslot

    // error message displayed when the limit is about to be exceeded
    public static String ERROR_MESSAGE = "You cannot order more products. Please wait for the restriction to be over." +
            "Time left: "; // will display the time that the user needs to wait

    // timeslot
    public static int TIMESLOT = 3 * 60; // 3 minutes

    public ServiceSystem() {
        this.jedis = new Jedis("redis://localhost:6379");
    }

    // add a new user
    public void addUser(String username) {
        jedis.sadd(USER_SET, username);
    }

    // ora muito bem, temos de fazer as funcoes para esta cena e depois aplicar tipo o ex6
    // nao sei explicat melhor, é facil faz


}
