package Controller;

import Models.Cat;
import Models.Color;
import Models.Owner;
import Service.CatsService;
import Service.OwnersService;
import Tools.CatExistenceException;
import Tools.FriendshipException;
import Tools.OwnerExistenceException;

import java.util.*;

public class CatsController {
    private CatsService catsService;
    private OwnersService ownersService;

    public CatsController(CatsService newCatsService, OwnersService newOwnersService){
        ownersService = newOwnersService;
        catsService = newCatsService;
    }

    public String addCat(String name, Calendar dateOfBirth, String breed, Color color, Owner owner){
        return catsService.addCat(name, dateOfBirth, breed, color, owner);
    }

    public Cat getCatById(String id){
        return catsService.getCatById(id);
    }

    public Owner addOwner(String name, Calendar dateOfBirth){
        return ownersService.addOwner(name, dateOfBirth);
    }

    public Owner getOwnerById(String id){
        return ownersService.getOwnerById(id);
    }

    public void deleteOwnerById(String id){
        try {
            ownersService.deleteOwnerById(id);
        }
        catch (OwnerExistenceException e){
            System.out.println("Owner with id " + id + " doesn't exist");
        }
    }

    public void deleteCatById(String id){
        try {
            catsService.deleteCatById(id);
        } catch (CatExistenceException e){
            System.out.println("Cat with id " + id + " doesn't exist");
        }
    }

    public void startFriendship(String firstCatId, String secondCatId){
        try {
            catsService.startFriendship(firstCatId, secondCatId);
        }
        catch (FriendshipException e){
            System.out.println("Can't start friendship because cat doesn't exist");
        }
    }

    public void stopFriendship(String firstCatId, String secondCatId){
        try {
            catsService.stopFriendship(firstCatId, secondCatId);
        }
        catch (FriendshipException e){
            System.out.println("Can't stop friendship because cat doesn't exist");
        }
    }

    public Owner getOwnerOfCat(String catId){
        try {
            return catsService.getOwnerOfCat(catId);
        }
        catch (CatExistenceException e){
            System.out.println("Cat with id " + catId + " doesn't exist");
            return null;
        }
    }

    public Set<Cat> getAllCatsOfOwner(String ownerId){
        try {
            return ownersService.getAllCatsOfOwner(ownerId);
        }
        catch (OwnerExistenceException e){
            System.out.println("Owner with id " + ownerId + " doesn't exist");
            return Collections.emptySet();
        }
    }

    public List<Cat> getAllCats(){
        return catsService.getAllCats();
    }

    public List<Owner> getAllOwners(){
        return ownersService.getAllOwners();
    }

    public Set<Cat> getAllCatFriends(String catId){
        try {
            return catsService.getAllCatFriends(catId);
        }
        catch (CatExistenceException e){
            System.out.println("Cat with id " + catId + " doesn't exist");
            return Collections.emptySet();
        }
    }
}
