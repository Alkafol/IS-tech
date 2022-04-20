package Dao;

import Models.Cat;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class CatDao implements IDao<Cat> {
    private SessionHandler sessionHandler;

    public CatDao(){
        sessionHandler = new SessionHandler();
    }

    public List<Cat> loadAll() {
        Session session = sessionHandler.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Cat> criteria = builder.createQuery(Cat.class);
        criteria.from(Cat.class);
        List<Cat> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void save(Cat cat){
        sessionHandler.getCurrentSession().save(cat);
    }

    public Cat getById(String id){
        Cat cat = sessionHandler.getCurrentSession().get(Cat.class, id);
        return cat;
    }

    public void delete(Cat cat){
        sessionHandler.getCurrentSession().delete(cat);
    }

    public void update(Cat cat){
        sessionHandler.getCurrentSession().update(cat);
    }

    public SessionHandler getSessionHandler(){
        return sessionHandler;
    }
}
