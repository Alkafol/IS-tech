package com.techprog.upgradedcats.service;

import com.techprog.upgradedcats.dto.CatDto;
import com.techprog.upgradedcats.dto.Mapper;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.repository.CatRepository;
import com.techprog.upgradedcats.security.ApplicationUserRole;
import com.techprog.upgradedcats.tools.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatServiceTest {

    @Mock
    private CatRepository catRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private OwnerService ownerService;
    @Mock
    private UserService userService;
    private CatService underTest;

    @BeforeEach
    void SetUp() {
        underTest = new CatService(userService, catRepository, mapper, ownerService);
    }

    @Test
    void canAddCatWithOwnerRole() throws OwnerAccessibilityException, OwnerExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        CatDto catDto = new CatDto("Barsik", null, "grey", null, "british",
                "2015-6-14", null);
        Cat cat = new Cat("Barsik", LocalDate.of(2000, 4, 14), "british", Color.grey, null);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(mapper.convertToCat(catDto)).thenReturn(cat);

        // when
        underTest.addCat(catDto, "alkafol");

        // then
        ArgumentCaptor<Cat> catArgumentCaptor = ArgumentCaptor.forClass(Cat.class);
        verify(catRepository).save(catArgumentCaptor.capture());
        Cat capturedCat = catArgumentCaptor.getValue();

        assert (capturedCat).equals(cat);
    }

    @Test
    void canAddCatWithOwnerRoleAndPredefinedOwner() throws OwnerAccessibilityException, OwnerExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        CatDto catDto = new CatDto("Barsik", null, "grey", owner.getId(), "british",
                "2015-6-14", null);
        Cat cat = new Cat("Barsik", LocalDate.of(2000, 4, 14), "british", Color.grey, owner);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(mapper.convertToCat(catDto)).thenReturn(cat);

        // when
        // then
        assertThrows(OwnerAccessibilityException.class, () -> underTest.addCat(catDto, "alkafol"));
    }

    @Test
    void canAddCatWithAdminRoleWithPredefinedNotExistedOwner() throws OwnerExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.ADMIN, owner);
        CatDto catDto = new CatDto("Barsik", null, "grey", "1111", "british",
                "2015-6-14", null);
        when(ownerService.getOwnerById("1111")).thenReturn(null);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);

        // when
        // then
        assertThrows(OwnerExistenceException.class, () -> underTest.addCat(catDto, "alkafol"));
    }

    @Test
    void canAddCatWithAdminRoleWithAdminOwner() throws OwnerExistenceException, OwnerAccessibilityException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.ADMIN, owner);
        CatDto catDto = new CatDto("Barsik", null, "grey", null, "british",
                "2015-6-14", null);
        Cat cat = new Cat("Barsik", LocalDate.of(2000, 4, 14), "british", Color.grey, null);

        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(mapper.convertToCat(catDto)).thenReturn(cat);

        // when
        underTest.addCat(catDto, "alkafol");

        // then
        ArgumentCaptor<Cat> catArgumentCaptor = ArgumentCaptor.forClass(Cat.class);
        verify(catRepository).save(catArgumentCaptor.capture());
        Cat capturedCat = catArgumentCaptor.getValue();

        assert (capturedCat).equals(cat);

    }

    @Test
    void getAnotherOwnersCatById() {
        // given
        Owner catOwner = new Owner("Anton", LocalDate.of(2001, 12, 1));
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        CatDto catDto = new CatDto("Barsik", "1111", "grey", catOwner.getId(), "british",
                "2015-6-14", null);
        Cat foundedCat = Cat.createCustomCat("Barsik", "1111", LocalDate.of(2015, 6, 14),
                "british", Color.grey, catOwner, null);

        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById("1111")).thenReturn(Optional.of(foundedCat));

        // when
        // then
        assertThrows(CatOwnershipException.class, () -> underTest.getCatById("1111", "alkafol"));
    }

    @Test
    void deleteCatByIdByAnotherOwner() {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        Owner catOwner = new Owner("Anton", LocalDate.of(1999, 5, 6));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        Cat cat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, catOwner);
        catOwner.addCat(cat);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));

        // when
        // then
        assertThrows(OwnerAccessibilityException.class, () -> underTest.deleteCatById(cat.getId(), "alkafol"));
    }

    @Test
    void stoppingFriendshipCheckForDeletedCat() throws OwnerAccessibilityException, NonexistentFriendshipException, CatExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        Cat cat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, owner);
        Cat friend = new Cat("Boris", LocalDate.of(2010, 12, 21), "british",
                Color.black, owner);
        cat.addFriend(friend);
        friend.addFriend(cat);
        CatDto catDto = new CatDto("Boris", friend.getId(), "grey", owner.getId(), "british",
                "2010-12-21", List.of(cat.getId()));
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
        when(catRepository.findById(friend.getId())).thenReturn(Optional.of(friend));
        when(mapper.convertToCat(catDto)).thenReturn(friend);

        CatService spiedCatService = Mockito.spy(underTest);
        doReturn(List.of(catDto)).when(spiedCatService).getAllCatFriends(cat.getId(), "alkafol");

        // when
        spiedCatService.deleteCatById(cat.getId(), "alkafol");

        // then
        verify(catRepository).delete(cat);
        assertEquals(0, cat.getFriends().size());
        assertNull(cat.getOwner());
    }

    @Test
    void startFriendshipByAnotherOwner() throws OwnerAccessibilityException, CatExistenceException, ExistedFriendshipException {
        // given
        Owner userOwner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        Owner catsOwner = new Owner("Anton", LocalDate.of(2001, 12, 1));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, userOwner);
        Cat firstCat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, catsOwner);
        Cat secondCat = new Cat("Boris", LocalDate.of(2007, 2, 15), "british",
                Color.black, catsOwner);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(firstCat.getId())).thenReturn(Optional.of(firstCat));
        when(catRepository.findById(secondCat.getId())).thenReturn(Optional.of(secondCat));

        // when
        // then
        assertThrows(OwnerAccessibilityException.class, () -> underTest.startFriendship(firstCat.getId(),
                secondCat.getId(), "alkafol"));
    }

    @Test
    void startFriendshipWithAlreadyExistedFriendship() throws OwnerAccessibilityException, CatExistenceException, ExistedFriendshipException {
        // given
        Owner catsOwner = new Owner("Anton", LocalDate.of(2001, 12, 1));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, catsOwner);
        Cat firstCat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, catsOwner);
        Cat secondCat = new Cat("Boris", LocalDate.of(2007, 2, 15), "british",
                Color.black, catsOwner);
        firstCat.addFriend(secondCat);
        secondCat.addFriend(firstCat);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(firstCat.getId())).thenReturn(Optional.of(firstCat));
        when(catRepository.findById(secondCat.getId())).thenReturn(Optional.of(secondCat));

        // when
        // then
        assertThrows(ExistedFriendshipException.class, () -> underTest.startFriendship(firstCat.getId(),
                secondCat.getId(), "alkafol"));

    }

    @Test
    void stopFriendshipByAnotherOwner() {
        // given
        Owner userOwner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        Owner catsOwner = new Owner("Anton", LocalDate.of(2001, 12, 1));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, userOwner);
        Cat firstCat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, catsOwner);
        Cat secondCat = new Cat("Boris", LocalDate.of(2007, 2, 15), "british",
                Color.black, catsOwner);
        firstCat.addFriend(secondCat);
        secondCat.addFriend(firstCat);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(firstCat.getId())).thenReturn(Optional.of(firstCat));
        when(catRepository.findById(secondCat.getId())).thenReturn(Optional.of(secondCat));

        // when
        // then
        assertThrows(OwnerAccessibilityException.class, () -> underTest.startFriendship(firstCat.getId(),
                secondCat.getId(), "alkafol"));
    }

    @Test
    void getAllCatsWithOwnerRole() {
        //given
        Owner firstOwner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, firstOwner);
        Cat firstOwnerCat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, firstOwner);
        firstOwner.addCat(firstOwnerCat);
        CatDto firstCatDto = new CatDto("Barsik", firstOwnerCat.getId(), "grey", firstOwner.getId(),
                "british", "2015-1-1", null);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(mapper.convertToCatDto(firstOwnerCat)).thenReturn(firstCatDto);

        // when
        List<CatDto> cats = underTest.getAllCats("alkafol");

        // then
        assertEquals(cats, List.of(firstCatDto));
    }

    @Test
    void getAllCatsWithAdminRole() {
        // given
        Owner firstOwner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.ADMIN, firstOwner);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);

        // when
        underTest.getAllCats("alkafol");

        // then
        verify(catRepository).findAll();
    }

    @Test
    void getAllCatFriendsFromAnotherOwner() throws OwnerAccessibilityException, CatExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        Owner realOwnerOfCat = new Owner("Kirill", LocalDate.of(1999, 4, 19));

        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        Cat cat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, realOwnerOfCat);
        realOwnerOfCat.addCat(cat);

        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));

        // when
        // then
        assertThrows(OwnerAccessibilityException.class, () -> underTest.getAllCatFriends(cat.getId(), "alkafol"));
    }

    @Test
    void getAllCatFriendsFromCatOwner() throws OwnerAccessibilityException, CatExistenceException {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        Cat cat = new Cat("Barsik", LocalDate.of(2015, 1, 1), "british",
                Color.grey, owner);
        owner.addCat(cat);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);
        when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));

        // when
        underTest.getAllCatFriends(cat.getId(), "alkafol");

        // then
        verify(catRepository).findFriendsFromFirstFriendshipColumn(cat.getId());
        verify(catRepository).findFriendsFromSecondFriendshipColumn(cat.getId());
    }

    @Test
    void getColoredFromOwner() {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.OWNER, owner);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);

        // when
        underTest.getColored(Color.grey, "alkafol");

        // then
        verify(catRepository).findCatsByColorWithSpecifiedOwner(Color.grey, user.getOwner());
    }

    @Test
    void getColoredFromAdmin() {
        // given
        Owner owner = new Owner("Roman", LocalDate.of(2000, 4, 14));
        User user = new User("alkafol", "1234", ApplicationUserRole.ADMIN, owner);
        when(userService.loadUserByUsername("alkafol")).thenReturn(user);

        // when
        underTest.getColored(Color.grey, "alkafol");

        // then
        verify(catRepository).findAllCatsByColor(Color.grey);
    }
}