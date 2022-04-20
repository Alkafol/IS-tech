package Dao;

import Models.Cat;
import Models.Owner;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class OwnerDao implements IDao<Owner>{
    private SessionHandler sessionHandler;

    public OwnerDao(){
        sessionHandler = new SessionHandler();
    }

    public List<Owner> loadAll() {
        Session session = sessionHandler.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Owner> criteria = builder.createQuery(Owner.class);
        criteria.from(Owner.class);
        List<Owner> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void save(Owner owner){
        sessionHandler.getCurrentSession().persist(owner);
    }

    public Owner getById(String id){
        return sessionHandler.getCurrentSession().get(Owner.class, id);
    }

    public void delete(Owner owner){
        sessionHandler.getCurrentSession().delete(owner);
    }

    public void update(Owner owner){
        sessionHandler.getCurrentSession().update(owner);
    }

    public SessionHandler getSessionHandler(){
        return sessionHandler;
    }
}
