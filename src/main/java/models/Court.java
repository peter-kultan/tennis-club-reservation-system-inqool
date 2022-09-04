package models;

public class Court {

    private int id;
    private Surface surface;

    public Court(int id, Surface surface) {
        this.id = id;
        this.surface = surface;
    }

    public Court(Surface surface) {
        this.surface = surface;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    @Override
    public String toString() {
        return "Court{" +
                "id=" + id +
                ", surface=" + surface +
                '}';
    }
}
