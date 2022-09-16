package com.techprog.upgradedcats.repository;

import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat, String> {

    @Query(value = "SELECT first_cat FROM friendship WHERE second_cat LIKE ?1",
            nativeQuery = true)
    List<String> findFriendsFromFirstFriendshipColumn(@Param("catId") String catId);

    @Query(value = "SELECT second_cat FROM friendship WHERE first_cat LIKE ?1",
            nativeQuery = true)
    List<String> findFriendsFromSecondFriendshipColumn(@Param("catId") String catId);

    @Query("SELECT cat FROM Cat cat WHERE cat.color = ?1")
    List<Cat> findAllCatsByColor(@Param("color") Color color);

    @Query("SELECT cat FROM Cat cat WHERE cat.color = ?1 AND cat.owner = ?2")
    List<Cat> findCatsByColorWithSpecifiedOwner(@Param("color") Color color, @Param("owner") Owner owner);
}
