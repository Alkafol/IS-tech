package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dao.CatDAO;
import com.techprog.upgradedcats.dao.OwnerDAO;
import com.techprog.upgradedcats.dto.CatDto;
import com.techprog.upgradedcats.dto.OwnerDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.tools.CatExistenceException;
import com.techprog.upgradedcats.tools.OwnerExistenceException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatsService {

    private final CatDAO _catDAO;
    private final OwnerDAO _ownerDAO;

    public CatsService(CatDAO _catDAO, OwnerDAO _ownerDAO) {
        this._catDAO = _catDAO;
        this._ownerDAO = _ownerDAO;
    }

    public Cat addCat(CatDto catDto) throws OwnerExistenceException {
        Cat cat = this.convertToCat(catDto);
        _catDAO.save(cat);
        return cat;
    }

    public CatDto getCatById(String id){
        return this.convertToCatDto(_catDAO.findById(id).get());
    }

    public Owner addOwner(OwnerDto ownerDto){
        Owner owner = this.convertToOwner(ownerDto);
        _ownerDAO.save(owner);
        return owner;
    }

    public OwnerDto getOwnerById(String id){
        return this.convertToOwnerDto(_ownerDAO.findById(id).get());
    }

    public void deleteOwnerById(String id) throws OwnerExistenceException {
        _ownerDAO.deleteById(id);
    }

    public void deleteCatById(String id) throws CatExistenceException {
        Owner owner = _ownerDAO.getById(_catDAO.getById(id).getOwner().getId());
        if (_catDAO.getById(id).getFriends().size() != 0){
            for (int i = 0; i < _catDAO.getById(id).getFriends().size(); i++){
                _catDAO.getById(id).getFriends().stream().toList().get(i).removeFriend(_catDAO.getById(id));
                _catDAO.save(_catDAO.getById(id).getFriends().stream().toList().get(i));
            }
            _catDAO.getById(id).removeAllFriends();
        }
        owner.removeCat(_catDAO.getById(id));
        _catDAO.save(_catDAO.getById(id));
        _ownerDAO.save(owner);
        // _catDAO.deleteById(id);
    }

    public void startFriendship(String firstCatId, String secondCatId) throws CatExistenceException {

        Cat firstCat = _catDAO.getById(firstCatId);
        Cat secondCat = _catDAO.getById(secondCatId);

        if (firstCat == null || secondCat == null){
            throw new CatExistenceException();
        }

        firstCat.addFriend(secondCat);
        secondCat.addFriend(firstCat);

        _catDAO.save(firstCat);
        _catDAO.save(secondCat);
    }

    public void stopFriendship(String firstCatId, String secondCatId) throws CatExistenceException {
        Cat firstCat = _catDAO.getById(firstCatId);
        Cat secondCat = _catDAO.getById(secondCatId);

        if (firstCat == null || secondCat == null){
            throw new CatExistenceException();
        }

        firstCat.removeFriend(secondCat);
        secondCat.removeFriend(firstCat);

        _catDAO.save(firstCat);
        _catDAO.save(secondCat);
    }

    public OwnerDto getOwnerOfCat(String catId) throws CatExistenceException {

        Cat cat = _catDAO.getById(catId);
        if (cat == null){
            throw new CatExistenceException();
        }

        return this.convertToOwnerDto(cat.getOwner());
    }

    public Set<CatDto> getAllCatsOfOwner(String ownerId) throws OwnerExistenceException {

        Owner owner = _ownerDAO.getById(ownerId);
        if (owner == null){
            throw new OwnerExistenceException();
        }

        return owner.getCats().stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toSet());
    }

    public List<CatDto> getAllCats(){
        return _catDAO.findAll().stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
    }

    public List<OwnerDto> getAllOwners(){
        return _ownerDAO.findAll().stream().map(owner -> convertToOwnerDto(owner)).collect(Collectors.toList());
    }

    public Set<CatDto> getAllCatFriends(String catId){
        Set<CatDto> allFriends = _catDAO.getById(catId).getFriends().stream().map(friend -> convertToCatDto(friend)).collect(Collectors.toSet());
        return allFriends;
    }

    public List<CatDto> getColored(Color color)
    {
        List<Cat> cats = _catDAO.findAll().stream().filter(c -> c.getColor().equals(color)).collect(Collectors.toList());
        List<CatDto> cats_dto = cats.stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
        return cats_dto;
    }

    public OwnerDto convertToOwnerDto(Owner owner){
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(owner.getName());
        ownerDto.setId(owner.getId());
        ownerDto.setDateOfBirth(owner.getDateOfBirth());
        ownerDto.setCatsId(owner.getCats().stream().map(cat -> cat.getId()).collect(Collectors.toSet()));
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
            cats = ownerDto.getCatsId().stream().map(catId -> _catDAO.findById(catId).get()).collect(Collectors.toSet());
        }
        Owner owner = Owner.createCustomOwner(name, id, dateOfBirth, cats);
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
        Owner owner = _ownerDAO.findById(catDto.getOwnerId()).get();
        Calendar dateOfBirth = catDto.getDateOfBirth();
        Set<Cat> friends = new HashSet<>();
        if (!catDto.getFriendsId().isEmpty()) {
            friends = catDto.getFriendsId().stream().map(friendId -> _catDAO.findById(friendId).get()).collect(Collectors.toSet());
        }
        Cat cat = Cat.createCustomCat(name, id, dateOfBirth, breed, color, owner, friends);
        return cat;
    }
}

