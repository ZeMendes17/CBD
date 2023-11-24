package cbd.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class App {
    public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build()) {
            Session session = cluster.connect("videos");

            // a)
            System.out.println("Inserting user...");
            insertUser(session, "Ze", "ze@example.com", "José");

            System.out.println("User:");
            searchUser(session, "Ze");
        } catch (Exception e) {
            System.err.println("Failed to connect to cluster: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static void insertUser(Session session, String username, String email, String name) {
        session.execute("INSERT INTO videos.user (username, email, name, registryTime) VALUES ('" + username + "', '" + email + "', '" + name + "', toTimestamp(now()));");

    }

    private static void deleteUser(Session session, String username) {
        session.execute("DELETE FROM videos.user WHERE username = '" + username + "';");
    }


    private static ResultSet searchUser(Session session, String username) {

        ResultSet results = session.execute("SELECT * FROM videos.user WHERE username = '" + username + "';");
        System.out.println(String.format("%-30s\t%-20s\t%-20s\t%-20s\n%s", "username", "email", "name", "registryTime",
                "-------------------------------+-----------------------+--------------------+--------------------"));
        for (Row row : results) {
            System.out.println(String.format("%-30s\t%-20s\t%-20s\t%-20s", row.getString("username"),
                    row.getString("email"), row.getString("name"), row.getTimestamp("registryTime")));
        }
        return results;
    }

    private static ResultSet updateUSer(Session session, )
}