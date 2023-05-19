package pl.zajavka;

import java.util.Optional;

public interface UserManagmentRepository {

    Optional<User> findByEmail(String email);

    Object findAll();

    void create(User user);
}
