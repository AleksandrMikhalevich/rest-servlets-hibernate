package by.astontrainee.dao;

import by.astontrainee.dao.exceptions.DaoException;
import by.astontrainee.entities.Employee;
import by.astontrainee.utils.HibernateUtils;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public class EmployeeDao implements Dao<Employee> {

    @Override
    public List<Employee> findAll() {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        EntityGraph<Employee> entityGraph = entityManager.createEntityGraph(Employee.class);
        entityGraph.addAttributeNodes("projects", "department");
        TypedQuery<Employee> query = entityManager.createQuery("SELECT a FROM Employee a", Employee.class)
                .setHint("jakarta.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Employee findById(Integer id) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        return entityManager.find(Employee.class, id);
    }

    @Override
    public Employee update(Employee employee) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Employee updatedEmployee;
        try {
            entityManager.getTransaction().begin();
            updatedEmployee = entityManager.merge(employee);
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return updatedEmployee;
    }

    @Override
    public void delete(Integer id) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Employee toDelete = entityManager.find(Employee.class, id);
            entityManager.remove(toDelete);
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Employee save(Employee employee) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Employee savedEmployee;
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(employee);
            savedEmployee = entityManager.find(Employee.class, employee.getId());
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return savedEmployee;
    }
}
