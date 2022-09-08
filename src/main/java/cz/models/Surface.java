package cz.models;

import javax.persistence.*;

@Entity
public class Surface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Surface(String name) {
        this.name = name;
    }

    public Surface() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != null) {
            throw new IllegalStateException("Id can't be changed");
        }
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
