package com.techprog.catsmicroservice.repository;

import com.techprog.entities.cat.Cat;
import com.techprog.entities.cat.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat, Integer> {

    @Query(value = "SELECT first_cat FROM friendship WHERE second_cat = ?1",
            nativeQuery = true)
    List<Integer> findFriendsFromFirstFriendshipColumn(@Param("catId") Integer catId);

    @Query(value = "SELECT second_cat FROM friendship WHERE first_cat = ?1",
            nativeQuery = true)
    List<Integer> findFriendsFromSecondFriendshipColumn(@Param("catId") Integer catId);

    @Query("SELECT cat FROM Cat cat WHERE cat.color = ?1")
    List<Cat> findAllCatsByColor(@Param("color") Color color);

    @Query("SELECT cat FROM Cat cat WHERE cat.color = ?1 AND cat.ownerId = ?2")
    List<Cat> findCatsByColorWithSpecifiedOwner(@Param("color") Color color, @Param("owner") Integer ownerId);
}
