package com.techprog.catsmicroservice.service;

import com.techprog.catsmicroservice.dto.CatDto;
import com.techprog.catsmicroservice.dto.Mapper;
import com.techprog.catsmicroservice.repository.CatRepository;
import com.techprog.catsmicroservice.tools.*;
import com.techprog.entities.cat.Cat;
import com.techprog.entities.cat.Color;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CatService {
    private final CatRepository catRepository;
    private final Mapper mapper;

    public CatService(CatRepository catRepository, Mapper mapper) {
        this.catRepository = catRepository;
        this.mapper = mapper;
    }

    public CatDto addCat(CatDto catDto){
        Cat cat = mapper.convertToCat(catDto);
        Cat savedCat = catRepository.save(cat);
        return mapper.convertToCatDto(savedCat);
    }

    public CatDto getCatById(Integer id) throws CatExistenceException{
        Cat foundedCat = catRepository.findById(id).orElseThrow(
                () -> new CatExistenceException("Cat with id " + id + " doesn't exist")
        );
        foundedCat.addFriends(getAllCatFriends(id));
        return mapper.convertToCatDto(foundedCat);
    }

    public void deleteCatById(Integer id) throws CatExistenceException, NonexistentFriendshipException {

        Cat cat = catRepository.findById(id).orElseThrow(
                () -> new CatExistenceException("Cat with id " + id + " doesn't exist")
        );
        List<Cat> friends = getAllCatFriends(id);

        for (Cat currentFriend : friends) {
            stopFriendship(id, currentFriend.getId());
        }

        catRepository.delete(cat);
    }

    public void startFriendship(Integer firstCatId, Integer secondCatId) throws CatExistenceException, ExistedFriendshipException{
        Cat firstCat = catRepository.findById(firstCatId).orElseThrow(
                () -> new CatExistenceException("Cat with id " + firstCatId + " doesn't exist")
        );
        Cat secondCat = catRepository.findById(secondCatId).orElseThrow(
                () -> new CatExistenceException("Cat with id " + secondCatId + " doesn't exist")
        );

        if (firstCat.getFriends().contains(secondCat) || secondCat.getFriends().contains(firstCat)) {
            throw new ExistedFriendshipException("Cats with id " + firstCatId + " and " + secondCatId + " are already friends");
        }

        firstCat.addFriends(List.of(secondCat));
        catRepository.save(firstCat);
    }

    public void stopFriendship(Integer firstCatId, Integer secondCatId) throws CatExistenceException, NonexistentFriendshipException {
        Cat firstCat = catRepository.findById(firstCatId).orElseThrow(
                () -> new CatExistenceException("Cat with id " + firstCatId + " doesn't exist")
        );
        Cat secondCat = catRepository.findById(secondCatId).orElseThrow(
                () -> new CatExistenceException("Cat with id " + secondCatId + " doesn't exist")
        );

        if (firstCat.getFriends().contains(secondCat)) {
            firstCat.removeFriendById(secondCatId);
            catRepository.save(firstCat);
        } else if (secondCat.getFriends().contains(firstCat)) {
            secondCat.removeFriendById(firstCatId);
            catRepository.save(secondCat);
        } else {
            throw new NonexistentFriendshipException("Cats with id " + firstCatId + " and " + secondCatId + " aren't friends");
        }
    }

    public List<CatDto> getAllCats() {
        List<Cat> cats = catRepository.findAll();
        for (Cat cat : cats){
            cat.addFriends(getAllCatFriends(cat.getId()));
        }

        return cats.stream().map(mapper::convertToCatDto).toList();
    }

    public List<CatDto> getColored(Color color) {
        List<Cat> coloredCats = catRepository.findAllCatsByColor(color);
        return coloredCats.stream().map(mapper::convertToCatDto).toList();
    }

    public List<CatDto> getColoredWithSpecifiedOwner(Color color, Integer ownerId){
        List<Cat> coloredCats = catRepository.findCatsByColorWithSpecifiedOwner(color, ownerId);
        return coloredCats.stream().map(mapper::convertToCatDto).toList();
    }

    private List<Cat> getAllCatFriends(Integer catId){
        List<Integer> friendsId = catRepository.findFriendsFromSecondFriendshipColumn(catId);
        friendsId.addAll(catRepository.findFriendsFromFirstFriendshipColumn(catId));

        return friendsId.stream().map(id -> catRepository.findById(id).get()).toList();
    }

}

