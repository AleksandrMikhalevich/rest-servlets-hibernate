package by.astontrainee.dao;

import by.astontrainee.dao.exceptions.DaoException;
import by.astontrainee.entities.Department;
import by.astontrainee.utils.HibernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public class DepartmentDao implements Dao<Department> {

    @Override
    public List<Department> findAll() {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> criteria = criteriaBuilder.createQuery(Department.class);
        Root<Department> allDepartments = criteria.from(Department.class);
        criteria.select(allDepartments);
        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Department findById(Integer id) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Department toFind;
        try {
            entityManager.getTransaction().begin();
            toFind = entityManager.find(Department.class, id);
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return toFind;
    }

    @Override
    public Department update(Department department) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Department updatedDepartment;
        try {
            entityManager.getTransaction().begin();
            updatedDepartment = entityManager.merge(department);
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return updatedDepartment;
    }

    @Override
    public void delete(Integer id) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Department toDelete = entityManager.find(Department.class, id);
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
    public Department save(Department department) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Department savedDepartment;
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(department);
            savedDepartment = entityManager.find(Department.class, department.getId());
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return savedDepartment;
    }
}
