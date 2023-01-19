package com.techprog.outerinterfacesmicroservice.dto;

import com.techprog.entities.user.ApplicationUserRole;
import com.techprog.entities.user.User;
import org.springframework.stereotype.Service;

@Service
public class Mapper {

    Mapper(){}

    public UserInfoDto convertToUserDto(User user, OwnerDto ownerDto){
        return new UserInfoDto(
                user.getUsername(),
                user.getRole().name(),
                user.getOwnerId().toString(),
                ownerDto.getName(),
                ownerDto.getDateOfBirth(),
                ownerDto.getCatsId()
        );
    }

    public User convertToUserFromUserCreationDto(UserCreationDto userCreationDto){
        return new User(
                userCreationDto.getUsername(),
                userCreationDto.getPassword(),
                ApplicationUserRole.valueOf(userCreationDto.getRole()),
                userCreationDto.getOwnerId() == null ? null : Integer.parseInt(userCreationDto.getOwnerId())
        );
    }
}
