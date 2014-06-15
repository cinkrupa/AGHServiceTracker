package pl.edu.agh.servicetracker.request;

public class Item {

    private Long id;

    private String name;

    private String category;

    private String location;

    public Item(Long id, String name, String category, String location) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }
}
