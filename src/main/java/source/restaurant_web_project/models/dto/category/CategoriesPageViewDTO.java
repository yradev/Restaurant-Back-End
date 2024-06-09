package source.restaurant_web_project.models.dto.category;

import source.restaurant_web_project.models.dto.superClass.ListViewDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoriesPageViewDTO extends ListViewDTO {
    private List<CategoryViewDTO> categories;

    public CategoriesPageViewDTO(long currentPage, long totalPages, long totalEntities) {
        super(currentPage, totalPages, totalEntities);
        categories = new ArrayList<>();
    }

    public List<CategoryViewDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryViewDTO> categories) {
        this.categories = categories;
    }
}
