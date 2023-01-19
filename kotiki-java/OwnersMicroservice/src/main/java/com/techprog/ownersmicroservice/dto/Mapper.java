package com.techprog.ownersmicroservice.dto;

import com.techprog.entities.owner.Owner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class Mapper {
    public OwnerDto convertToOwnerDto(Owner owner){
        String name = owner.getName();
        String id = owner.getId().toString();
        String dateOfBirth = owner.getDateOfBirth().toString();
        List<String> catsId = owner.getCatsId().stream().map(String::valueOf).toList();
        return new OwnerDto(id, name, dateOfBirth, catsId);
    }

    public Owner convertToOwner(OwnerDto ownerDto){
        String name = ownerDto.getName();
        LocalDate dateOfBirth = LocalDate.parse(ownerDto.getDateOfBirth());
        Integer id = ownerDto.getId() == null ? null : Integer.parseInt(ownerDto.getId());
        Set<Integer> catsId = new HashSet<>();
        if (ownerDto.getCatsId() != null) {
            catsId.addAll(ownerDto.getCatsId().stream().map(Integer::parseInt).toList());
        }

        Owner owner = new Owner(name, dateOfBirth);
        owner.setCatsId(catsId);
        if (id != null) {
            owner.setId(id);
        }
        return owner;
    }

    public RabbitListenerOwnerResponse convertToRabbitListenerOwnerResponse(OwnerDto ownerDto, String message){
        return new RabbitListenerOwnerResponse(ownerDto, message);
    }
}
