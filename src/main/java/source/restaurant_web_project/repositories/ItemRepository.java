package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findItemsByCategory_Name(String categoryName);
    List<Item> findItemsByCategory_Position(long position);

    Item findItemByCategory_PositionAndPosition(long categoryPosition, long itemPosition);
    Item findItemByName(String name);
}
