package ru.VYurkin.TestFromEffectiveMobile.models.user;

import jakarta.persistence.*;
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

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

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
