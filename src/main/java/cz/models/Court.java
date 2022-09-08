package cz.models;

public class Court {

    private int id;
    private Surface surface;
    private double hourPrice;

    public Court(int id, Surface surface, double hourPrice) {
        this.id = id;
        this.surface = surface;
        this.hourPrice = hourPrice;
    }

    public Court(Surface surface, double hourPrice) {
        this.surface = surface;
        this.hourPrice = hourPrice;
    }

    public Court() {}

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

    public double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(double hourPrice) {
        this.hourPrice = hourPrice;
    }

    @Override
    public String toString() {
        return "Court{" +
                "id=" + id +
                ", surface=" + surface +
                ", hourPrice=" + hourPrice +
                '}';
    }
}
