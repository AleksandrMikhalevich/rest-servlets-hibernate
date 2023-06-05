package by.astontrainee.dao;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public interface Dao<T> {

    List<T> findAll();

    T findById(Integer id);

    T update(T t);

    void delete(Integer id);

    T save(T t);
}
