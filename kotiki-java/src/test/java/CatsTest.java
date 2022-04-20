import Controller.CatsController;
import Dao.CatDao;
import Dao.IDao;
import Dao.OwnerDao;
import Dao.SessionHandler;
import Models.Cat;
import Models.Color;
import Models.Owner;
import Service.CatsService;
import Service.OwnersService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatsTest {

    @Test
    public void additionTest() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 26);

        IDao<Cat> catDao = Mockito.mock(IDao.class);
        CatsService catsService = new CatsService(catDao);

        Cat cat = new Cat("Barsik", cal, "Scottish fold", Color.grey, null);
        String id = cat.getId();

        Mockito.when(catDao.getById(id)).thenReturn(cat);
        Mockito.when(catDao.getSessionHandler()).thenReturn(new SessionHandler());

        Cat foundCat = catsService.getCatById(id);

        Mockito.verify(catDao).getById(id);

        assertEquals(cat, foundCat);
    }

   @Test
    public void friendshipTest(){

        IDao<Cat> catDao = Mockito.mock(IDao.class);
        IDao<Owner> ownerDao = new OwnerDao();
        CatsService catsService = new CatsService(catDao);
        OwnersService ownersService = new OwnersService(ownerDao);
        CatsController catsController = new CatsController(catsService, ownersService);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 26);
        Cat firstCat = new Cat("Barsik", cal, "Scottish fold", Color.grey, null);
        String firstCatId = firstCat.getId();

        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        Cat secondCat = new Cat("Simon", cal, "Persian", Color.black, null);
        String secondCatId = secondCat.getId();

        Mockito.when(catDao.getById(firstCatId)).thenReturn(firstCat);
        Mockito.when(catDao.getById(secondCatId)).thenReturn(secondCat);
        Mockito.when(catDao.getSessionHandler()).thenReturn(new SessionHandler());

        catsController.startFriendship(firstCatId, secondCatId);

        Mockito.verify(catDao).getById(firstCatId);
        Mockito.verify(catDao).getById(secondCatId);
        Mockito.verify(catDao).update(firstCat);
        Mockito.verify(catDao).update(secondCat);

        assertEquals(true, firstCat.getFriends().contains(secondCat));
        assertEquals(true, secondCat.getFriends().contains(firstCat));
    }




}