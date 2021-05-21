package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;
import com.example.demo.model.RoleDTO;
import com.example.demo.repo.UserRepository;

import static com.example.demo.security.UserSecurity.getFromUser;

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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

    public User updateUserRole(Integer id, RoleDTO roleDTO) {
        User user = findById(id);
        user.setRole(roleDTO.getRole());
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.delete(findById(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
                                  
        return getFromUser(user);
    }

}
