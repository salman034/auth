package com.auth.auth.entity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class 3: CustomUserDetails
 * Description:
 * Implements Spring Security's UserDetails interface to integrate
 * our Users entity with Spring Security authentication and authorization.
 */
@Getter
public class CustomUserDetails implements UserDetails{

    private final Users user; // Step 1: The Users entity wrapped by this class

    /**
     * Step 2: Constructor
     * Input: Users entity
     * Output: CustomUserDetails object
     */
    public CustomUserDetails(Users user) {
        this.user = user;
    }

    /**
     * Step 3: Get authorities/roles of the user
     * Input: none
     * Output: Collection of GrantedAuthority
     * Note: currently returns empty list; modify to return actual roles if needed.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Step 4: Get user's password
     * Input: none
     * Output: password string
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Step 5: Get username (used as principal)
     * Input: none
     * Output: emailId string
     */
    @Override
    public String getUsername() {
        return user.getEmailId();
    }

    /**
     * Step 6: Account expiration check
     * Input: none
     * Output: boolean
     * Note: always true; implement logic if you track account expiration.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Step 7: Account locked check
     * Input: none
     * Output: boolean
     * Note: always true; implement logic if you track account locking.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Step 8: Credentials expiration check
     * Input: none
     * Output: boolean
     * Note: always true; implement logic if you track password expiry.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Step 9: Account enabled check
     * Input: none
     * Output: boolean
     * Returns true only if Users.active is not null and true
     */
    @Override
    public boolean isEnabled() {
        return user.getActive() != null && user.getActive();
    }
}
