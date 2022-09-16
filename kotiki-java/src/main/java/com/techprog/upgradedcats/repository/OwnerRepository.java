package com.techprog.upgradedcats.repository;

import com.techprog.upgradedcats.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String>{
}

