package ch.hemisoft.immo.domain;

import static java.util.Objects.requireNonNull;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import groovy.transform.ToString;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@NoArgsConstructor(access=PROTECTED)
@RequiredArgsConstructor
@ToString(excludes = User.PASSWORD)
@EqualsAndHashCode(of = User.USER_NAME)
@Getter @Setter
public class User implements PasswordProtectable {
    static final String ID = "id"; 
    static final String USER_NAME = "userName"; 
    static final String PASSWORD = "password"; 
    static final String EMAIL = "email"; 
    static final String ENABLED = "enabled"; 
    
    @Id @GeneratedValue(strategy = IDENTITY)    Long    id;
    @NonNull @NotNull                           String  userName;
    @NonNull @NotNull                           String  password;
    @NonNull @NotNull                           String  email;
    @NonNull @NotNull                           Boolean enabled;

    @JoinTable(
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_TO_USER"),
            inverseForeignKey = @ForeignKey(name = "FK_USER_TO_USER_ROLE")
    )
    @ManyToMany(fetch = EAGER)
    private Collection<UserRole> userRoles = new ArrayList<>();
    
    public User(User user) {
        id = requireNonNull(user.getId());
        userName = requireNonNull(user.getUserName());
        password = requireNonNull(user.getPassword());
        email = requireNonNull(user.getEmail());
        enabled = requireNonNull(user.getEnabled());
    }
}
