package com.examly.springapp.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.examly.springapp.model.User;

/**
 * This class implements the UserDetails interface and provides
 * the necessary user details for Spring Security.
 */
public class UserPrinciple implements UserDetails {

    private User user;
    /**
     * Constructor to initialize UserPrinciple with a User object.
     */
    public UserPrinciple(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     * In this case, it returns the user's role prefixed with "ROLE_".
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + user.getUserRole());
    }

    /**
     * Returns the password of the user.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username of the user.
     * In this case, it returns the user's email.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     * Returns true, indicating the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Returns true, indicating the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * Returns true, indicating the credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * Returns true, indicating the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Static method to build a UserPrinciple object from a User object.
     */
    public static UserDetails build(User user) {
        return new UserPrinciple(user);
    }
}
