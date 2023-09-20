package redis.ex3;

import redis.clients.jedis.Jedis;
import java.util.Set; 

public class SimplePost {
 
	private Jedis jedis;
	public static String USERS = "users"; // Key set for users' name
	
	public SimplePost() {
		this.jedis = new Jedis("redis://localhost:6379");
	}
 
	public void saveUser(String username) {
		jedis.sadd(USERS, username);
	}
	public Set<String> getUser() {
		return jedis.smembers(USERS);
	}
	
	public Set<String> getAllKeys() {
		return jedis.keys("*");
	}
 
	public static void main(String[] args) {
		SimplePost board = new SimplePost();
		// set some users
		String[] users = { "Ana", "Pedro", "Maria", "Luis" };
		for (String user: users) 
			board.saveUser(user);

		board.getAllKeys().forEach(System.out::println);

		board.getUser().forEach(System.out::println);
	}
}
