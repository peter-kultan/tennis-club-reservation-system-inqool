package cz.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Surface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

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
        return "Surface{" + "id=" + id + ", name='" + name + '\'' + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Surface surface = (Surface) o;
        return Objects.equals(id, surface.id) && Objects.equals(name, surface.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
