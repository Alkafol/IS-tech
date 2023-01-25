package com.techprog.outerinterfacesmicroservice.service;

import com.techprog.outerinterfacesmicroservice.dto.*;
import com.techprog.entities.user.User;
import com.techprog.outerinterfacesmicroservice.repository.UserRepository;
import com.techprog.outerinterfacesmicroservice.security.PasswordConfig;
import com.techprog.outerinterfacesmicroservice.tools.CatMicroserviceException;
import com.techprog.outerinterfacesmicroservice.tools.OwnerMicroserviceException;
import com.techprog.outerinterfacesmicroservice.tools.UserExistenceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final Mapper mapper;
    private final RequestSender requestSender;

    public UserService(UserRepository userRepository, Mapper mapper, PasswordConfig passwordConfig, RequestSender requestSender){
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.passwordConfig = passwordConfig;
        this.requestSender = requestSender;
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserInfoDto addUser(UserCreationDto userCreationDto) throws OwnerMicroserviceException, UserExistenceException {
        if (loadUserByUsername(userCreationDto.getUsername()) != null){
            throw new UserExistenceException("User with given username already exists");
        }

        OwnerDto ownerDto = mapper.convertToOwnerDtoFromUserCreationDto(userCreationDto);
        OwnerDto createdOwner = requestSender.addOwner(ownerDto);

        User receivedUser = mapper.convertToUserFromUserCreationDto(userCreationDto);
        receivedUser.setOwnerId(Integer.parseInt(createdOwner.getId()));
        receivedUser.setPassword(passwordConfig.passwordEncoder().encode(receivedUser.getPassword()));

        User createdUser = userRepository.save(receivedUser);

        return mapper.convertToUserDto(createdUser, createdOwner);
    }

    public void deleteUser(String username) throws CatMicroserviceException, OwnerMicroserviceException, UserExistenceException {
        User user = loadUserByUsername(username);
        if (user == null){
            throw new UserExistenceException("User doesn't exits");
        }

        OwnerDto ownerDto = requestSender.getOwnerById(user.getOwnerId());
        for (String catId : ownerDto.getCatsId()){
            requestSender.deleteCat(Integer.parseInt(catId));
        }
        requestSender.deleteOwner(Integer.parseInt(ownerDto.getId()));

        userRepository.delete(user);
    }

    public List<UserInfoDto> getAllUsers() throws OwnerMicroserviceException {
        List<User> allUsers = userRepository.findAll();
        List<UserInfoDto> allUsersInfo = new ArrayList<>();

        for (User user : allUsers){
            OwnerDto ownerDto = requestSender.getOwnerById(user.getOwnerId());
            allUsersInfo.add(mapper.convertToUserDto(user, ownerDto));
        }

        return allUsersInfo;
    }

    public UserInfoDto getUserByUsername(String username) throws OwnerMicroserviceException, UserExistenceException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UserExistenceException("User with given username doesn't exist");
        }
        OwnerDto ownerDto = requestSender.getOwnerById(user.getOwnerId());

        return  mapper.convertToUserDto(user, ownerDto);
    }
}
