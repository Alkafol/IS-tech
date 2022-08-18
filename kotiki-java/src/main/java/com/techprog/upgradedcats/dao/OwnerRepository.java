package com.techprog.upgradedcats.dao;

import com.techprog.upgradedcats.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String>{

    @Query("SELECT u FROM Owner u WHERE u.user.username = ?1")
    Owner findByUsername(String username);
}

