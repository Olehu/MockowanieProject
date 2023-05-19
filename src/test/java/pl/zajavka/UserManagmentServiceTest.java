package pl.zajavka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserManagmentServiceTest {

    @InjectMocks
    private UserManagmentService userManagmentService;

    @Mock
    private UserManagmentRepository userManagmentRepository;

    @Test
    void shouldCreateUserCorrectly() {
        //given
        var user = someUser();

        when(UserManagmentRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user));
        //when
        userManagmentService.create(user);

        var result = userManagmentService.findByEmail(user.getEmail());

        //then

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    void shouldCreateMultipleUsersCorrectly() {
        //given
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");

        when(userManagmentRepository.findByEmail(user1.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user1));
        when(userManagmentRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user2));
        when(userManagmentRepository.findByEmail(user3.getEmail()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user3));

        when(userManagmentRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        //when
        userManagmentService.create(user1);
        userManagmentService.create(user2);
        userManagmentService.create(user3);

        var result1 = userManagmentService.findByEmail(user1.getEmail());
        var result2 = userManagmentService.findByEmail(user2.getEmail());
        var result3 = userManagmentService.findByEmail(user3.getEmail());
        var all = userManagmentService.findAll();

        //then
        Assertions.assertEquals(3, all.size());
        Assertions.assertTrue(result1.isPresent());
        Assertions.assertEquals(user1, result1.get());
        Assertions.assertTrue(result2.isPresent());
        Assertions.assertEquals(user2, result2.get());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user3, result3.get());




    }

    @Test
    void shouldFailWhenDuplicatedUserIsCreated() {
        //given
        String duplicatedEmail = "someemail@gmail.com";
        var user1 = someUser().withEmail(duplicatedEmail);
        var user2 = someUser().withEmail(duplicatedEmail);

        when(userManagmentRepository.findByEmail(duplicatedEmail))
                .thenReturn(Optional.empty())
                .thenThrow(new RuntimeException(String.format("User with email: [%s] is already created", user1.getEmail())));


        //when ,then
        userManagmentService.create(user1);
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> userManagmentService.create(user2));
        Assertions.assertEquals(String.format("User with email: [%s] is already created", user1.getEmail()), exception.getMessage());


    }

    @Test
    void shouldFailWhenAddingUsersToRepositoryFails() {
        //given
        String errorMassage = "Error while creating user";

        var user = someUser();
        when(UserManagmentRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        doThrow(new RuntimeException(errorMassage)).when(userManagmentRepository).create(user);

        //when, then
        Throwable excception = Assertions.assertThrows(RuntimeException, () -> userManagmentService.create(user));
        Assertions.assertEquals(errorMassage, excception.getMessage());


    }

    @Test
    void ShouldFindUsersWithName() {
        //given
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withName("NewName").withEmail("email3@gmail.com");
        when(userManagmentRepository.findByEmail(user1.getName())).thenReturn(List.of(user1, user2))
        when(userManagmentRepository.findByEmail(user3.getName())).thenReturn(List.of(user3));
        when(userManagmentRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        //when
        var result1 = userManagmentService.findByName(user1.getName());
        var result2 = userManagmentService.findByName(user2.getName());
        var result3 = userManagmentService.findByName(user3.getName());
        var all = userManagmentService.findAll();


        //then

        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals(Stream.of(user1, user2).sorted().collect(Collectors.toList()), result2.stream().sorted().collect(Collectors.toList()));
        Assertions.assertEquals(List.of(user3), result3);


    }

    @Test
    void shouldModifyUserDataCorrectly() {
        //given
        String email1 = "email1@gmail.com";
        String email2 = "email2@gmail.com";
        String email3 = "email3@gmail.com";
        String emailNew = "newEmail@gmail.com";
        var user1 = someUser().withEmail(email1);
        var user2 = someUser().withEmail(email2);
        var user3 = someUser().withEmail(email3);

        when(userManagmentRepository.findByEmail(email1))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user1))
                .then(Optional.of(user1))
                .thenReturn(Optional.empty());
        when(userManagmentRepository.findByEmail(email2))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user2));
        when(userManagmentRepository.findByEmail(email3))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user3));
        when(userManagmentRepository.findByEmail(emailNew))
                .thenReturn(Optional.of(user1.withEmail(emailNew)));
        when(userManagmentRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        //when
        userManagmentService.create(user1);
        userManagmentService.create(user2);
        userManagmentService.create(user3);

        var all = userManagmentService.findAll();
        Assertions.assertEquals(3, all.size());

        var result1 = userManagmentService.findByEmail(user1.getEmail());
        userManagmentService.update(user1.getEmail(), user1.withEmail(emailNew));
        var result2 = userManagmentService.findByEmail(user1.getEmail());
        var result3 = userManagmentService.findByEmail(emailNew);
        var allAgain = userManagmentService.findAll();

        //then
        Assertions.assertEquals(3, allAgain.size());
        Assertions.assertTrue(result1.isPresent());
        Assertions.assertEquals(user1, result1.get());
        Assertions.assertTrue(result2.isEmpty());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user1.withEmail(emailNew), result3.get());
    }

    @Test
    void shouldThrowWhenModifyingNonExistingUser() {
        //given
        var user1 = someUser();
        String newEmail = "email1@gmail.com";

        //when,then
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> userManagmentService.update(user1.getEmail(), user1.withEmail(newEmail)));
        Assertions.assertEquals(String.format("User with email: [%s] doeesn't exist"), user1.getEmail(), exception.getMessage());

    }

    @Test
    void shouldDeleteUserCorrectly() {
        //given
        var user1 = someUser().withEmail("email1@gmail.com");
        var user2 = someUser().withEmail("email2@gmail.com");
        var user3 = someUser().withEmail("email3@gmail.com");

        when(userManagmentRepository.findByEmail(user1.getEmail()))
                .thenReturn(Optional.of(user1))
                .thenReturn(Optional.empty());
        when(userManagmentRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.of(user2));
        when(userManagmentRepository.findByEmail(user3.getEmail()))
                .thenReturn(Optional.of(user3));
        when(userManagmentRepository.findAll())
                .thenReturn(List.of(user1, user2, user3))
                .thenReturn(List.of(user1, user2));

        //when

        var all = userManagmentService.findAll();
        Assertions.assertEquals(3, all.size());

        userManagmentService.delete(user1.getEmail());

        var result1 = userManagmentService.findByEmail(user1.getEmail());
        var result2 = userManagmentService.findByEmail(user2.getEmail());
        var result3 = userManagmentService.findByEmail(user3.getEmail());

        var allAgain = userManagmentService.findAll();

        //then
        Assertions.assertEquals(2, allAgain.size());
        Assertions.assertTrue(result1.isEmpty());
        Assertions.assertTrue(result2.isPresent());
        Assertions.assertEquals(user2, result2.get());
        Assertions.assertTrue(result3.isPresent());
        Assertions.assertEquals(user3, result3.get());
    }

    @Test
    void shouldTrhowWhenDeletingnonExistingUser() {
        //given
        var user1 = someUser().withEmail("email1@gmail.com");

        //when,then
        Throwable exception = Assertions.assertThrows(RuntimeException, () -> userManagmentService.delete(user1.getEmail()));
        Assertions.assertEquals(String.format("User with email: [$s] doens't Exist", user1.getEmail(), exception.getMessage()));
    }

    private static User someUser() {
        return User.builder()
                .name("name")
                .surname("surname")
                .email("email@gmail.com")
                .build();
    }



}
