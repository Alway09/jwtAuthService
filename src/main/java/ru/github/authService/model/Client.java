package ru.github.authService.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client implements Persistable<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login", nullable = false, unique = true)
    @Size(max = 128)
    private String login;

    @Column(name = "password", nullable = false)
    @Size(min = 5, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    public Client(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(Hibernate.getClass(o))) {
            return false;
        }
        Client that = (Client) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
