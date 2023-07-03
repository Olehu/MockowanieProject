package pl.zajavka;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserManagmentService {
    private final UserManagmentRepository userManagmentRepository;

    public void create(final User user) {
        if (userManagmentRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException(String.format("User with email: [%s] is already created", user.getEmail()));
        }
        userManagmentRepository.create(user);
    }

    public List<User> findByName(final String name) {
        return userManagmentRepository.findByName(name);
    }

    public Optional<User> findByEmail(String email) {
        return userManagmentRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userManagmentRepository.findAll();
    }

    public void update(String email, User updatedUser) {
        if (userManagmentRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException(String.format("User with email: [%s] is already created", email));
        }
        userManagmentRepository.update(email, updatedUser);
    }

    public void delete(String email) {
        Optional<User> existingUser = userManagmentRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User with email: [" + email + "] doesn't exist");
        }
        userManagmentRepository.delete(email);
    }
}
