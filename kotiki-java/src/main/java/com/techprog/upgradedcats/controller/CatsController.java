package com.techprog.upgradedcats.controller;

import com.techprog.upgradedcats.dto.CatDto;
import com.techprog.upgradedcats.dto.OwnerDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.service.CatsService;
import com.techprog.upgradedcats.tools.CatExistenceException;
import com.techprog.upgradedcats.tools.OwnerExistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/cats")
public class CatsController {
    private CatsService catsService;

    public CatsController(CatsService newCatsService){
        catsService = newCatsService;
    }

    @GetMapping("/cat")
    public List<CatDto> getAllCats(){
        return catsService.getAllCats();
    }

    @GetMapping("/cat/{id}")
    public CatDto getCatById(@PathVariable String id){
        return catsService.getCatById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cat")
    public Cat addCat(@RequestBody CatDto cat){
        try {
            return catsService.addCat(cat);
        }
        catch (OwnerExistenceException e){
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cat/{id}")
    public void deleteCatById(@PathVariable String id){
        try {
            catsService.deleteCatById(id);
        }
        catch (CatExistenceException e){
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/owner")
    public Owner addOwner(@RequestBody OwnerDto owner){
        return catsService.addOwner(owner);
    }

    @GetMapping("/owner/{id}")
    public OwnerDto getOwnerById(@PathVariable String id){
        return catsService.getOwnerById(id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/owner/{id}")
    public void deleteOwnerById(@PathVariable String id){
        try {
            catsService.deleteOwnerById(id);
        }
        catch (OwnerExistenceException e){
        }
    }

    @GetMapping("/owner")
    public List<OwnerDto> getAllOwners(){
        return catsService.getAllOwners();
    }

    @PutMapping("/cat/startFriendship/{firstCatId}/{secondCatId}")
    public void startFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId){
        try {
            catsService.startFriendship(firstCatId, secondCatId);
        }
        catch (CatExistenceException e){
        }
    }

    @PutMapping("/cat/stopFriendship/{firstCatId}/{secondCatId}")
    public void stopFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId){
        try {
            catsService.stopFriendship(firstCatId, secondCatId);
        }
        catch (CatExistenceException e){
        }
    }

    @GetMapping("/cat/getColored/{color}")
    public List<CatDto> getAllColoredCat(@PathVariable Color color){
        return catsService.getColored(color);
    }




    public Set<CatDto> getAllCatFriends(String catId){
        return catsService.getAllCatFriends(catId);
    }
}
