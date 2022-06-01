package com.techprog.upgradedcats;

import com.techprog.upgradedcats.dao.CatDAO;
import com.techprog.upgradedcats.dao.OwnerDAO;
import com.techprog.upgradedcats.dto.CatDto;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.service.CatsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UpgradedcatsApplicationTests {
    
    @Test
    public void additionTest() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 26);

        CatDAO catDAO = Mockito.mock(CatDAO.class);
        OwnerDAO ownerDAO = Mockito.mock(OwnerDAO.class);
        CatsService catsService = new CatsService(catDAO, ownerDAO);

        Cat cat = new Cat("Barsik", cal, "Scottish fold", Color.grey, null);
        Optional<Cat> optionalCat = Optional.of(cat);
        String id = cat.getId();
        Mockito.when(catDAO.findById(id)).thenReturn(optionalCat);

        CatDto catDto = catsService.convertToCatDto(cat);

        CatDto foundCat = catsService.getCatById(id);

        assertEquals(catDto.getId(), foundCat.getId());
    }
}
