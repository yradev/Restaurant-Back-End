package source.restaurant_web_project.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.repositories.RestaurantConfigurationRepository;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;
import source.restaurant_web_project.configurations.enums.Roles;
import source.restaurant_web_project.models.entity.Role;
import source.restaurant_web_project.repositories.RoleRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializator implements CommandLineRunner {
    private final RestaurantConfigurationRepository confgurationRepository;
    private final RoleRepository roleRepository;

    public DataInitializator(RestaurantConfigurationRepository confgurationRepository, RoleRepository roleRepository) {
        this.confgurationRepository = confgurationRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() != Roles.values().length) {
            List<String> currentRolesNames = roleRepository.findAll().stream().map(Role::getName).toList();
            Arrays.stream(Roles.values())
                    .filter(role -> !currentRolesNames.contains(role.name()))
                    .map(role -> {
                        Role tempRole = new Role();
                        tempRole.setName(role.name());
                        return tempRole;
                    })
                    .forEach(roleRepository::save);
        }

        if (confgurationRepository.count() == 0) {
            RestaurantConfigurationEntity restaurantConfigurationEntity = new RestaurantConfigurationEntity();
            confgurationRepository.save(restaurantConfigurationEntity);
        }
    }
}
