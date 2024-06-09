package source.restaurant_web_project.models.dto.item;

import java.util.List;

public class ItemsAllViewDTO {
    private String categoryName;
    private List<ItemViewDTO> items;
    public ItemsAllViewDTO(){}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ItemViewDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemViewDTO> items) {
        this.items = items;
    }
}
