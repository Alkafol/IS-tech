package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dto.Mapper;
import com.techprog.upgradedcats.dto.UserDto;
import com.techprog.upgradedcats.repository.UserRepository;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.security.PasswordConfig;
import com.techprog.upgradedcats.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OwnerService ownerService;
    private final PasswordConfig passwordConfig;
    private final Mapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, Mapper mapper, OwnerService ownerService, PasswordConfig passwordConfig){
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.ownerService = ownerService;
        this.passwordConfig = passwordConfig;
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public UserDto addUser(UserDto userDto) {
        User receivedUser = mapper.convertToUser(userDto);

        receivedUser.setPassword(passwordConfig.passwordEncoder().encode(receivedUser.getPassword()));
        User createdUser = userRepository.save(receivedUser);

        return mapper.convertToUserDto(createdUser);
    }

    public void deleteUser(String username) throws UserExistenceException, OwnerAccessibilityException,
            OwnerExistenceException, NonexistentFriendshipException, CatExistenceException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UserExistenceException();
        }

        ownerService.deleteOwnerById(user.getOwner().getId(), username);
        userRepository.delete(user);
    }

}
