package redis.ex6;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class MessageSystem {

    private final Jedis jedis;
    // stores all the users in the system
    public static String USERS_SET = "users";
    // stores all the users that a user follows
    public static String FOLLOWERS_SET = "followers";
    // stores the messages of a user
    public static String MESSAGES_LIST = "messages";

    public MessageSystem() {
            this.jedis = new Jedis("redis://localhost:6379");
    }

    // adds a user to the system
    public void addUser(String username) {
        jedis.sadd(USERS_SET, username);
    }

    // adds a follower to a user
    public void addFollower(String username, String follower) {
        jedis.sadd(FOLLOWERS_SET + ":" + username, follower);
    }

    // remove a user from the system
    public void removeUser(String username) {
        jedis.srem(USERS_SET, username);
        // remove all the followers of the user
        for (String follower : jedis.smembers(FOLLOWERS_SET + ":" + username)) {
            jedis.srem(FOLLOWERS_SET + ":" + follower, username);
        }
        // removes the set of followers of the user
        jedis.del(FOLLOWERS_SET + ":" + username);

        // removes all the messages of the user
        jedis.del(MESSAGES_LIST + ":" + username);
    }

    // removes a follower from a user
    public void removeFollower(String username, String follower) {
        jedis.srem(FOLLOWERS_SET + ":" + username, follower);
    }

    // stores a message to the system
    public void storeMsg(String username, String message) {
        jedis.rpush(MESSAGES_LIST + ":" + username, message);
    }

    // returns the messages of a user
    public String[] getMsgs(String username) {
        return jedis.lrange(MESSAGES_LIST + ":" + username, 0, -1).toArray(new String[0]);
    }

    // returns the followers of a user
    public String[] getFollowers(String username) {
        return jedis.smembers(FOLLOWERS_SET + ":" + username).toArray(new String[0]);
    }

    // returns all the users in the system
    public String[] getUsers() {
        return jedis.smembers(USERS_SET).toArray(new String[0]);
    }

    public static void main(String[] args) {
        MessageSystem ms = new MessageSystem();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Message System!\n");
        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        ms.addUser(username);

        int choice;
        while (true) {
            System.out.println("\nWhat would you like to do?\n");
            System.out.println("1. Send a message");
            System.out.println("2. Follow a user");
            System.out.println("3. Unfollow a user");
            System.out.println("4. See your message history");
            System.out.println("5. See your followers");
            System.out.println("6. See all users");
            System.out.println("7. Exit\n");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            System.out.println();

            switch (choice){
                case 1:
                    System.out.print("Enter your message: ");
                    sc.nextLine();
                    String message = sc.nextLine();
                    ms.storeMsg(username, message);
                    System.out.println("Message sent!");
                    break;
                case 2:
                    System.out.print("Enter the username of the user you want to follow: ");
                    sc.nextLine();
                    String userToFollow = sc.nextLine();
                    ms.addFollower(userToFollow, username);
                    System.out.println("You are now following " + userToFollow);
                    break;
                case 3:
                    System.out.print("Enter the username of the user you want to unfollow: ");
                    sc.nextLine();
                    String userToUnfollow = sc.nextLine();
                    ms.removeFollower(userToUnfollow, username);
                    System.out.println("You are no longer following " + userToUnfollow);
                    break;
                case 4:
                    System.out.println("Your message history:");
                    for (String msg : ms.getMsgs(username)) {
                        System.out.println(msg);
                    }
                    break;
                case 5:
                    System.out.println("Your followers:");
                    for (String follower : ms.getFollowers(username)) {
                        System.out.println(follower);
                    }
                    break;
                case 6:
                    System.out.println("All users:");
                    for (String user : ms.getUsers()) {
                        System.out.println(user);
                    }
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    ms.removeUser(username);
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

}