package cbd.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.HashSet;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build()) {
            Session session = cluster.connect("videos");

            // a)
            // Vamos adicionar/pesquisar/atualizar/eliminar um utilizador
            System.out.println("Alinea a)");

            System.out.println("Inserting user...");
            insertUser(session, "Ze", "ze@example.com", "José");

            searchUser(session, "Ze");
            System.out.println();

            System.out.println("Updating user...");
            updateUser(session, "Ze", "jose@example.com", "Zezzocas");

            searchUser(session, "Ze");
            System.out.println();

            System.out.println("Deleting user...");
            deleteUser(session, "Ze");

            searchUser(session, "Ze");
            System.out.println();

            // Vamos também testar para os videos
            System.out.println("Inserting video...");
            Set<String> tags = new HashSet<>();
            tags.add("vlog");
            tags.add("top");
            tags.add("first");
            int id = insertVideo(session, "Ze", "Primeiro video do Canal", "Boas pessoal!", tags);

            searchVideo(session, id, "Ze");
            System.out.println();

            System.out.println("Deleting video...");
            deleteVideo(session, id, "Ze");

            searchVideo(session, id, "Ze");
            System.out.println();

            // b)
            System.out.println("Alinea b)");

            // 1. Os últimos 3 comentários introduzidos para um vídeo;
            System.out.println("\n1. Os últimos 3 comentários introduzidos para um vídeo;");
            ResultSet results = session.execute("SELECT * FROM videos.comment_video WHERE videoId = 4 LIMIT 3;");
            System.out.println(results.all());

            // 3. Todos os vídeos com a tag Aveiro;
            System.out.println("\n3. Todos os vídeos com a tag Aveiro;");
            results = session.execute("SELECT * FROM videos.video WHERE tags CONTAINS 'Aveiro';");
            System.out.println(results.all());

            // 6. Os últimos 10 vídeos, ordenado inversamente pela data da partilhada;
            System.out.println("\n6. Os últimos 10 vídeos, ordenado inversamente pela data da partilhada;");
            results = session.execute("SELECT * FROM videos.video LIMIT 10;");
            System.out.println(results.all());

            // 7. Todos os seguidores (followers) de determinado vídeo;
            System.out.println("\n7. Todos os seguidores (followers) de determinado vídeo;");
            results = session.execute("SELECT users FROM videos.follower WHERE videoId = 1;");
            System.out.println(results.all());

            session.close();
            cluster.close();
        } catch (Exception e) {
            System.err.println("Failed to connect to cluster: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // inserts
    private static void insertUser(Session session, String username, String email, String name) {
        session.execute("INSERT INTO videos.user (username, email, name, registryTime) VALUES ('" + username + "', '" + email + "', '" + name + "', toTimestamp(now()));");

    }

    private static int insertVideo(Session session, String author, String description, String name, Set<String> tags) {
        // primeiro temos de autoincrementar o id
        ResultSet results = session.execute("SELECT MAX(id) FROM videos.video;");
        int id = results.one().getInt(0) + 1;
        
        session.execute("INSERT INTO videos.video (id, author, uploadTime, description, name, tags) VALUES (" + id + ", '" + author + "', toTimestamp(now()), '" + description + "', '" + name + "', ?);", tags);

        return id;
    }

    // deletes
    private static void deleteUser(Session session, String username) {
        session.execute("DELETE FROM videos.user WHERE username = '" + username + "';");
    }

    private static void deleteVideo(Session session, int id, String author) {
        session.execute("DELETE FROM videos.video WHERE id = " + id + " AND author = '" + author + "';");
    }


    // searches
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

    private static ResultSet searchVideo(Session session, int id, String author) {
        ResultSet results = session.execute("SELECT * FROM videos.video WHERE id = " + id + " AND author = '" + author + "';");
        System.out.println(String.format("%-10s\t%-30s\t%-20s\t%-20s\t%-20s\t%-20s\n%s", "id", "author", "uploadTime", "description", "name", "tags",
                "----------+------------------------------+-----------------------+--------------------+--------------------+--------------------"));
        for (Row row : results) {
            System.out.println(String.format("%-10s\t%-30s\t%-20s\t%-20s\t%-20s\t%-20s", row.getInt("id"),
                    row.getString("author"), row.getTimestamp("uploadTime"), row.getString("description"), row.getString("name"), row.getSet("tags", String.class)));
        }
        return results;
    }

    // updates
    private static ResultSet updateUser(Session session, String username, String email, String name) {
        ResultSet results = session.execute("UPDATE videos.user SET email = '" + email + "', name = '" + name + "' WHERE username = '" + username + "';");
        return results;
    }
}