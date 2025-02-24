package com.bookstore.app.bootstrap;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.entity.Role;
import com.bookstore.app.entity.User;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.RoleRepository;
import com.bookstore.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminAndRoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}")
    String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    String adminPassword;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadRole();
        loadAdmin();
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

    private void loadAdmin() {
        Role role = roleRepository.findByName(RoleType.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name: " + RoleType.ADMIN.name() + " not found"));
        if(userRepository.findByEmail(adminEmail).isPresent()) return;
        User userAdmin = new User();
        userAdmin.setEmail(adminEmail);
        userAdmin.setFullName("Cao Ba Hieu");
        userAdmin.setPassword(passwordEncoder.encode(adminPassword));
        userAdmin.setPhone("0987183624");
        userAdmin.setAddress("Ha Noi");
        userAdmin.setActive(true);
        userAdmin.setRoles(Set.of(role));

        userRepository.save(userAdmin);
    }
}
