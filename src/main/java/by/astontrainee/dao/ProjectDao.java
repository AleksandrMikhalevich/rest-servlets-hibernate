package by.astontrainee.dao;

import by.astontrainee.dao.exceptions.DaoException;
import by.astontrainee.entities.Employee;
import by.astontrainee.entities.Project;
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
public class ProjectDao implements Dao<Project> {

    @Override
    public List<Project> findAll() {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteria = criteriaBuilder.createQuery(Project.class);
        Root<Project> allProjects = criteria.from(Project.class);
        criteria.select(allProjects);
        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Project findById(Integer id) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Project toFind;
        try {
            entityManager.getTransaction().begin();
            toFind = entityManager.find(Project.class, id);
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
    public Project update(Project project) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Project updatedProject;
        try {
            entityManager.getTransaction().begin();
            updatedProject = entityManager.merge(project);
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return updatedProject;
    }

    @Override
    public void delete(Integer id) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Project toDelete = entityManager.find(Project.class, id);
            for (Employee employee : toDelete.getEmployees()) {
                toDelete.removeEmployee(employee);
            }
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
    public Project save(Project project) {
        EntityManager entityManager = HibernateUtils.getEntityManager();
        Project savedProject;
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(project);
            savedProject = entityManager.find(Project.class, project.getId());
            entityManager.getTransaction().commit();
        } catch (HibernateException e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(e.getMessage());
        } finally {
            entityManager.close();
        }
        return savedProject;
    }
}
