package source.restaurant_web_project.models.entity.superClass;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public BaseEntity(){}
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
