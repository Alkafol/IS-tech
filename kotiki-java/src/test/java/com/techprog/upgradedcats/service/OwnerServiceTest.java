package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dto.Mapper;
import com.techprog.upgradedcats.dto.OwnerDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.repository.OwnerRepository;
import com.techprog.upgradedcats.tools.CatExistenceException;
import com.techprog.upgradedcats.tools.NonexistentFriendshipException;
import com.techprog.upgradedcats.tools.OwnerAccessibilityException;
import com.techprog.upgradedcats.tools.OwnerExistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private CatService catService;
    private OwnerService underTest;

    @BeforeEach
    void SetUp(){
        underTest = new OwnerService(ownerRepository, mapper, catService);
    }

    @Test
    void addOwner() {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 10, 16));
        OwnerDto ownerDto = new OwnerDto(owner.getId(), "Roman", "2000-10-16", new ArrayList<>());
        when(mapper.convertToOwner(ownerDto)).thenReturn(owner);

        // when
        underTest.addOwner(ownerDto);

        // then
        verify(ownerRepository).save(owner);
    }

    @Test
    void getOwnerById() throws OwnerExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2002, 10, 10));
        when(ownerRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        // when
        underTest.getOwnerById(owner.getId());

        // then
        verify(ownerRepository).findById(owner.getId());
    }

    @Test
    void deleteOwnerById() throws OwnerAccessibilityException, OwnerExistenceException, NonexistentFriendshipException, CatExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2002, 12, 11));
        Cat cat = new Cat("Barsik", LocalDate.of(2010, 9, 15), "british", Color.black,
                owner);
        owner.addCat(cat);
        when(ownerRepository.getById(owner.getId())).thenReturn(owner);

        // when
        underTest.deleteOwnerById(owner.getId(), "alkafol");

        // then
        verify(catService).deleteCatById(cat.getId(), "alkafol");
        verify(ownerRepository).deleteById(owner.getId());
    }

    @Test
    void getAllOwners() {
        // given
        // when
        underTest.getAllOwners();

        // then
        verify(ownerRepository).findAll();
    }
}