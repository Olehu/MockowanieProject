package pl.zajavka;

import java.util.List;
import java.util.Optional;

public interface UserManagmentRepository {

    void create(final User user);

    List<User> findByName(final String name);

    Optional<User> findByEmail(final String email);

    List<User> findAll();
    void update(final String email, final User user);
    void delete(final String email);
}
