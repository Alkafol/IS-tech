package com.techprog.upgradedcats.dao;

import com.techprog.upgradedcats.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerDAO extends JpaRepository<Owner, String>{}

