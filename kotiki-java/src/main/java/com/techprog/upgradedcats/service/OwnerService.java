package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dto.Mapper;
import com.techprog.upgradedcats.repository.OwnerRepository;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.dto.OwnerDto;
import com.techprog.upgradedcats.tools.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final Mapper mapper;
    private final CatService catService;

    public OwnerService(OwnerRepository ownerRepository, Mapper mapper, CatService catService) {
        this.ownerRepository = ownerRepository;
        this.mapper = mapper;
        this.catService = catService;
    }

    // admin function, checked with PreAuthorize()
    public void addOwner(OwnerDto ownerDto){
        Owner receivedOwner = mapper.convertToOwner(ownerDto);
        Owner createdOwner = ownerRepository.save(receivedOwner);
        mapper.convertToOwnerDto(createdOwner);
    }

    // admin function, checked with PreAuthorize()
    public OwnerDto getOwnerById(String id) throws OwnerExistenceException {
        return mapper.convertToOwnerDto(ownerRepository.findById(id).orElseThrow(OwnerExistenceException::new));
    }

    // admin function, checked with PreAuthorize()
    public void deleteOwnerById(String id, String username) throws OwnerExistenceException, OwnerAccessibilityException,
            NonexistentFriendshipException, CatExistenceException {
        Owner owner = ownerRepository.getById(id);

        for (Cat cat : owner.getCats()){
            catService.deleteCatById(cat.getId(), username);
        }

        ownerRepository.deleteById(id);
    }

    // admin function, checked with PreAuthorize()
    public List<OwnerDto> getAllOwners() {
        return ownerRepository.findAll().stream().map(mapper::convertToOwnerDto).collect(Collectors.toList());
    }
}
