package com.bookstore.app.bootstrap;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.entity.Role;
import com.bookstore.app.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadRole();
    }

    private void loadRole() {
        List<RoleType> roleTypes = List.of(RoleType.ADMIN, RoleType.USER);
        Map<RoleType, String> mapDescription = Map.of(
                RoleType.ADMIN, "This role is for admin",
                RoleType.USER, "This role is for user"
        );
        roleTypes.forEach(roleType ->  {
            roleRepository.findByName(roleType).ifPresentOrElse(System.out::println, () -> {
                Role role = new Role();
                role.setName(roleType);
                role.setDescription(mapDescription.get(roleType));
                roleRepository.save(role);
            });
        });
    }
}
