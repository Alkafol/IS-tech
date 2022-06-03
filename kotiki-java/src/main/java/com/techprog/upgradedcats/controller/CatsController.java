package com.techprog.upgradedcats.controller;

import com.techprog.upgradedcats.repository.CatDto;
import com.techprog.upgradedcats.repository.OwnerDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.service.CatsService;
import com.techprog.upgradedcats.tools.CatExistenceException;
import com.techprog.upgradedcats.tools.CatOwnershipException;
import com.techprog.upgradedcats.tools.OwnerAccessibilityException;
import com.techprog.upgradedcats.tools.OwnerExistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cats")
public class CatsController {
    private final CatsService catsService;

    public CatsController(CatsService newCatsService){
        catsService = newCatsService;
    }

    @GetMapping("/cat")
    @PreAuthorize("hasAuthority('cat:get_all')")
    public List<CatDto> getAllCats(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return catsService.getAllCats(username);
    }

    @GetMapping("/cat/{id}")
    @PreAuthorize("hasAuthority('cat:get_by_id')")
    public CatDto getCatById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return catsService.getCatById(id, username);
        }
        catch (CatOwnershipException e){
            System.out.println("Logged user doesn't have access to this cat");
            return null;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cat")
    @PreAuthorize("hasAuthority('cat:add')")
    public Cat addCat(@RequestBody CatDto cat){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            return catsService.addCat(cat, username);
        }
        catch (OwnerExistenceException e){
            System.out.println("Owner doesn't exist");
        }
        catch (OwnerAccessibilityException e){
            System.out.println("Logged user doesn't have permission to define owner id");
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cat/{id}")
    @PreAuthorize("hasAuthority('cat:delete')")
    public void deleteCatById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            catsService.deleteCatById(id, username);
        }
        catch (CatExistenceException e){
            System.out.println("Cat doesn't exist");
        }
        catch (OwnerAccessibilityException e){
            System.out.println("Logged user doesn't have permission to delete this cat");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/owner")
    @PreAuthorize("hasAuthority('owner:add')")
    public Owner addOwner(@RequestBody OwnerDto owner){
        return catsService.addOwner(owner);
    }

    @GetMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('owner:get_by_id')")
    public OwnerDto getOwnerById(@PathVariable String id){
        return catsService.getOwnerById(id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('owner:delete')")
    public void deleteOwnerById(@PathVariable String id){
        try {
            catsService.deleteOwnerById(id);
        }
        catch (OwnerExistenceException e){
        }
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAuthority('owner:get_all')")
    public List<OwnerDto> getAllOwners(){
        return catsService.getAllOwners();
    }

    @PutMapping("/cat/startFriendship/{firstCatId}/{secondCatId}")
    @PreAuthorize("hasAuthority('cat:start_friendship')")
    public void startFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId){
        try {
            catsService.startFriendship(firstCatId, secondCatId);
        }
        catch (CatExistenceException e){
        }
    }

    @PutMapping("/cat/stopFriendship/{firstCatId}/{secondCatId}")
    @PreAuthorize("hasAuthority('cat:stop_friendship')")
    public void stopFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId){
        try {
            catsService.stopFriendship(firstCatId, secondCatId);
        }
        catch (CatExistenceException e){
        }
    }

    @GetMapping("/cat/getColored/{color}")
    @PreAuthorize("hasAuthority('cat:get_by_color')")
    public List<CatDto> getAllColoredCat(@PathVariable Color color){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return catsService.getColored(color, username);
    }
}
