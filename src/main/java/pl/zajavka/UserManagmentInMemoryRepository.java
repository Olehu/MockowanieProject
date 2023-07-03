package pl.zajavka;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class UserManagmentInMemoryRepository implements UserManagmentRepository{

    public UserManagmentInMemoryRepository() {
    }

    private final Map<String, User> userMap = new HashMap<>();
    @Override
    public void create(final User user) {
        userMap.put(user.getEmail(), user);
    }

    public List<User> findByName(final String name) {
        return userMap.values().stream()
                .filter(user -> name.equals(user.getName()))
                .collect(Collectors.toList());
    }
//    private List<User> users = new ArrayList<>();

public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(userMap.get(email));
}

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }


    public void update(final String email, final User user) {
        if (!email.equals(user.getEmail())) {
            userMap.remove(email);
        }
        userMap.put(user.getEmail(), user);
    }

    public void delete(final String email) {
        userMap.remove(email);
    }
}
