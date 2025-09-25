package com.auth.auth.utils;

import com.auth.auth.entity.Users;
import com.auth.auth.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    private final UsersRepository usersRepository;

    public UserUtils(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Long getCurrentUserId() {
        String emailId = getCurrentUserEmail();
        if (emailId == null) return null;
        Users users = usersRepository.findByEmailId(emailId);
        return users != null ? users.getId() : null;
    }

    public String getCurrentUserEmailId() {
        return getCurrentUserEmail();
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

}
