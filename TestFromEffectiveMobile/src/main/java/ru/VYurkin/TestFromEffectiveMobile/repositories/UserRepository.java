package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query(value = "select * from users join user_role ur on users.user_id = ur.role_id where role = :role ", nativeQuery = true)
    List<User> findByRole(@Param("role") String role);

    }
