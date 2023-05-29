package pl.zajavka;


import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
@With
public class User {
    String name;
    String surname;
    String email;

    public User get() {
        return null;
    }

    public String getEmail() {
        return email;
    }
}
