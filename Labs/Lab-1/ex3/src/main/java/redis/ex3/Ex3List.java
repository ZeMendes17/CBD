package redis.ex3;

import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.Set;

public class Ex3List {
    private final Jedis jedis;
    public static String USERS = "users"; // Key set for users' name

    public Ex3List() {
        this.jedis = new Jedis("redis://localhost:6379");
    }

    public void saveUser(String username) {
        jedis.lpush(USERS, username);
    }
    public List<String> getUser() {
        return jedis.lrange(USERS, 0, -1);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

    public static void main(String[] args) {
        Ex3List board = new Ex3List();
        // set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
        for (String user: users)
            board.saveUser(user);

        board.getAllKeys().forEach(System.out::println);

        board.getUser().forEach(System.out::println);
    }
}

// after usage of the program, run the following command to clean the database:
// $ redis-cli FLUSHDB
