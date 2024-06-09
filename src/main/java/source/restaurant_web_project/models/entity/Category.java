package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import jakarta.persistence.*;

import java.sql.Blob;
import java.util.List;

@Entity
@Table(name="categories")
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String description;
    private long position;

    private String imageUrl;

    @OneToMany(mappedBy = "category", targetEntity = Item.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Item> items;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
