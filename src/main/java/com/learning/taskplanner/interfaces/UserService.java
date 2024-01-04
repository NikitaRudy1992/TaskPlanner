package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void registerNewUser(User user);
    List<User> getAllUsers();
    void deleteUser(Long userId);
}
