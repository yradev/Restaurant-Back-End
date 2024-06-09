package source.restaurant_web_project.models.dto.item;

import java.util.List;

public class ItemsViewByPages {
    private long totalPages;
    private long totalEntities;
    private List<ItemViewDTO> items;

    public ItemsViewByPages(){}

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalEntities() {
        return totalEntities;
    }

    public void setTotalEntities(long totalEntities) {
        this.totalEntities = totalEntities;
    }

    public List<ItemViewDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemViewDTO> items) {
        this.items = items;
    }
}
