package Service;

import Dao.IDao;
import Dao.OwnerDao;
import Models.Cat;
import Models.Owner;
import Tools.OwnerExistenceException;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class OwnersService {
    private IDao<Owner> _ownerDao;

    public OwnersService(IDao<Owner> ownerDAO){
        _ownerDao = ownerDAO;
    }

    public Owner addOwner(String name, Calendar dateOfBirth){
        Owner owner = new Owner(name, dateOfBirth);
        _ownerDao.getSessionHandler().openCurrentSessionwithTransaction();
        _ownerDao.save(owner);
        _ownerDao.getSessionHandler().closeCurrentSessionwithTransaction();
        return owner;
    }

    public Owner getOwnerById(String id){
        _ownerDao.getSessionHandler().openCurrentSession();
        Owner owner = _ownerDao.getById(id);
        _ownerDao.getSessionHandler().closeCurrentSession();
        return owner;
    }

    public void deleteOwnerById(String id) throws OwnerExistenceException {
        _ownerDao.getSessionHandler().openCurrentSessionwithTransaction();
        Owner owner = _ownerDao.getById(id);
        if (owner == null){
            throw new OwnerExistenceException();
        }
        _ownerDao.delete(owner);
        _ownerDao.getSessionHandler().closeCurrentSessionwithTransaction();
    }

    public Set<Cat> getAllCatsOfOwner(String ownerId) throws OwnerExistenceException {
        _ownerDao.getSessionHandler().openCurrentSessionwithTransaction();
        Owner owner = _ownerDao.getById(ownerId);
        if (owner == null){
            throw new OwnerExistenceException();
        }
        _ownerDao.getSessionHandler().closeCurrentSessionwithTransaction();
        return owner.getCats();
    }

    public List<Owner> getAllOwners(){
        _ownerDao.getSessionHandler().openCurrentSessionwithTransaction();
        List<Owner> allOwners = _ownerDao.loadAll();
        _ownerDao.getSessionHandler().closeCurrentSessionwithTransaction();
        return allOwners;
    }
}
