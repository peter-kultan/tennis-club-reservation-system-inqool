package cz.services;

import java.util.Collection;

public interface Service<T> {

    Collection<T> getAllEntities();

    Collection<T> getEntity(int id);

    void addEntity(T entity);

    void updateEntity(T entity);

    void deleteEntity(int id);
}
