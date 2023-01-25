package com.techprog.outerinterfacesmicroservice.service;

import com.techprog.entities.cat.Color;
import com.techprog.entities.user.ApplicationUserRole;
import com.techprog.entities.user.User;
import com.techprog.outerinterfacesmicroservice.dto.CatDto;
import com.techprog.outerinterfacesmicroservice.dto.OwnerDto;
import com.techprog.outerinterfacesmicroservice.tools.AccessibilityException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppServiceTests {
    @Mock
    UserService userService;

    @Mock
    RequestSender requestSender;

    @InjectMocks
    AppService underTest;

    @Test
    public void testAddCatOwnerRole() throws Exception{
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        Integer catId = 1;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);
        CatDto createdCatDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(null).when(catDto).getOwnerId();
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(ownerId).when(user).getOwnerId();
        doNothing().when(catDto).setOwnerId(ownerId.toString());
        doReturn(createdCatDto).when(requestSender).createCat(catDto);
        doReturn(catId.toString()).when(createdCatDto).getId();

        // when
        CatDto actualCreatedCatDto = underTest.addCat(catDto, username);

        // then
        assertEquals(createdCatDto, actualCreatedCatDto);
        verify(catDto).setOwnerId(ownerId.toString());
        verify(requestSender).createCat(catDto);
        verify(requestSender).startOwnership(catId, ownerId);
    }

    @Test
    public void testAddCatOwnerRoleAccessibilityException(){
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(ownerId.toString()).when(catDto).getOwnerId();

        // when
        // then
        assertThrows(AccessibilityException.class, () -> underTest.addCat(catDto, username));
    }

    @Test
    public void testAddCatAdminRole() throws Exception{
        // given
        String username = "alkafol";
        Integer catId = 1;
        Integer ownerId = 1;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);
        CatDto createdCatDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();
        doReturn(ownerId.toString()).when(catDto).getOwnerId();
        doReturn(mock(OwnerDto.class)).when(requestSender).getOwnerById(ownerId);
        doNothing().when(catDto).setOwnerId(ownerId.toString());
        doReturn(catId.toString()).when(createdCatDto).getId();
        doReturn(createdCatDto).when(requestSender).createCat(catDto);
        doNothing().when(requestSender).startOwnership(catId, ownerId);

        // when
        CatDto actualReturnedCatDto = underTest.addCat(catDto, username);

        // then
        assertEquals(createdCatDto, actualReturnedCatDto);
        verify(userService).loadUserByUsername(username);
        verify(requestSender).getOwnerById(ownerId);
        verify(catDto).setOwnerId(ownerId.toString());
        verify(requestSender).createCat(catDto);
        verify(requestSender).startOwnership(catId, ownerId);
    }

    @Test
    public void testGetCatByIdOwnerRole() throws Exception{
        // given
        Integer catId = 1;
        String username = "alkafol";
        Integer realCatsOwnerId = 3;
        Integer userOwnerId = 3;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(catDto).when(requestSender).getCatById(catId);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(realCatsOwnerId.toString()).when(catDto).getOwnerId();
        doReturn(userOwnerId).when(user).getOwnerId();

        // when
        CatDto returnedCatDto = underTest.getCatById(catId, username);

        // then
        verify(requestSender).getCatById(catId);
        verify(userService).loadUserByUsername(username);
        assertEquals(catDto, returnedCatDto);
    }

    @Test
    public void testGetCatByIdOwnerRoleAccessibilityException() throws Exception{
        // given
        Integer catId = 1;
        String username = "alkafol";
        Integer realCatsOwnerId = 2;
        Integer userOwnerId = 3;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(catDto).when(requestSender).getCatById(catId);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(realCatsOwnerId.toString()).when(catDto).getOwnerId();
        doReturn(userOwnerId).when(user).getOwnerId();

        // when
        // then
        assertThrows(AccessibilityException.class, () -> underTest.getCatById(catId, username));
    }

    @Test
    public void testGetCatByIdAdminRole() throws Exception{
        // given
        Integer catId = 1;
        String username = "alkafol";
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(catDto).when(requestSender).getCatById(catId);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();

        // when
        CatDto returnedCatDto = underTest.getCatById(catId, username);

        // then
        verify(userService).loadUserByUsername(username);
        verify(requestSender).getCatById(catId);
        assertEquals(catDto, returnedCatDto);
    }

    @Test
    public void testGetAllCatsOwnerRole() throws Exception{
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        Integer catId = 2;
        User user = mock(User.class);
        OwnerDto ownerDto = mock(OwnerDto.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(ownerId).when(user).getOwnerId();
        doReturn(ownerDto).when(requestSender).getOwnerById(ownerId);
        doReturn(List.of(catId.toString())).when(ownerDto).getCatsId();
        doReturn(catDto).when(requestSender).getCatById(catId);

        // when
        List<CatDto> returnedCatsDto = underTest.getAllCats(username);

        // then
        verify(userService).loadUserByUsername(username);
        verify(requestSender).getOwnerById(ownerId);
        verify(requestSender).getCatById(catId);
        assertEquals(List.of(catDto), returnedCatsDto);
    }

    @Test
    public void testGetAllCatsAdminRole() throws Exception{
        // given
        String username = "alkafol";
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);
        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();
        doReturn(List.of(catDto)).when(requestSender).getAllCats();

        // when
        List<CatDto> allCatsDto = underTest.getAllCats(username);

        // then
        verify(requestSender).getAllCats();
        assertEquals(List.of(catDto), allCatsDto);
    }

    @Test
    public void testStartFriendshipOwnerRole() throws Exception{
        // given
        String username = "alkafol";
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Integer firstCatOwnerId = 1;
        CatDto firstCatDto = mock(CatDto.class);
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(firstCatDto).when(requestSender).getCatById(firstCatId);
        doReturn(firstCatOwnerId.toString()).when(firstCatDto).getOwnerId();
        doReturn(firstCatOwnerId).when(user).getOwnerId();
        doNothing().when(requestSender).startFriendship(firstCatId, secondCatId);

        // when
        underTest.startFriendship(username, firstCatId, secondCatId);

        // then
        verify(userService).loadUserByUsername(username);
        verify(requestSender).startFriendship(firstCatId, secondCatId);
    }

    @Test
    public void testStartFriendshipOwnerRoleAccessibilityException() throws Exception{
        // given
        String username = "alkafol";
        Integer userOwnerId = 3;
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Integer firstCatOwnerId = 1;
        Integer secondCatOwnerId = 2;
        CatDto firstCatDto = mock(CatDto.class);
        CatDto secondCatDto = mock(CatDto.class);
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(firstCatDto).when(requestSender).getCatById(firstCatId);
        doReturn(firstCatOwnerId.toString()).when(firstCatDto).getOwnerId();
        doReturn(userOwnerId).when(user).getOwnerId();
        doReturn(secondCatDto).when(requestSender).getCatById(secondCatId);
        doReturn(secondCatOwnerId.toString()).when(secondCatDto).getOwnerId();

        // when
        // then
        assertThrows(AccessibilityException.class, () -> underTest.startFriendship(username, firstCatId, secondCatId));
        verify(firstCatDto).getOwnerId();
        verify(secondCatDto).getOwnerId();
        verify(user, atLeastOnce()).getOwnerId();
        verify(user).getRole();
    }

    @Test
    public void testStartFriendshipAdminRole() throws Exception{
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        String username = "alkafol";
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();

        // when
        underTest.startFriendship(username, firstCatId, secondCatId);

        // then
        verify(user).getRole();
        verify(requestSender).startFriendship(firstCatId, secondCatId);
    }

    @Test
    public void testStopFriendshipOwnerRole() throws Exception{
        // given
        String username = "alkafol";
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Integer firstCatOwnerId = 1;
        CatDto firstCatDto = mock(CatDto.class);
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(firstCatDto).when(requestSender).getCatById(firstCatId);
        doReturn(firstCatOwnerId.toString()).when(firstCatDto).getOwnerId();
        doReturn(firstCatOwnerId).when(user).getOwnerId();
        doNothing().when(requestSender).stopFriendship(firstCatId, secondCatId);

        // when
        underTest.stopFriendship(username, firstCatId, secondCatId);

        // then
        verify(userService).loadUserByUsername(username);
        verify(requestSender).stopFriendship(firstCatId, secondCatId);
    }

    @Test
    public void testStopFriendshipOwnerRoleAccessibilityException() throws Exception{
        // given
        String username = "alkafol";
        Integer userOwnerId = 3;
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Integer firstCatOwnerId = 1;
        Integer secondCatOwnerId = 2;
        CatDto firstCatDto = mock(CatDto.class);
        CatDto secondCatDto = mock(CatDto.class);
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(firstCatDto).when(requestSender).getCatById(firstCatId);
        doReturn(firstCatOwnerId.toString()).when(firstCatDto).getOwnerId();
        doReturn(userOwnerId).when(user).getOwnerId();
        doReturn(secondCatDto).when(requestSender).getCatById(secondCatId);
        doReturn(secondCatOwnerId.toString()).when(secondCatDto).getOwnerId();

        // when
        // then
        assertThrows(AccessibilityException.class, () -> underTest.stopFriendship(username, firstCatId, secondCatId));
        verify(firstCatDto).getOwnerId();
        verify(secondCatDto).getOwnerId();
        verify(user, atLeastOnce()).getOwnerId();
        verify(user).getRole();
    }

    @Test
    public void testStopFriendshipAdminRole() throws Exception{
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        String username = "alkafol";
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();

        // when
        underTest.stopFriendship(username, firstCatId, secondCatId);

        // then
        verify(user).getRole();
        verify(requestSender).stopFriendship(firstCatId, secondCatId);
    }

    @Test
    public void testDeleteCatOwnerRole() throws Exception{
        // given
        String username = "alkafol";
        Integer catId = 1;
        Integer ownerId = 1;
        CatDto catDto = mock(CatDto.class);
        User user = mock(User.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(catDto).when(requestSender).getCatById(catId);
        doReturn(ownerId.toString()).when(catDto).getOwnerId();
        doReturn(ownerId).when(user).getOwnerId();
        doNothing().when(requestSender).deleteCat(catId);
        doNothing().when(requestSender).stopOwnership(catId, ownerId);

        // when
        underTest.deleteCat(username, catId);

        // then
        verify(userService).loadUserByUsername(username);
        verify(user).getRole();
        verify(requestSender).deleteCat(catId);
        verify(requestSender).stopOwnership(catId, ownerId);
    }

    @Test
    public void testDeleteCatOwnerRoleAccessibilityException() throws Exception{
        // given
        String username = "alkafol";
        Integer catId = 1;
        Integer userOwnerId = 1;
        Integer catOwnerId = 2;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(catDto).when(requestSender).getCatById(catId);
        doReturn(catOwnerId.toString()).when(catDto).getOwnerId();
        doReturn(userOwnerId).when(user).getOwnerId();

        // when
        // then
        assertThrows(AccessibilityException.class, () -> underTest.deleteCat(username, catId));
        verify(userService).loadUserByUsername(username);
        verify(user).getRole();
        verify(requestSender).getCatById(catId);

    }

    @Test
    public void testDeleteCatAdminRole() throws Exception{
        // given
        String username = "alkafol";
        Integer catId = 1;
        Integer ownerId = 2;
        User user = mock(User.class);
        CatDto catDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();
        doReturn(catDto).when(requestSender).getCatById(catId);
        doReturn(ownerId.toString()).when(catDto).getOwnerId();
        doNothing().when(requestSender).deleteCat(catId);
        doNothing().when(requestSender).stopOwnership(catId, ownerId);

        // when
        underTest.deleteCat(username, catId);

        // then
        verify(userService).loadUserByUsername(username);
        verify(user).getRole();
        verify(requestSender).deleteCat(catId);
        verify(requestSender).stopOwnership(catId, ownerId);
    }

    @Test
    public void testGetByColorOwnerRole() throws Exception{
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        User user = mock(User.class);
        CatDto blackCatDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.OWNER).when(user).getRole();
        doReturn(ownerId).when(user).getOwnerId();
        doReturn(List.of(blackCatDto)).when(requestSender).getByColor(Color.black, ownerId);

        // when
        List<CatDto> returnedCats = underTest.getByColor(username, Color.black);

        // then
        assertEquals(List.of(blackCatDto), returnedCats);
        verify(userService).loadUserByUsername(username);
        verify(requestSender).getByColor(Color.black, ownerId);
    }

    @Test
    public void testGetByColorAdminRole() throws Exception{
        // given
        String username = "alkafol";
        Integer ownerId = 1;
        User user = mock(User.class);
        CatDto blackCatDto = mock(CatDto.class);

        doReturn(user).when(userService).loadUserByUsername(username);
        doReturn(ApplicationUserRole.ADMIN).when(user).getRole();
        doReturn(List.of(blackCatDto)).when(requestSender).getByColor(Color.black, null);

        // when
        underTest.getByColor(username, Color.black);

        // then
        verify(userService).loadUserByUsername(username);
        verify(requestSender).getByColor(Color.black, null);
    }
}
