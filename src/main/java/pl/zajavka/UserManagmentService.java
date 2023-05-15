package pl.zajavka;

import java.util.List;
import java.util.Optional;

public interface UserManagmentService {

    void create(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
