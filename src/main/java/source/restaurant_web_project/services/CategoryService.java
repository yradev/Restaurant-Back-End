package source.restaurant_web_project.services;

import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.dto.category.CategoriesPageViewDTO;
import source.restaurant_web_project.models.dto.category.CategoryAddDto;
import source.restaurant_web_project.models.dto.category.CategoryEditDTO;
import source.restaurant_web_project.models.dto.category.CategoryViewDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CategoryService {

    void addCategory(MultipartFile image, CategoryAddDto categoryAddDto) throws IOException, SQLException;

    void editCategory(MultipartFile image,long position, CategoryEditDTO categoryDTO) throws IOException, SQLException;

    void deleteCategory(long position);

    List<CategoryViewDTO> getAllCategories();
}
