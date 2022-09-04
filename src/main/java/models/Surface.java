package models;

public class Surface {

    private int id;
    private double hourPrice;

    public Surface(int id, double hourPrice) {
        this.id = id;
        this.hourPrice = hourPrice;
    }

    public Surface(double hourPrice) {
        this.hourPrice = hourPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(double hourPrice) {
        this.hourPrice = hourPrice;
    }

    @Override
    public String toString() {
        return "Surface{" +
                "id=" + id +
                ", hourPrice=" + hourPrice +
                '}';
    }
}
