package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dto.Mapper;
import com.techprog.upgradedcats.dto.UserDto;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.repository.UserRepository;
import com.techprog.upgradedcats.security.ApplicationUserRole;
import com.techprog.upgradedcats.security.PasswordConfig;
import com.techprog.upgradedcats.tools.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    OwnerService ownerService;
    @Mock
    Mapper mapper;
    @Mock
    PasswordConfig passwordConfig;
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService underTest;

    @BeforeEach
    void SetUp(){
        underTest = new UserService(userRepository, mapper, ownerService, passwordConfig);
    }

    @Test
    void loadUserByUsername() {
        // given
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, new Owner());
        when(userRepository.findByUsername("alkafol")).thenReturn(user);

        // when
        underTest.loadUserByUsername("alkafol");

        // then
        verify(userRepository).findByUsername("alkafol");
    }

    @Test
    void addUser() {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 10, 15));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        UserDto userDto = new UserDto("alkafol", "1234", "owner", "Roman", owner.getId(),
                "2000-10-15", null);
        when(mapper.convertToUser(userDto)).thenReturn(user);
        when(passwordConfig.passwordEncoder()).thenReturn(bCryptPasswordEncoder);
        when(bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("123456");

        // when
        underTest.addUser(userDto);

        // then
        verify(passwordConfig.passwordEncoder()).encode("1234");
        userRepository.save(user);
        assertEquals("123456", user.getPassword());
    }

    @Test
    void deleteUser() throws OwnerAccessibilityException, OwnerExistenceException, NonexistentFriendshipException,
            UserExistenceException, CatExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 12, 29));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        when(userRepository.findByUsername("alkafol")).thenReturn(user);

        // when
        underTest.deleteUser("alkafol");

        // then
        verify(ownerService).deleteOwnerById(owner.getId(), "alkafol");
        verify(userRepository).delete(user);

    }
}