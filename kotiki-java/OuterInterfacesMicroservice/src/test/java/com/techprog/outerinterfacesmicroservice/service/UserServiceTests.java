package com.techprog.outerinterfacesmicroservice.service;

import com.techprog.entities.user.User;
import com.techprog.outerinterfacesmicroservice.dto.Mapper;
import com.techprog.outerinterfacesmicroservice.dto.OwnerDto;
import com.techprog.outerinterfacesmicroservice.dto.UserCreationDto;
import com.techprog.outerinterfacesmicroservice.dto.UserInfoDto;
import com.techprog.outerinterfacesmicroservice.repository.UserRepository;
import com.techprog.outerinterfacesmicroservice.security.PasswordConfig;
import com.techprog.outerinterfacesmicroservice.tools.UserExistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @Mock
    Mapper mapper;

    @Mock
    RequestSender requestSender;

    @Mock
    PasswordConfig passwordConfig;

    @InjectMocks
    UserService underTest;

    @Test
    public void testAddUser() throws Exception{
        // given
        String username = "alkafol";
        String ownerId = "1";
        String password = "12345";
        String encodedPassword = "23456";
        UserCreationDto userCreationDto = mock(UserCreationDto.class);
        OwnerDto ownerDtoFromUserCreationDto = mock(OwnerDto.class);
        OwnerDto savedOwnerDto = mock(OwnerDto.class);
        User user = mock(User.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        User createdUser = mock(User.class);
        UserInfoDto createdUserInfoDto = mock(UserInfoDto.class);

        underTest = spy(underTest);
        doReturn(username).when(userCreationDto).getUsername();
        doReturn(null).when(underTest).loadUserByUsername(username);
        doReturn(ownerDtoFromUserCreationDto).when(mapper).convertToOwnerDtoFromUserCreationDto(userCreationDto);
        doReturn(savedOwnerDto).when(requestSender).addOwner(ownerDtoFromUserCreationDto);
        doReturn(user).when(mapper).convertToUserFromUserCreationDto(userCreationDto);
        doReturn(ownerId).when(savedOwnerDto).getId();
        doReturn(password).when(user).getPassword();
        doReturn(passwordEncoder).when(passwordConfig).passwordEncoder();
        doReturn(encodedPassword).when(passwordEncoder).encode(password);
        doNothing().when(user).setPassword(encodedPassword);
        doReturn(createdUser).when(userRepository).save(user);
        doReturn(createdUserInfoDto).when(mapper).convertToUserDto(createdUser, savedOwnerDto);

        // when
        UserInfoDto actualUserInfoDto =  underTest.addUser(userCreationDto);

        // then
        verify(requestSender).addOwner(ownerDtoFromUserCreationDto);
        verify(user).setOwnerId(Integer.parseInt(ownerId));
        verify(user).setPassword(anyString());
        verify(userRepository).save(user);
        assertEquals(createdUserInfoDto, actualUserInfoDto);
    }

    @Test
    public void testAddUserUserExistenceException(){
        // given
        String username = "alkafol";
        UserCreationDto userCreationDto = mock(UserCreationDto.class);
        User existedUser = mock(User.class);
        underTest = spy(underTest);
        doReturn(username).when(userCreationDto).getUsername();
        doReturn(existedUser).when(underTest).loadUserByUsername(username);

        // when
        // then
        assertThrows(UserExistenceException.class, () -> underTest.addUser(userCreationDto));
    }

    @Test
    public void testDeleteUser() throws Exception{
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        String catId = "1";
        User user = mock(User.class);
        OwnerDto ownerDto = mock(OwnerDto.class);
        underTest = spy(underTest);

        doReturn(user).when(underTest).loadUserByUsername(username);
        doReturn(ownerId).when(user).getOwnerId();
        doReturn(ownerDto).when(requestSender).getOwnerById(ownerId);
        doReturn(List.of(catId)).when(ownerDto).getCatsId();
        doNothing().when(requestSender).deleteCat(Integer.parseInt(catId));
        doReturn(ownerId.toString()).when(ownerDto).getId();
        doNothing().when(requestSender).deleteOwner(ownerId);
        doNothing().when(userRepository).delete(user);

        // when
        underTest.deleteUser(username);

        // then
        verify(requestSender).getOwnerById(ownerId);
        verify(requestSender).deleteCat(Integer.valueOf(catId));
        verify(requestSender).deleteOwner(ownerId);
        verify(userRepository).delete(user);
    }

    @Test
    public void testDeleteUserUserExistenceException(){
        // given
        String username = "alkafol";
        underTest = spy(underTest);
        doReturn(null).when(underTest).loadUserByUsername(username);

        // when
        // then
        assertThrows(UserExistenceException.class, () -> underTest.deleteUser(username));
    }

    @Test
    public void getAllUsers() throws Exception{
        // given
        Integer ownerId = 1;
        User user = mock(User.class);
        OwnerDto ownerDto = mock(OwnerDto.class);
        UserInfoDto userInfoDto = mock(UserInfoDto.class);

        doReturn(List.of(user)).when(userRepository).findAll();
        doReturn(ownerId).when(user).getOwnerId();
        doReturn(ownerDto).when(requestSender).getOwnerById(ownerId);
        doReturn(userInfoDto).when(mapper).convertToUserDto(user, ownerDto);

        // when
        List<UserInfoDto> allUsers = underTest.getAllUsers();

        // then
        verify(userRepository).findAll();
        verify(requestSender).getOwnerById(ownerId);
        verify(mapper).convertToUserDto(user, ownerDto);
        assertEquals(List.of(userInfoDto), allUsers);
    }

    @Test
    public void testGetUserByUsername() throws Exception{
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        User user = mock(User.class);
        OwnerDto ownerDto = mock(OwnerDto.class);
        UserInfoDto userInfoDto = mock(UserInfoDto.class);
        doReturn(user).when(userRepository).findByUsername(username);
        doReturn(ownerId).when(user).getOwnerId();
        doReturn(ownerDto).when(requestSender).getOwnerById(ownerId);
        doReturn(userInfoDto).when(mapper).convertToUserDto(user, ownerDto);

        // when
        UserInfoDto receivedUserInfoDto = underTest.getUserByUsername(username);

        // then
        verify(userRepository).findByUsername(username);
        verify(requestSender).getOwnerById(ownerId);
        assertEquals(userInfoDto, receivedUserInfoDto);
    }

    @Test
    public void testGetUserByUsernameUserExistenceException(){
        // given
        String username = "alkafol";
        doReturn(null).when(userRepository).findByUsername(username);

        // when
        // then
        assertThrows(UserExistenceException.class, () -> underTest.getUserByUsername(username));
    }
}
