package com.techprog.ownersmicroservice.service;

import com.techprog.entities.owner.Owner;
import com.techprog.ownersmicroservice.dto.Mapper;
import com.techprog.ownersmicroservice.dto.OwnerDto;
import com.techprog.ownersmicroservice.repository.OwnerRepository;
import com.techprog.ownersmicroservice.tools.OwnerExistenceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final Mapper mapper;

    public OwnerService(OwnerRepository ownerRepository, Mapper mapper) {
        this.ownerRepository = ownerRepository;
        this.mapper = mapper;
    }

    public OwnerDto addOwner(OwnerDto ownerDto) {
        Owner receivedOwner = mapper.convertToOwner(ownerDto);
        Owner createdOwner = ownerRepository.save(receivedOwner);
        return mapper.convertToOwnerDto(createdOwner);
    }

    public OwnerDto getOwnerById(Integer id) throws OwnerExistenceException {
        return mapper.convertToOwnerDto(ownerRepository.findById(id).orElseThrow(
                () -> new OwnerExistenceException("Owner doesn't exist"))
        );
    }

    public void deleteOwnerById(Integer id) throws OwnerExistenceException {
        Owner owner = ownerRepository.findById(id).orElseThrow(
                () -> new OwnerExistenceException("Owner doesn't exist")
        );

        for (Integer catId : owner.getCatsId()) {
            deleteOwnership(catId, id);
        }

        ownerRepository.deleteById(id);
    }

    public void addCatToOwner(Integer catId, Integer ownerId) throws OwnerExistenceException {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new OwnerExistenceException("Owner doesn't exist")
        );
        owner.addCat(catId);
        ownerRepository.save(owner);
    }

    public void deleteOwnership(Integer catId, Integer ownerId) throws OwnerExistenceException {
        Owner owner = ownerRepository.findById(ownerId).orElseThrow(
                () -> new OwnerExistenceException("Owner doesn't exist")
        );
        owner.removeCat(catId);
        ownerRepository.save(owner);
    }

    public List<OwnerDto> getAllOwners() {
        return ownerRepository.findAll().stream().map(mapper::convertToOwnerDto).collect(Collectors.toList());
    }
}
