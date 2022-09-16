package com.techprog.upgradedcats.controller;

import com.techprog.upgradedcats.dto.CatDto;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.service.CatService;
import com.techprog.upgradedcats.tools.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cats")
public class CatsController {
    private final CatService catService;

    public CatsController(CatService catService){
        this.catService = catService;
    }

    @GetMapping("/cat")
    @PreAuthorize("hasAuthority('cat:get_all')")
    public List<CatDto> getAllCats(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return catService.getAllCats(username);
    }

    @GetMapping("/cat/{id}")
    @PreAuthorize("hasAuthority('cat:get_by_id')")
    public CatDto getCatById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return catService.getCatById(id, username);
        }
        catch (CatOwnershipException e){
            System.out.println("Logged user doesn't have access to this cat");
            return null;
        }
        catch (CatExistenceException e){
            System.out.println("No cat with such ID");
            return null;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cat")
    @PreAuthorize("hasAuthority('cat:add')")
    public CatDto addCat(@RequestBody CatDto cat){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return catService.addCat(cat, username);
        }
        catch (OwnerExistenceException e){
            System.out.println("Owner doesn't exist");
        }
        catch (OwnerAccessibilityException e){
            System.out.println("You don't have access to create cat to this owner");
        }
        return null;
    }

    @DeleteMapping("/cat/{id}")
    @PreAuthorize("hasAuthority('cat:delete')")
    public boolean deleteCatById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            catService.deleteCatById(id, username);
            return true;
        }
        catch (CatExistenceException e){
            System.out.println("Cat doesn't exist");
            return false;
        }
        catch (OwnerAccessibilityException e){
            System.out.println("Logged user doesn't have permission to delete this cat");
            return false;
        }
        // this exception logically cannot be called in this method
        catch (NonexistentFriendshipException e){
            System.out.println("Unknown error");
            return false;
        }
    }

    @PutMapping("/cat/startFriendship/{firstCatId}/{secondCatId}")
    @PreAuthorize("hasAuthority('cat:start_friendship')")
    public boolean startFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            catService.startFriendship(firstCatId, secondCatId, username);
            return true;
        }
        catch (CatExistenceException e){
            System.out.println("Cat doesn't exist");
            return false;
        }
        catch (ExistedFriendshipException e){
            System.out.println("Cats are already friends");
            return false;
        }
        catch (OwnerAccessibilityException e){
            System.out.println("You don't have access to this cats");
            return false;
        }
    }

    @PutMapping("/cat/stopFriendship/{firstCatId}/{secondCatId}")
    @PreAuthorize("hasAuthority('cat:stop_friendship')")
    public boolean stopFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            catService.stopFriendship(firstCatId, secondCatId, username);
            return true;
        }
        catch (CatExistenceException e){
            System.out.println("Cat doesn't exist");
            return false;
        }
        catch (NonexistentFriendshipException e) {
            System.out.println("This cats are not friends");
            return false;
        }
        catch (OwnerAccessibilityException e){
            System.out.println("You don't have access to this cats");
            return false;
        }
    }

    @GetMapping("/cat/getColored/{color}")
    @PreAuthorize("hasAuthority('cat:get_by_color')")
    public List<CatDto> getAllColoredCat(@PathVariable Color color){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return catService.getColored(color, username);
    }

    @GetMapping("/cat/getAllFriends/{catId}")
    @PreAuthorize("hasAuthority('cat:get_all_friends')")
    public List<CatDto> getAllFriends(@PathVariable String catId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return catService.getAllCatFriends(catId, username);
        }catch (CatExistenceException e) {
            System.out.println("Cat doesn't exist");
        } catch (OwnerAccessibilityException e){
            System.out.println("You don't have access to this cat");
        }

        return null;
    }
}
