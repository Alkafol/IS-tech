package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dto.Mapper;
import com.techprog.upgradedcats.repository.CatRepository;
import com.techprog.upgradedcats.dto.CatDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.tools.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.techprog.upgradedcats.security.ApplicationUserRole.ADMIN;
import static com.techprog.upgradedcats.security.ApplicationUserRole.OWNER;

// service should convert DTO
// mapper is best practice for DTO-converting (instead of private methods in service)

@Service
public class CatService {
    private final UserService userService;
    private final OwnerService ownerService;
    private final CatRepository catRepository;
    private final Mapper mapper;


    public CatService(@Lazy UserService userService, CatRepository catRepository, Mapper mapper, @Lazy OwnerService ownerService) {
        this.userService = userService;
        this.ownerService = ownerService;
        this.catRepository = catRepository;
        this.mapper = mapper;
    }

    // when admin add cat he sets owner's id manually (or he becomes owner), when owner adds cat id is set automatically
    public CatDto addCat(CatDto catDto, String username) throws OwnerExistenceException, OwnerAccessibilityException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == OWNER) {
            Cat cat = mapper.convertToCat(catDto);
            if (cat.getOwner() == null){
                cat.setOwner(user.getOwner());
            }
            else {
                throw new OwnerAccessibilityException();
            }

            Cat savedCat = catRepository.save(cat);
            return mapper.convertToCatDto(savedCat);
        }

        else if (user.getRole() == ADMIN) {
            if (catDto.getOwnerId() != null && ownerService.getOwnerById(catDto.getOwnerId()) == null){
                throw new OwnerExistenceException();
            }
            Cat cat = mapper.convertToCat(catDto);
            if (cat.getOwner() == null) {
                cat = Cat.createCustomCat(cat.getName(), cat.getId(), cat.getDateOfBirth(), cat.getBreed(), cat.getColor(),
                        user.getOwner(), cat.getFriends());
            }
            Cat savedCat = catRepository.save(cat);
            return mapper.convertToCatDto(savedCat);
        }

        // never gets here
        return null;
    }

    // admin cat get any cat he likes, whereas owner can get only cats he owns
    public CatDto getCatById(String id, String username) throws CatOwnershipException, CatExistenceException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == OWNER) {
            Cat foundedCat = catRepository.findById(id).orElseThrow(CatOwnershipException::new);

            if (foundedCat.getOwner() != user.getOwner()) {
                throw new CatOwnershipException();
            }

            return mapper.convertToCatDto(foundedCat);
        }
        else if (user.getRole() == ADMIN){
            return mapper.convertToCatDto(catRepository.findById(id).orElseThrow(CatExistenceException::new));
        }

        // never gets here
        return null;
    }

    // admin can delete any cat, whereas owner can delete only his cats
    // when deleting cat we also delete all his friendship
    public void deleteCatById(String id, String username) throws CatExistenceException, OwnerAccessibilityException,
            NonexistentFriendshipException {

        User user = userService.loadUserByUsername(username);
        Cat cat = catRepository.findById(id).orElseThrow(CatExistenceException::new);

        if (user.getRole() == OWNER && user.getOwner() != cat.getOwner()){
            throw new OwnerAccessibilityException();
        }

        List<Cat> friends = getAllCatFriends(id, username).stream().map(mapper::convertToCat).toList();
        for (Cat currentFriend : friends){
            stopFriendship(id, currentFriend.getId(), username);
        }

        cat.setOwner(null);
        user.getOwner().removeCat(cat);

        catRepository.delete(cat);
    }

    public void startFriendship(String firstCatId, String secondCatId, String username) throws CatExistenceException, ExistedFriendshipException, OwnerAccessibilityException {
        User user = userService.loadUserByUsername(username);

        Cat firstCat = catRepository.findById(firstCatId).orElseThrow(CatExistenceException::new);
        Cat secondCat = catRepository.findById(secondCatId).orElseThrow(CatExistenceException::new);

        if (user.getRole() == OWNER && firstCat.getOwner() != user.getOwner() && secondCat.getOwner() != user.getOwner()){
            throw new OwnerAccessibilityException();
        }

        if (firstCat.getFriends().contains(secondCat) || secondCat.getFriends().contains(firstCat)){
            throw new ExistedFriendshipException();
        }

        firstCat.addFriend(secondCat);
        catRepository.save(firstCat);
    }

    public void stopFriendship(String firstCatId, String secondCatId, String username) throws CatExistenceException, NonexistentFriendshipException, OwnerAccessibilityException {
        User user = userService.loadUserByUsername(username);

        Cat firstCat = catRepository.findById(firstCatId).orElseThrow(CatExistenceException::new);
        Cat secondCat = catRepository.findById(secondCatId).orElseThrow(CatExistenceException::new);

        if (user.getRole() == OWNER && firstCat.getOwner() != user.getOwner() && secondCat.getOwner() != user.getOwner()){
            throw new OwnerAccessibilityException();
        }

        if (firstCat.getFriends().contains(secondCat)){
            firstCat.removeFriend(secondCat);
            catRepository.save(firstCat);
        }
        else if (secondCat.getFriends().contains(firstCat)){
            secondCat.removeFriend(firstCat);
            catRepository.save(secondCat);
        }
        else{
            throw new NonexistentFriendshipException();
        }
    }

    // admin get all cats, owner get only his cats
    public List<CatDto> getAllCats(String username) {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == OWNER) {
            Set<Cat> cats = user.getOwner().getCats();
            return cats.stream().map(mapper::convertToCatDto).collect(Collectors.toList());
        } else if (user.getRole()== ADMIN) {
            return catRepository.findAll().stream().map(mapper::convertToCatDto).collect(Collectors.toList());
        }

        // never gets here
        return null;
    }

    public List<CatDto> getAllCatFriends(String catId, String username) throws CatExistenceException, OwnerAccessibilityException {
        User user = userService.loadUserByUsername(username);
        if (user.getRole() == OWNER && catRepository.findById(catId).orElseThrow(CatExistenceException::new).getOwner() != user.getOwner()){
            throw new OwnerAccessibilityException();
        }

        List<String> friendsId = catRepository.findFriendsFromSecondFriendshipColumn(catId);
        friendsId.addAll(catRepository.findFriendsFromFirstFriendshipColumn(catId));

        List<Cat> cats = friendsId.stream().map(id -> catRepository.findById(id).orElseThrow()).toList();
        return cats.stream().map(mapper::convertToCatDto).toList();
    }

    public List<CatDto> getColored(Color color, String username) {
        User user = userService.loadUserByUsername(username);
        List<Cat> coloredCats = new ArrayList<>();

        if (user.getRole() == OWNER){
            coloredCats = catRepository.findCatsByColorWithSpecifiedOwner(color, user.getOwner());
        }
        else if (user.getRole() == ADMIN){
            coloredCats = catRepository.findAllCatsByColor(color);
        }

        return coloredCats.stream().map(mapper::convertToCatDto).toList();
    }
}

