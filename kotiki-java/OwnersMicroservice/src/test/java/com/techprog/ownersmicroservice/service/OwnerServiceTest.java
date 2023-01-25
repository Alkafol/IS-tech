package com.techprog.ownersmicroservice.service;

import com.techprog.entities.owner.Owner;
import com.techprog.ownersmicroservice.dto.Mapper;
import com.techprog.ownersmicroservice.dto.OwnerDto;
import com.techprog.ownersmicroservice.repository.OwnerRepository;
import com.techprog.ownersmicroservice.tools.OwnerExistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {
    @Mock
    OwnerRepository ownerRepository;

    @Mock
    Mapper mapper;

    @InjectMocks
    OwnerService underTest;

    @Test
    public void testAddOwner(){
        // given
        OwnerDto receivedOwnerDto = mock(OwnerDto.class);
        Owner convertedOwner = mock(Owner.class);
        Owner createdOwner = mock(Owner.class);
        OwnerDto returnedOwnerDto = mock(OwnerDto.class);
        doReturn(convertedOwner).when(mapper).convertToOwner(receivedOwnerDto);
        doReturn(createdOwner).when(ownerRepository).save(convertedOwner);
        doReturn(returnedOwnerDto).when(mapper).convertToOwnerDto(createdOwner);

        // when
        OwnerDto actualReturnedOwnerDto  = underTest.addOwner(receivedOwnerDto);

        // then
        assertEquals(returnedOwnerDto, actualReturnedOwnerDto);
        verify(ownerRepository).save(convertedOwner);
    }

    @Test
    public void testGetOwnerById() throws Exception{
        // given
        Integer ownerId = 1;
        Owner owner = mock(Owner.class);
        OwnerDto ownerDto = mock(OwnerDto.class);
        doReturn(Optional.of(owner)).when(ownerRepository).findById(ownerId);
        doReturn(ownerDto).when(mapper).convertToOwnerDto(owner);

        // when
        OwnerDto returnedOwner = underTest.getOwnerById(ownerId);

        // then
        verify(ownerRepository).findById(ownerId);
        assertEquals(ownerDto, returnedOwner);
    }

    @Test
    public void testGetOwnerByIdOwnerExistenceException(){
        // given
        Integer ownerId = 1;
        doReturn(Optional.empty()).when(ownerRepository).findById(ownerId);

        // when
        // then
        assertThrows(OwnerExistenceException.class, () -> underTest.getOwnerById(ownerId));
    }

    @Test
    public void testDeleteOwnerById() throws Exception{
        // given
        Integer ownerId = 1;
        Integer catId = 1;
        Owner owner = mock(Owner.class);
        underTest = spy(underTest);
        doReturn(Optional.of(owner)).when(ownerRepository).findById(ownerId);
        doReturn(Set.of(catId)).when(owner).getCatsId();
        doNothing().when(underTest).deleteOwnership(catId, ownerId);
        doNothing().when(ownerRepository).deleteById(ownerId);

        // when
        underTest.deleteOwnerById(ownerId);

        // then
        verify(ownerRepository).findById(ownerId);
        verify(underTest).deleteOwnership(catId, ownerId);
        verify(ownerRepository).deleteById(ownerId);
    }

    @Test
    public void testDeleteOwnerByIdOwnerExistenceException(){
        // given
        Integer ownerId = 1;
        doReturn(Optional.empty()).when(ownerRepository).findById(ownerId);

        // when
        // then
        assertThrows(OwnerExistenceException.class, () -> underTest.deleteOwnerById(ownerId));
    }

    @Test
    public void testAddCatToOwner() throws Exception{
        // given
        Integer ownerId = 1;
        Integer catId = 1;
        Owner owner = mock(Owner.class);
        doReturn(Optional.of(owner)).when(ownerRepository).findById(ownerId);
        doNothing().when(owner).addCat(catId);
        doReturn(mock(Owner.class)).when(ownerRepository).save(owner);

        // when
        underTest.addCatToOwner(catId, ownerId);

        // then
        verify(owner).addCat(catId);
        ownerRepository.save(owner);
    }

    @Test
    public void testAddCatToOwnerOwnerExistenceException(){
        // given
        Integer ownerId = 1;
        Integer catId = 1;
        doReturn(Optional.empty()).when(ownerRepository).findById(ownerId);

        // when
        // then
        assertThrows(OwnerExistenceException.class, () -> underTest.addCatToOwner(catId, ownerId));
    }

    @Test
    public void testDeleteOwnership() throws Exception{
        // given
        Integer ownerId = 1;
        Integer catId = 1;
        Owner owner = mock(Owner.class);
        doReturn(Optional.of(owner)).when(ownerRepository).findById(ownerId);
        doNothing().when(owner).removeCat(catId);
        doReturn(mock(Owner.class)).when(ownerRepository).save(owner);

        // when
        underTest.deleteOwnership(catId, ownerId);

        // then
        verify(owner).removeCat(catId);
        verify(ownerRepository).save(owner);
    }

    @Test
    public void testDeleteOwnershipOwnerExistenceException(){
        // given
        Integer ownerId = 1;
        Integer catId = 1;
        doReturn(Optional.empty()).when(ownerRepository).findById(ownerId);

        // when
        // then
        assertThrows(OwnerExistenceException.class, () -> underTest.deleteOwnership(catId, ownerId));
    }

    @Test
    public void testGetAllOwners(){
        // given
        Owner owner = mock(Owner.class);
        OwnerDto ownerDto = mock(OwnerDto.class);
        doReturn(List.of(owner)).when(ownerRepository).findAll();
        doReturn(ownerDto).when(mapper).convertToOwnerDto(owner);

        // when
        List<OwnerDto> allOwners = underTest.getAllOwners();

        // then
        assertEquals(List.of(ownerDto), allOwners);
    }
}
