package ru.VYurkin.TestFromEffectiveMobile.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @OneToMany(mappedBy = "user")
    private List<Organisation> organisations;

    @ElementCollection(targetClass = Role.class, fetch=FetchType.EAGER)
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name="role_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    @NotEmpty(message = "не должен быть пустым")
    @Size(min = 2, max = 50, message = "должен быть не короче 2 символов, и не длиннее 50 символов")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "не должен быть пустым")
    @Email(message = "должен быть вида user@mail.com")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "не должен быть пустым")
    @Column(name = "password")
    private String password;

    @Min(value = 0, message = "не может быть отрицательным")
    @Column(name = "balance")
    private float balance;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<Purchases> historyPurchase;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_delete")
    private Boolean isDelete;

}
