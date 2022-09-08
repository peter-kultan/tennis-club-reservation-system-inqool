package cz.models;

import javax.persistence.*;

@Entity
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "surfaceId", referencedColumnName = "id")
    private Surface surface;

    @Column(name = "hourPrice")
    private double hourPrice;

    public Court(Surface surface, double hourPrice) {
        this.surface = surface;
        this.hourPrice = hourPrice;
    }

    public Court() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if(id != null) {
            throw new IllegalStateException("Id can't be changed");
        }
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
