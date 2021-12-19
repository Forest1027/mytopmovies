package com.forest.mytopmovies.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User entity implements UserDetails to seamlessly integrates into Spring Security
 */
@Entity
@Table(name = "mtm_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "username", length = 15, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "verified")
    @ColumnDefault("false")
    private boolean verified;

    @Column(name = "active")
    @ColumnDefault("true")
    private boolean active;

    public User() {
    }

    public User(String id, String username, String password, String email, boolean verified, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.verified = verified;
        this.active = active;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", verified=" + verified +
                ", active=" + active +
                '}';
    }

    public static class UserBuilder {
        private String id;
        private String username;
        private String password;
        private String email;
        private boolean verified;
        private boolean active;

        public UserBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withVerified(boolean verified) {
            this.verified = verified;
            return this;
        }

        public UserBuilder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            return new User(id, username, password, email, verified, active);
        }
    }


}


