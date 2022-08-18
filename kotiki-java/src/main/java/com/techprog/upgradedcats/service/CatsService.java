package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dao.CatRepository;
import com.techprog.upgradedcats.dao.OwnerRepository;
import com.techprog.upgradedcats.dao.UserRepository;
import com.techprog.upgradedcats.repository.CatDto;
import com.techprog.upgradedcats.repository.OwnerDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.security.ApplicationUserRole;
import com.techprog.upgradedcats.tools.CatExistenceException;
import com.techprog.upgradedcats.tools.CatOwnershipException;
import com.techprog.upgradedcats.tools.OwnerAccessibilityException;
import com.techprog.upgradedcats.tools.OwnerExistenceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.techprog.upgradedcats.security.ApplicationUserRole.ADMIN;
import static com.techprog.upgradedcats.security.ApplicationUserRole.OWNER;

@Service
public class CatsService {

    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CatsService(CatRepository catRepository, OwnerRepository ownerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cat addCat(CatDto catDto, String username) throws OwnerExistenceException, OwnerAccessibilityException {
        if (userRepository.findByUsername(username).getRole() == OWNER){
            if (catDto.getOwnerId() == null) {
                catDto.setOwnerId(ownerRepository.findByUsername(username).getId());
            }
            else{
                throw new OwnerAccessibilityException();
            }
        }
        Cat cat = this.convertToCat(catDto);
        catRepository.save(cat);
        return cat;
    }

    public CatDto getCatById(String id, String username) throws CatOwnershipException {
        if (userRepository.findByUsername(username).getRole() == OWNER){
            if (catRepository.findById(id).get().getOwner() != ownerRepository.findByUsername(username)){
                throw new CatOwnershipException();
            }
        }
        return this.convertToCatDto(catRepository.findById(id).get());
    }

    public Owner addOwner(OwnerDto ownerDto){
        Owner owner = this.convertToOwner(ownerDto);
        ownerRepository.save(owner);
        return owner;
    }

    public OwnerDto getOwnerById(String id){
        return this.convertToOwnerDto(ownerRepository.findById(id).get());
    }

    public void deleteOwnerById(String id) throws OwnerExistenceException {
        ownerRepository.deleteById(id);
    }

    public void deleteCatById(String id, String username) throws CatExistenceException, OwnerAccessibilityException {
        if (userRepository.findByUsername(username).getRole() == OWNER){
            if (catRepository.findById(id).get().getOwner() != ownerRepository.findByUsername(username)){
                throw new OwnerAccessibilityException();
            }
        }
        Owner owner = ownerRepository.getById(catRepository.getById(id).getOwner().getId());
        if (catRepository.getById(id).getFriends().size() != 0){
            for (int i = 0; i < catRepository.getById(id).getFriends().size(); i++){
                catRepository.getById(id).getFriends().stream().toList().get(i).removeFriend(catRepository.getById(id));
                catRepository.save(catRepository.getById(id).getFriends().stream().toList().get(i));
            }
            catRepository.getById(id).removeAllFriends();
        }
        owner.removeCat(catRepository.getById(id));
        catRepository.save(catRepository.getById(id));
        ownerRepository.save(owner);
        // _catDAO.deleteById(id);
    }

    public void startFriendship(String firstCatId, String secondCatId) throws CatExistenceException {

        Cat firstCat = catRepository.getById(firstCatId);
        Cat secondCat = catRepository.getById(secondCatId);

        if (firstCat == null || secondCat == null){
            throw new CatExistenceException();
        }

        firstCat.addFriend(secondCat);
        secondCat.addFriend(firstCat);

        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    public void stopFriendship(String firstCatId, String secondCatId) throws CatExistenceException {
        Cat firstCat = catRepository.getById(firstCatId);
        Cat secondCat = catRepository.getById(secondCatId);

        if (firstCat == null || secondCat == null){
            throw new CatExistenceException();
        }

        firstCat.removeFriend(secondCat);
        secondCat.removeFriend(firstCat);

        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    public OwnerDto getOwnerOfCat(String catId) throws CatExistenceException {

        Cat cat = catRepository.getById(catId);
        if (cat == null){
            throw new CatExistenceException();
        }

        return this.convertToOwnerDto(cat.getOwner());
    }

    public Set<CatDto> getAllCatsOfOwner(String ownerId) throws OwnerExistenceException {

        Owner owner = ownerRepository.getById(ownerId);
        if (owner == null){
            throw new OwnerExistenceException();
        }

        return owner.getCats().stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toSet());
    }

    public List<CatDto> getAllCats(String username){
        if (userRepository.findByUsername(username).getRole() == ADMIN) {
            return catRepository.findAll().stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
        }
        else if (userRepository.findByUsername(username).getRole() == OWNER){
            return ownerRepository.findByUsername(username).getCats().stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
        }
        return null;
    }

    public List<OwnerDto> getAllOwners(){
        return ownerRepository.findAll().stream().map(owner -> convertToOwnerDto(owner)).collect(Collectors.toList());
    }

    public Set<CatDto> getAllCatFriends(String catId){
        Set<CatDto> allFriends = catRepository.getById(catId).getFriends().stream().map(friend -> convertToCatDto(friend)).collect(Collectors.toSet());
        return allFriends;
    }

    public List<CatDto> getColored(Color color, String username)
    {
        List<Cat> cats = Collections.emptyList();
        if (userRepository.findByUsername(username).getRole() == ADMIN) {
            cats = catRepository.findAll().stream().filter(c -> c.getColor().equals(color)).collect(Collectors.toList());
        }
        if (userRepository.findByUsername(username).getRole() == OWNER){
            cats = catRepository.findAll().stream().filter(c -> c.getColor().equals(color)).filter(c -> c.getOwner() == ownerRepository.findByUsername(username)).collect(Collectors.toList());
        }
        List<CatDto> cats_dto = cats.stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
        return cats_dto;
    }

    public OwnerDto convertToOwnerDto(Owner owner){
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(owner.getName());
        ownerDto.setId(owner.getId());
        ownerDto.setDateOfBirth(owner.getDateOfBirth());
        ownerDto.setCatsId(owner.getCats().stream().map(cat -> cat.getId()).collect(Collectors.toSet()));
        ownerDto.setUsername(owner.getUser().getUsername());
        ownerDto.setPassword(owner.getUser().getPassword());
        ownerDto.setRole(owner.getUser().getRole().toString());
        return ownerDto;
    }

    public CatDto convertToCatDto(Cat cat){
        CatDto catDto = new CatDto();
        catDto.setName(cat.getName());
        catDto.setBreed(cat.getBreed());
        catDto.setId(cat.getId());
        catDto.setDateOfBirth(cat.getDateOfBirth());
        if (cat.getOwner() != null) {
            catDto.setOwnerId(cat.getOwner().getId());
        }
        else{
            catDto.setOwnerId(null);
        }
        catDto.setFriendsId(cat.getFriends().stream().map(friend -> friend.getId()).collect(Collectors.toList()));
        catDto.setColor(cat.getColor().toString());
        return catDto;
    }

    public Owner convertToOwner(OwnerDto ownerDto){
        String name = ownerDto.getName();
        Calendar dateOfBirth = ownerDto.getDateOfBirth();
        String id = ownerDto.getId();
        Set<Cat> cats = new HashSet<>();
        if (!ownerDto.getCatsId().isEmpty()) {
            cats = ownerDto.getCatsId().stream().map(catId -> catRepository.findById(catId).get()).collect(Collectors.toSet());
        }
        User user = userRepository.findByUsername(ownerDto.getUsername());
        if (user == null){
            String username = ownerDto.getUsername();
            String password = passwordEncoder.encode(ownerDto.getPassword());
            ApplicationUserRole role = ApplicationUserRole.valueOf(ownerDto.getRole());
            user = new User(username, password, role);
            userRepository.save(user);
        }
        Owner owner = Owner.createCustomOwner(name, id, dateOfBirth, cats, user);
        return owner;
    }

    public Cat convertToCat(CatDto catDto) throws OwnerExistenceException {
        String name = catDto.getName();
        String breed = catDto.getBreed();
        String id = catDto.getId();
        Color color = Color.valueOf(catDto.getColor());
        if (catDto.getOwnerId() == null){
            throw new OwnerExistenceException();
        }
        Owner owner = ownerRepository.findById(catDto.getOwnerId()).get();
        Calendar dateOfBirth = catDto.getDateOfBirth();
        Set<Cat> friends = new HashSet<>();
        if (!catDto.getFriendsId().isEmpty()) {
            friends = catDto.getFriendsId().stream().map(friendId -> catRepository.findById(friendId).get()).collect(Collectors.toSet());
        }
        Cat cat = Cat.createCustomCat(name, id, dateOfBirth, breed, color, owner, friends);
        return cat;
    }
}

