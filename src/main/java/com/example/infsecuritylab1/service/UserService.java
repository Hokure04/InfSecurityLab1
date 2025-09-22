package com.example.infsecuritylab1.service;

import com.example.infsecuritylab1.exception.AuthorizeException;
import com.example.infsecuritylab1.model.Role;
import com.example.infsecuritylab1.model.User;
import com.example.infsecuritylab1.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;


    public void addUser(final User user){
        userRepository.save(user);
        log.info("{} registered successfully", user.getEmail());
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public long countUsers(){
        return userRepository.count();
    }

    public boolean isExist(final String email){
        Optional<User> userForCheck = userRepository.findByEmail(email);
        if(userForCheck.isPresent()){
            log.info("User with email: {} already exist", email);
            return true;
        }
        log.info("User with email: {} not exist", email);
        return false;
    }

    public User getUserByEmail(final String email){
        if(!isExist(email)){
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        return userRepository.findByEmail(email).get();
    }


    public User promoteToAdmin(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRole(Role.ROLE_ADMIN);
        return userRepository.save(user);
    }

    public User blockUser(Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: "+ email + "not found"));
        if(currentUser.getId().equals(id)){
            throw new AuthorizeException("Вы не можете заблокировать сами себя");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(false);
        return userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) {
        return getUserDetailsService().loadUserByUsername(username);
    }

    public UserDetailsService getUserDetailsService() {
        return this::getUserByEmail;
    }
}
