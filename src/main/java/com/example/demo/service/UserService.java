package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;
import com.example.demo.model.RoleDTO;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.UserSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
        return UserSecurity.getFromUser(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.getOne(id);
    }

    public User updateUserRole(Integer id, RoleDTO roleDTO) {
        User user = userRepository.getOne(id);
        user.setRole(roleDTO.getRole());
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

}
