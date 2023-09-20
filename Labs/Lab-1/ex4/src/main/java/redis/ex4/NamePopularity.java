package redis.ex4;

public class NamePopularity implements Comparable<NamePopularity> {
    private String name;
    private int popularity;

    public NamePopularity(String name, int popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName() {
        return this.name;
    }

    public int getPopularity() {
        return this.popularity;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public int compareTo(NamePopularity other) {
        return Integer.compare(this.popularity, other.popularity);
    }
}

