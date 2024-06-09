package source.restaurant_web_project.services;

import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.dto.item.*;

import java.io.IOException;
import java.sql.SQLException;

public interface ItemService {
    void addItem(MultipartFile image, ItemAddDto itemAddDTO) throws IOException, SQLException;

    void deleteItem(long categoryPosition, long itemPosition);

    void editItem(MultipartFile image,long categoryPosition, long itemPosition, ItemEditDTO itemEditDTO) throws IOException, SQLException;

    ItemsAllViewDTO getAllItemsByCategory(long position);

    ItemFavoritesDTO getItemById(long id) throws SQLException, IOException;
}
