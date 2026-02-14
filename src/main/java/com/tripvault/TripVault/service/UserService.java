package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User Not Found"));
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
