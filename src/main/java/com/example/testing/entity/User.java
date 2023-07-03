package com.example.testing.entity;

import com.example.testing.utils.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Table(name = "user")
@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean isEnable;
    private Boolean isExpired;
    private Boolean isLocked;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(Long id) {
        this.id = id;
    }

    public User(String name,
                String password,
                String email,
                UserRole role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.userRole = role;
        this.isLocked = false;
        this.isExpired = false;
        this.isEnable = true;
    }

    public User(Long id, String name,
                String password, String email,
                String country, Boolean isEnable,
                Boolean isExpired, Boolean isLocked,
                UserRole userRole) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.isEnable = isEnable;
        this.isExpired = isExpired;
        this.isLocked = isLocked;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
