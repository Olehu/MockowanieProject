package pl.zajavka;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Data
@Repository
public class UserManagmentInMemoryRepository implements UserManagmentRepository{

    Map<String, User> mapOfUser = new HashMap<>();


    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = Optional.ofNullable(mapOfUser.get(email));
        return user;
    }

    @Override
    public Object findAll() {
        return null;
    }

    @Override
    public void create(User user) {
        mapOfUser.put(user.getEmail(), user);

    }
}
