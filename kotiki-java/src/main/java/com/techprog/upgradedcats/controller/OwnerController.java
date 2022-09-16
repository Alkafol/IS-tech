package com.techprog.upgradedcats.controller;

import com.techprog.upgradedcats.dto.OwnerDto;
import com.techprog.upgradedcats.service.OwnerService;
import com.techprog.upgradedcats.tools.OwnerExistenceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService){
        this.ownerService = ownerService;
    }

    @GetMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('owner:get_by_id')")
    public OwnerDto getOwnerById(@PathVariable String id){
        try {
            return ownerService.getOwnerById(id);
        } catch (OwnerExistenceException e) {
            System.out.println("Owner doesn't exist");
        }

        return null;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('owner:get_all')")
    public List<OwnerDto> getAllOwners(){
        return ownerService.getAllOwners();
    }
}
