package com.techprog.upgradedcats.dao;

import com.techprog.upgradedcats.models.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatDAO extends JpaRepository<Cat, String> { }
