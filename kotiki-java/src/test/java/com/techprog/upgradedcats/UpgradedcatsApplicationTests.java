package com.techprog.upgradedcats;

import com.techprog.upgradedcats.dao.CatRepository;
import com.techprog.upgradedcats.dao.OwnerRepository;
import com.techprog.upgradedcats.dao.UserRepository;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.repository.CatDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.service.CatsService;
import com.techprog.upgradedcats.tools.CatOwnershipException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Optional;

import static com.techprog.upgradedcats.security.ApplicationUserRole.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UpgradedcatsApplicationTests {
    
    @Test
    public void additionTest() throws CatOwnershipException {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 26);

        CatRepository catRepository = Mockito.mock(CatRepository.class);
        OwnerRepository ownerRepository = Mockito.mock(OwnerRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        CatsService catsService = new CatsService(catRepository, ownerRepository, userRepository, passwordEncoder);

        User user = new User("anton", "1234", ADMIN);
        Owner owner = new Owner("Anton", cal, user);
        Cat cat = new Cat("Barsik", cal, "Scottish fold", Color.grey, owner);
        Optional<Cat> optionalCat = Optional.of(cat);
        String id = cat.getId();
        Mockito.when(catRepository.findById(id)).thenReturn(optionalCat);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        CatDto catDto = catsService.convertToCatDto(cat);

        CatDto foundCat = catsService.getCatById(id, user.getUsername());

        assertEquals(catDto.getId(), foundCat.getId());
    }
}
