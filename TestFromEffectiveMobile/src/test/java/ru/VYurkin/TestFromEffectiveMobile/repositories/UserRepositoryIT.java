package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Role;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts={"/sql/clearDbs.sql", "/sql/user/AddUsers.sql"})
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void shouldFindUserByUsername(){
        //given
        Optional<User> user1 = userRepository.findByUsername("user1");
        Optional<User> user2 = userRepository.findByUsername("user2");
        //then
        Assertions.assertTrue(user1.isPresent());
        Assertions.assertTrue(user2.isPresent());
        Assertions.assertEquals("user1", user1.get().getUsername());
        Assertions.assertEquals("user2", user2.get().getUsername());
    }

    @Test
    public void shouldFindUserByEmail(){
        //given
        Optional<User> user1 = userRepository.findByEmail("user1@mail.ru");
        Optional<User> user2 = userRepository.findByEmail("user2@mail.ru");
        //then
        Assertions.assertTrue(user1.isPresent());
        Assertions.assertTrue(user2.isPresent());
        Assertions.assertEquals("user1@mail.ru", user1.get().getEmail());
        Assertions.assertEquals("user2@mail.ru", user2.get().getEmail());
    }
    @Test
    public void shouldFindAllAdminsAndUsers(){
        //given

        List<User> admins = userRepository.findByRole(Role.ADMIN.toString());
        List<User> users = userRepository.findByRole(Role.USER.toString());
        //then
        Assertions.assertFalse(admins.isEmpty());
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(1, admins.get(0).getUserId());
        Assertions.assertEquals(3, admins.get(1).getUserId());
        Assertions.assertEquals(2, users.get(0).getUserId());
        Assertions.assertEquals(4, users.get(1).getUserId());
    }
}
