package Service;

import Dao.IDao;
import Models.Cat;
import Models.Owner;
import Models.Color;
import Tools.CatExistenceException;
import Tools.FriendshipException;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class CatsService {
    private IDao<Cat> _catDao;

    public CatsService(IDao<Cat> catDAO){
        _catDao = catDAO;
    }

    public String addCat(String name, Calendar dateOfBirth, String breed, Color color, Owner owner){
        Cat cat = new Cat(name, dateOfBirth, breed, color, owner);

        owner.addCat(cat);
        cat.setOwner(owner);

        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        _catDao.save(cat);
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();

        return cat.getId();
    }

    public Cat getCatById(String id){
        _catDao.getSessionHandler().openCurrentSession();
        Cat cat = _catDao.getById(id);
        _catDao.getSessionHandler().closeCurrentSession();
        return cat;
    }

    public void deleteCatById(String id) throws CatExistenceException {
        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        Cat cat = _catDao.getById(id);
        if (cat == null){
            throw new CatExistenceException();
        }
        _catDao.delete(cat);
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();
    }

    public void startFriendship(String firstCatId, String secondCatId) throws FriendshipException {
        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        Cat firstCat = _catDao.getById(firstCatId);
        Cat secondCat = _catDao.getById(secondCatId);
        if (firstCat == null || secondCat == null){
            throw new FriendshipException();
        }
        firstCat.addFriend(secondCat);
        secondCat.addFriend(firstCat);
        _catDao.update(firstCat);
        _catDao.update(secondCat);
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();
    }

    public void stopFriendship(String firstCatId, String secondCatId) throws FriendshipException {
        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        Cat firstCat = _catDao.getById(firstCatId);
        Cat secondCat = _catDao.getById(secondCatId);
        firstCat.removeFriend(secondCat);
        secondCat.removeFriend(firstCat);
        if (firstCat == null || secondCat == null){
            throw new FriendshipException();
        }
        _catDao.update(firstCat);
        _catDao.update(secondCat);
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();
    }

    public Owner getOwnerOfCat(String catId) throws CatExistenceException {
        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        Cat cat = _catDao.getById(catId);
        if (cat == null){
            throw new CatExistenceException();
        }
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();
        return cat.getOwner();
    }

    public List<Cat> getAllCats(){
        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        List<Cat> allCats = _catDao.loadAll();
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();
        return allCats;
    }


    public Set<Cat> getAllCatFriends(String catId) throws CatExistenceException {
        _catDao.getSessionHandler().openCurrentSessionwithTransaction();
        Cat cat = _catDao.getById(catId);
        if (cat == null){
            throw new CatExistenceException();
        }
        Set<Cat> allFriends = _catDao.getById(catId).getFriends();
        _catDao.getSessionHandler().closeCurrentSessionwithTransaction();
        return  allFriends;
    }
}
