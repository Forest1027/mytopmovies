package com.forest.mytopmovies.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

/**
 * User entity implements UserDetails to seamlessly integrates into Spring Security
 */
@Entity
@Table(name = "mtm_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "is_active")
    private boolean isActive;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email,
                @JsonProperty("isVerified") boolean isVerified,
                @JsonProperty("isActive") boolean isActive) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isVerified = isVerified;
        this.isActive = isActive;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return false;
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
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", isVerified=" + isVerified +
                ", isActive=" + isActive +
                '}';
    }

    public static class UserBuilder {
        private String id;
        private String username;
        private String password;
        private String email;
        private boolean isVerified;
        private boolean isActive;

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

        public UserBuilder withIsVerified(boolean isVerified) {
            this.isVerified = isVerified;
            return this;
        }

        public UserBuilder withIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public User build() {
            return new User(id, username, password, email, isVerified, isActive);
        }
    }


}


