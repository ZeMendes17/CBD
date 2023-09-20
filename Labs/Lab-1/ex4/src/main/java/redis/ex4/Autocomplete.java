package redis.ex4;

import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;

public class Autocomplete {
    private final Jedis jedis;
    public static String LIST_NAME= "names";

    public Autocomplete() {
        this.jedis = new Jedis("redis://localhost:6379");
    }

    // to save names to a List from a file
    public List<String> loadNames(String file) {
        List<String> names = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(file));

            while (sc.hasNextLine()) {
                String name = sc.nextLine();
                names.add(name);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        return names;
    }

    // add elements of the list to Redis
    public void addNames(List<String> names) {
        for (String name : names) {
            jedis.rpush(LIST_NAME, name); // in order to stay in order it needs to be a rpush instead of a lpush
        }
    }

    // search for names that start with a given prefix
    public List<String> searchNames(String prefix) {
        List<String> matchingNames = new ArrayList<>();
        List<String> allNames = jedis.lrange(LIST_NAME, 0, -1);

        for (String name : allNames) {
            if (name.startsWith(prefix)) {
                matchingNames.add(name);
            }
        }

        return matchingNames;
    }

    // function to clear the redis database
    public void clear() {
        jedis.flushDB();
    }

    public static void main(String[] args) {
        Autocomplete auto = new Autocomplete();
        // insert all the names to a List
        List<String> names = auto.loadNames("../names.txt");
        // add the list to redis
        auto.addNames(names);

        // now we are able to search for the names
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Search for ('Enter' for quit): ");
            String prefix = sc.nextLine();
            if (prefix.isEmpty()) { // if the user enters empty string, quit
                auto.clear(); // if we want to clear the redis database
                break;
            }
            List<String> matchingNames = auto.searchNames(prefix);
            for(String name : matchingNames)
                System.out.println(name);
            System.out.println();
        }
    }
}
