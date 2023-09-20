package redis.ex3;

import redis.clients.jedis.Jedis;
import java.util.Set;
import  java.util.HashMap;
import java.util.Map;
public class Ex3HashMap {
    private final Jedis jedis;
    public static String USERS = "users"; // Key set for users' name

    public Ex3HashMap() {
        this.jedis = new Jedis("redis://localhost:6379");
    }

    public void saveUser(String username, String password) {
        jedis.hset(USERS, username, password);
    }

    public Map<String, String> getUser() {
        return jedis.hgetAll(USERS);
    }

    public String getPassword(String username) {
        return jedis.hget(USERS, username);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

    public static void main(String[] args) {
        Ex3HashMap board = new Ex3HashMap();
        // set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
        String[] passwords = { "1234", "1221", "2222", "4321"};
        for (int i = 0; i < users.length; i++)
            board.saveUser(users[i], passwords[i]);

        board.getAllKeys().forEach(System.out::println);

        board.getUser().forEach((k,v) -> System.out.println(k + " : " + v));

        System.out.println(board.getPassword("Maria"));
    }
}
