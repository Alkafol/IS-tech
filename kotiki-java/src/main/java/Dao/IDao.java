package Dao;

import java.util.List;

public interface IDao<T> {
    public void save(T t);
    public void delete(T t);
    public List<T> loadAll();
    public T getById(String id);
    public SessionHandler getSessionHandler();
    public void update(T t);
}
