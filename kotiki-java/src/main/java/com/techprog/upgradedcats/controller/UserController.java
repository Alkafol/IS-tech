package com.techprog.upgradedcats.controller;

import com.techprog.upgradedcats.dto.UserDto;
import com.techprog.upgradedcats.service.UserService;
import com.techprog.upgradedcats.tools.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('owner:add')")
    public UserDto addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }

    @DeleteMapping("/deleteUser/{username}")
    @PreAuthorize("hasAuthority('owner:delete')")
    public boolean deleteUser(@PathVariable String username){
        try {
            userService.deleteUser(username);
            return true;
        }
        catch (UserExistenceException e){
            System.out.println("User doesn't exist");
            return false;
        }
        catch (OwnerAccessibilityException | OwnerExistenceException | NonexistentFriendshipException |
                CatExistenceException e){
            System.out.println("Unknown error");
            return false;
        }
    }

}
