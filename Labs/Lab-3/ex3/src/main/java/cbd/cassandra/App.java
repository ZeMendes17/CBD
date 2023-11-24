package cbd.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class App {
    public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build()) {
            Session session = cluster.connect("videos");

            insertUser(session, "Ze", "ze@example.com", "José");
            System.out.println("User inserted");
            deleteUser(session, "Ze");
            System.out.println("User deleted");
        } catch (Exception e) {
            System.err.println("Failed to connect to cluster: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static void insertUser(Session session, String username, String email, String name) {
        session.execute("INSERT INTO user (username, email, name, registTime) VALUES ('" + username + "', '" + email + "', '" + name + "', toTimestamp(now()));");

    }

    private static void deleteUser(Session session, String username) {
        session.execute("DELETE FROM user WHERE username = '" + username + "';");
    }

}