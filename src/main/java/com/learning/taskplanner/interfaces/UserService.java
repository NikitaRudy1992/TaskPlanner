package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void registerNewUser(User user);
}
