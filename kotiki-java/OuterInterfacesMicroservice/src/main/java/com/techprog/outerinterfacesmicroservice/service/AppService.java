package com.techprog.outerinterfacesmicroservice.service;

import com.techprog.entities.cat.Color;
import com.techprog.entities.user.ApplicationUserRole;
import com.techprog.entities.user.User;
import com.techprog.outerinterfacesmicroservice.dto.CatDto;
import com.techprog.outerinterfacesmicroservice.tools.AccessibilityException;
import com.techprog.outerinterfacesmicroservice.tools.CatMicroserviceException;
import com.techprog.outerinterfacesmicroservice.tools.OwnerMicroserviceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {
    private final UserService userService;
    private final RequestSender requestSender;

    public AppService(UserService userService, RequestSender requestSender) {
        this.userService = userService;
        this.requestSender = requestSender;
    }

    public CatDto addCat(CatDto catDto, String username) throws CatMicroserviceException, OwnerMicroserviceException, AccessibilityException {
        User user = userService.loadUserByUsername(username);
        String ownerId = catDto.getOwnerId();

        if (user.getRole() == ApplicationUserRole.OWNER) {
            // owner can't specify ownerId
            if (ownerId != null) {
                throw new AccessibilityException("You can't specify owner id");
            }

            catDto.setOwnerId(user.getOwnerId().toString());
            CatDto createdCat = requestSender.createCat(catDto);
            requestSender.startOwnership(Integer.parseInt(createdCat.getId()), user.getOwnerId());
            return createdCat;
        } else {
            // admin can create cat for himself or for anyone else
            if (ownerId == null) {
                ownerId = user.getOwnerId().toString();
            } else {
                // check existence
                requestSender.getOwnerById(Integer.parseInt(ownerId));
            }
            catDto.setOwnerId(ownerId);

            CatDto createdCat = requestSender.createCat(catDto);
            requestSender.startOwnership(Integer.parseInt(createdCat.getId()), Integer.parseInt(ownerId));
            return createdCat;
        }
    }

    public CatDto getCatById(Integer catId, String username) throws CatMicroserviceException, AccessibilityException {
        User user = userService.loadUserByUsername(username);
        CatDto responseCatDto = requestSender.getCatById(catId);

        if (user.getRole() == ApplicationUserRole.OWNER) {
            if (!responseCatDto.getOwnerId().equals(user.getOwnerId().toString())) {
                throw new AccessibilityException("Access forbidden");
            }
        }

        return responseCatDto;
    }

    public List<CatDto> getAllCats(String username) throws CatMicroserviceException, OwnerMicroserviceException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == ApplicationUserRole.OWNER) {
            List<String> catsId = requestSender.getOwnerById(user.getOwnerId()).getCatsId();

            List<CatDto> allCats = new ArrayList<>();
            for (String id : catsId) {
                allCats.add(requestSender.getCatById(Integer.parseInt(id)));
            }

            return allCats;
        } else {
            return requestSender.getAllCats();
        }
    }

    public void startFriendship(String username, Integer firstId, Integer secondId) throws CatMicroserviceException, AccessibilityException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == ApplicationUserRole.OWNER
                && !requestSender.getCatById(firstId).getOwnerId().equals(user.getOwnerId().toString())
                && !requestSender.getCatById(secondId).getOwnerId().equals(user.getOwnerId().toString())) {
            throw new AccessibilityException("Access forbidden");
        }

        requestSender.startFriendship(firstId, secondId);
    }

    public void stopFriendship(String username, Integer firstId, Integer secondId) throws CatMicroserviceException, AccessibilityException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == ApplicationUserRole.OWNER
                && !requestSender.getCatById(firstId).getOwnerId().equals(user.getOwnerId().toString())
                && !requestSender.getCatById(secondId).getOwnerId().equals(user.getOwnerId().toString())) {
            throw new AccessibilityException("Access forbidden");
        }

        requestSender.stopFriendship(firstId, secondId);
    }

    public void deleteCat(String username, Integer catId) throws CatMicroserviceException, OwnerMicroserviceException, AccessibilityException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == ApplicationUserRole.OWNER) {
            if (!requestSender.getCatById(catId).getOwnerId().equals(user.getOwnerId().toString())) {
                throw new AccessibilityException("Accessibility exception");
            }
            requestSender.deleteCat(catId);
            requestSender.stopOwnership(catId, user.getOwnerId());
        }
        else {
            Integer ownerId = Integer.parseInt(requestSender.getCatById(catId).getOwnerId());
            requestSender.deleteCat(catId);
            requestSender.stopOwnership(catId, ownerId);
        }
    }


    public List<CatDto> getByColor(String username, Color color) throws CatMicroserviceException {
        User user = userService.loadUserByUsername(username);

        if (user.getRole() == ApplicationUserRole.OWNER) {
            return requestSender.getByColor(color, user.getOwnerId());
        } else {
            return requestSender.getByColor(color, null);
        }
    }
}

