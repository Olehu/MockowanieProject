package pl.zajavka;


import lombok.Builder;
import lombok.Value;
import lombok.With;

@Builder
@Value
@With
public class User {
    String name;
    String surname;
    String email;

    public User get() {
    }
}
