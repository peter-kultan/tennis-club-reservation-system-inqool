package cz.models;

public class Surface {

    public int id;
    public String name;

    public Surface(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Surface(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Surface{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
