package com.techprog.catsmicroservice.service;

import com.techprog.catsmicroservice.dto.CatDto;
import com.techprog.catsmicroservice.dto.Mapper;
import com.techprog.catsmicroservice.repository.CatRepository;
import com.techprog.catsmicroservice.tools.CatExistenceException;
import com.techprog.catsmicroservice.tools.ExistedFriendshipException;
import com.techprog.catsmicroservice.tools.NonexistentFriendshipException;
import com.techprog.entities.cat.Cat;
import com.techprog.entities.cat.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTests {
    @Mock
    CatRepository catRepository;

    @Mock
    Mapper mapper;

    @InjectMocks
    CatService underTest;

    @Test
    public void testGetCatById() throws Exception{
        // given
        Integer catId = 1;
        Cat cat = mock(Cat.class);
        CatDto catDto = mock(CatDto.class);
        underTest = spy(underTest);
        doReturn(Optional.of(cat)).when(catRepository).findById(catId);
        doReturn(Collections.emptyList()).when(underTest).getAllCatFriends(catId);
        doReturn(catDto).when(mapper).convertToCatDto(cat);

        // when
        CatDto foundedCat = underTest.getCatById(catId);

        // then
        assertEquals(foundedCat, catDto);
    }

    @Test
    public void testGetCatByIdCatExistenceException(){
        // given
        Integer catId = 1;
        doReturn(Optional.empty()).when(catRepository).findById(catId);

        // when
        // then
        assertThrows(CatExistenceException.class, () -> underTest.deleteCatById(catId));
    }

    @Test
    public void testDeleteCatById() throws Exception{
        // given
        Integer catId = 1;
        Cat cat = mock(Cat.class);
        Integer friendId = 2;
        Cat friend = mock(Cat.class);
        underTest = spy(underTest);
        doReturn(friendId).when(friend).getId();
        doReturn(Optional.of(cat)).when(catRepository).findById(catId);
        doReturn(List.of(friend)).when(underTest).getAllCatFriends(catId);
        doNothing().when(underTest).stopFriendship(catId, friendId);

        // when
        underTest.deleteCatById(catId);

        // then
        verify(underTest).getAllCatFriends(catId);
        verify(underTest).stopFriendship(catId, friendId);
        verify(catRepository).delete(cat);
    }

    @Test
    public void deleteCatByIdCatExistenceException(){
        // given
        Integer catId = 1;
        doReturn(Optional.empty()).when(catRepository).findById(catId);

        // when
        // then
        assertThrows(CatExistenceException.class, () -> underTest.deleteCatById(catId));
    }

    @Test
    public void addCatTest(){
        // given
        Cat cat = mock(Cat.class);
        CatDto catDto = mock(CatDto.class);
        Cat savedCat = mock(Cat.class);
        CatDto finalDto = mock(CatDto.class);
        doReturn(cat).when(mapper).convertToCat(catDto);
        doReturn(savedCat).when(catRepository).save(cat);
        doReturn(finalDto).when(mapper).convertToCatDto(savedCat);

        // when
        CatDto returnedDto = underTest.addCat(catDto);

        // then
        assertEquals(finalDto, returnedDto);
        verify(catRepository).save(cat);
    }

    @Test
    public void testStartFriendship() throws Exception{
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        Cat secondCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.of(secondCat)).when(catRepository).findById(secondCatId);
        doReturn(Collections.emptySet()).when(firstCat).getFriends();
        doReturn(Collections.emptySet()).when(secondCat).getFriends();
        doNothing().when(firstCat).addFriends(List.of(secondCat));
        doReturn(mock(Cat.class)).when(catRepository).save(firstCat);

        // when
        underTest.startFriendship(firstCatId, secondCatId);

        // then
        verify(firstCat).getFriends();
        verify(secondCat).getFriends();
        verify(firstCat).addFriends(List.of(secondCat));
        verify(catRepository).save(firstCat);
    }

    @Test
    public void testStartFriendshipWithFirstCatExistenceException(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        doReturn(Optional.empty()).when(catRepository).findById(firstCatId);

        // when
        // then
        assertThrows(CatExistenceException.class, () -> underTest.startFriendship(firstCatId, secondCatId));
    }

    @Test
    public void testStartFriendshipWithSecondCatExistenceException(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.empty()).when(catRepository).findById(secondCatId);

        // when
        // then
        assertThrows(CatExistenceException.class, () -> underTest.startFriendship(firstCatId, secondCatId));
    }

    @Test
    public void testStartFriendshipWithExistedFriendshipException(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        Cat secondCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.of(secondCat)).when(catRepository).findById(secondCatId);
        doReturn(Set.of(secondCat)).when(firstCat).getFriends();

        // when
        // then
        assertThrows(ExistedFriendshipException.class, () -> underTest.startFriendship(firstCatId, secondCatId));
    }

    @Test
    public void testStopFriendshipFromFirstCat() throws Exception {
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        Cat secondCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.of(secondCat)).when(catRepository).findById(secondCatId);
        doReturn(Set.of(secondCat)).when(firstCat).getFriends();
        doNothing().when(firstCat).removeFriendById(secondCatId);
        doReturn(mock(Cat.class)).when(catRepository).save(firstCat);

        // when
        underTest.stopFriendship(firstCatId, secondCatId);

        // then
        verify(firstCat).removeFriendById(secondCatId);
        verify(catRepository).save(firstCat);
    }

    @Test
    public void testStopFriendshipFromSecondCat() throws Exception{
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        Cat secondCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.of(secondCat)).when(catRepository).findById(secondCatId);
        doReturn(Collections.emptySet()).when(firstCat).getFriends();
        doReturn(Set.of(firstCat)).when(secondCat).getFriends();
        doNothing().when(secondCat).removeFriendById(firstCatId);
        doReturn(mock(Cat.class)).when(catRepository).save(secondCat);

        // when
        underTest.stopFriendship(firstCatId, secondCatId);

        // then
        verify(secondCat).removeFriendById(firstCatId);
        verify(catRepository).save(secondCat);
    }

    @Test
    public void testStopFriendshipWithFirstCatExistenceException(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        doReturn(Optional.empty()).when(catRepository).findById(firstCatId);

        // when
        // then
        assertThrows(CatExistenceException.class, () -> underTest.stopFriendship(firstCatId, secondCatId));
    }

    @Test
    public void testStopFriendshipWithSecondCatExistenceException(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.empty()).when(catRepository).findById(secondCatId);

        // when
        // then
        assertThrows(CatExistenceException.class, () -> underTest.stopFriendship(firstCatId, secondCatId));
    }

    @Test
    public void testStopFriendshipWithNonexistentFriendshipException(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        Cat secondCat = mock(Cat.class);
        doReturn(Optional.of(firstCat)).when(catRepository).findById(firstCatId);
        doReturn(Optional.of(secondCat)).when(catRepository).findById(secondCatId);
        doReturn(Collections.emptySet()).when(firstCat).getFriends();
        doReturn(Collections.emptySet()).when(secondCat).getFriends();

        // when
        // then
        assertThrows(NonexistentFriendshipException.class, () -> underTest.stopFriendship(firstCatId, secondCatId));
    }

    @Test
    public void testGetAllCats(){
        // given
        Integer firstCatId = 1;
        Integer secondCatId = 2;
        Cat firstCat = mock(Cat.class);
        Cat secondCat = mock(Cat.class);
        CatDto firstCatDto = mock(CatDto.class);
        CatDto secondCatDto = mock(CatDto.class);
        underTest = spy(underTest);
        doReturn(List.of(firstCat, secondCat)).when(catRepository).findAll();
        doReturn(firstCatId).when(firstCat).getId();
        doReturn(secondCatId).when(secondCat).getId();
        doReturn(Collections.emptyList()).when(underTest).getAllCatFriends(firstCatId);
        doReturn(Collections.emptyList()).when(underTest).getAllCatFriends(secondCatId);
        doNothing().when(firstCat).addFriends(Collections.emptyList());
        doNothing().when(secondCat).addFriends(Collections.emptyList());
        doReturn(firstCatDto).when(mapper).convertToCatDto(firstCat);
        doReturn(secondCatDto).when(mapper).convertToCatDto(secondCat);

        // when
        List<CatDto> allCats = underTest.getAllCats();

        // then
        verify(catRepository).findAll();
        verify(underTest).getAllCatFriends(firstCatId);
        verify(underTest).getAllCatFriends(secondCatId);
        verify(firstCat).addFriends(Collections.emptyList());
        verify(secondCat).addFriends(Collections.emptyList());
        assertEquals(firstCatDto, allCats.get(0));
        assertEquals(secondCatDto, allCats.get(1));
    }

    @Test
    public void testGetColored(){
        // given
        Cat firstCat = mock(Cat.class);
        CatDto firstCatDto = mock(CatDto.class);
        doReturn(List.of(firstCat)).when(catRepository).findAllCatsByColor(Color.black);
        doReturn(firstCatDto).when(mapper).convertToCatDto(firstCat);

        // when
        List<CatDto> allColoredCats = underTest.getColored(Color.black);

        // then
        verify(catRepository).findAllCatsByColor(Color.black);
        assertEquals(firstCatDto, allColoredCats.get(0));
    }

    @Test
    public void testGetColoredWithSpecifiedOwner(){
        // given
        Integer ownerId = 1;
        Cat firstCat = mock(Cat.class);
        CatDto firstCatDto = mock(CatDto.class);
        doReturn(List.of(firstCat)).when(catRepository).findCatsByColorWithSpecifiedOwner(Color.black, ownerId);
        doReturn(firstCatDto).when(mapper).convertToCatDto(firstCat);

        // when
        List<CatDto> allColoredCats = underTest.getColoredWithSpecifiedOwner(Color.black, ownerId);

        // then
        verify(catRepository).findCatsByColorWithSpecifiedOwner(Color.black, ownerId);
        assertEquals(firstCatDto, allColoredCats.get(0));
    }

    @Test
    public void testGetAllCatFriends(){
        // given
        Integer catId = 1;
        Integer firstFriendId = 2;
        Integer secondFriendId = 3;
        Cat firstFriend = mock(Cat.class);
        Cat secondFriend = mock(Cat.class);
        doReturn(new ArrayList<>(List.of(secondFriendId))).when(catRepository).findFriendsFromFirstFriendshipColumn(catId);
        doReturn(new ArrayList<>(List.of(firstFriendId))).when(catRepository).findFriendsFromSecondFriendshipColumn(catId);
        doReturn(Optional.of(firstFriend)).when(catRepository).findById(firstFriendId);
        doReturn(Optional.of(secondFriend)).when(catRepository).findById(secondFriendId);

        // when
        List<Cat> friends = underTest.getAllCatFriends(catId);

        // then
        verify(catRepository).findFriendsFromSecondFriendshipColumn(catId);
        verify(catRepository).findFriendsFromFirstFriendshipColumn(catId);
        assertEquals(List.of(firstFriend, secondFriend), friends);
    }
}
