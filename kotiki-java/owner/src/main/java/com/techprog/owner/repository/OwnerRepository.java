package com.techprog.owner.repository;

import com.techprog.owner.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String>{

    @Query("SELECT u FROM Owner u WHERE u.user.username = ?1")
    Owner findByUsername(String username);
}

