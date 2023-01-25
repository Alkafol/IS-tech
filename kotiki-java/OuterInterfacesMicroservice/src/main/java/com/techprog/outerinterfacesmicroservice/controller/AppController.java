package com.techprog.outerinterfacesmicroservice.controller;

import com.techprog.entities.cat.Color;
import com.techprog.outerinterfacesmicroservice.dto.*;
import com.techprog.outerinterfacesmicroservice.service.AppService;
import com.techprog.outerinterfacesmicroservice.service.UserService;
import com.techprog.outerinterfacesmicroservice.tools.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AppController {
    private final AppService appService;
    private final UserService userService;

    public AppController(AppService appService, UserService userService) {
        this.appService = appService;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cat")
    @PreAuthorize("hasAuthority('cat:add')")
    public CatDto addCat(@RequestBody CatDto catDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return appService.addCat(catDto, username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('user:add')")
    // only admin method
    public UserInfoDto addUser(@RequestBody UserCreationDto userCreationDto){
        try {
            return userService.addUser(userCreationDto);
        } catch (OwnerMicroserviceException | UserExistenceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cat/get/{id}")
    @PreAuthorize("hasAuthority('cat:get_by_id')")
    public CatDto getCatById(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return appService.getCatById(id, username);
        }
        catch (CatMicroserviceException | AccessibilityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/get/{username}")
    @PreAuthorize("hasAuthority('user:get_by_username')")
    // only admin method
    public UserInfoDto getUserByUsername(@PathVariable String username){
        try {
            return userService.getUserByUsername(username);
        } catch (OwnerMicroserviceException | UserExistenceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/get_all")
    @PreAuthorize("hasAuthority('user:get_all')")
    // only admin method
    public List<UserInfoDto> getAllUsers(){
        try {
            return userService.getAllUsers();
        } catch (OwnerMicroserviceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cat/get_all/")
    @PreAuthorize("hasAuthority('cat:get_all')")
    public List<CatDto> getAllCats(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return appService.getAllCats(username);
        }
        catch (CatMicroserviceException | OwnerMicroserviceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/cat/start_friendship/{firstId}/{secondId}")
    @PreAuthorize("hasAuthority('cat:start_friendship')")
    public void startFriendship(@PathVariable Integer firstId, @PathVariable Integer secondId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            appService.startFriendship(username, firstId, secondId);
        }
        catch (CatMicroserviceException | AccessibilityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/cat/stop_friendship/{firstId}/{secondId}")
    @PreAuthorize("hasAuthority('cat:stop_friendship')")
    public void stopFriendship(@PathVariable Integer firstId, @PathVariable Integer secondId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            appService.stopFriendship(username, firstId, secondId);
        }
        catch (CatMicroserviceException | AccessibilityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/cat/delete/{id}")
    @PreAuthorize("hasAuthority('cat:delete')")
    public void deleteCat(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            appService.deleteCat(username, id);
        }
        catch (CatMicroserviceException | OwnerMicroserviceException | AccessibilityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/user/delete/{username}")
    @PreAuthorize("hasAuthority('user:delete')")
    // only admin method
    public void deleteUser(@PathVariable String username){
        try {
            userService.deleteUser(username);
        } catch (CatMicroserviceException | OwnerMicroserviceException | UserExistenceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cat/get_by_color/{color}")
    @PreAuthorize("hasAuthority('cat:get_by_color')")
    public List<CatDto> getByColor(@PathVariable Color color){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return appService.getByColor(username, color);
        }
        catch (CatMicroserviceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
