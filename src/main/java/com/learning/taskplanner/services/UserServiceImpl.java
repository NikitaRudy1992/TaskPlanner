package com.learning.taskplanner.services;

import com.learning.taskplanner.exceptions.CustomUsernameExistsException;
import com.learning.taskplanner.interfaces.UserService;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.UserRole;
import com.learning.taskplanner.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public void registerNewUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new CustomUsernameExistsException("Username already exists.");
                });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
