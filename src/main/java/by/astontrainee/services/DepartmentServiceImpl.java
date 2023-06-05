package by.astontrainee.services;

import by.astontrainee.controllers.exceptions.NotFoundException;
import by.astontrainee.dao.Dao;
import by.astontrainee.dao.DepartmentDao;
import by.astontrainee.dao.exceptions.DaoException;
import by.astontrainee.dto.DepartmentRequestDto;
import by.astontrainee.dto.DepartmentResponseDto;
import by.astontrainee.entities.Department;
import by.astontrainee.mappers.DepartmentMapper;
import by.astontrainee.services.exceptions.ServiceException;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public class DepartmentServiceImpl implements DepartmentService {

    private final Dao<Department> departmentDao = new DepartmentDao();

    @Override
    public List<DepartmentResponseDto> findAllDepartments() {
        List<DepartmentResponseDto> departments;
        try {
            departments = DepartmentMapper.getInstance().toListDto(departmentDao.findAll());
            if(departments == null) {
                throw new NotFoundException("There are no departments yet");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return departments;
    }

    @Override
    public DepartmentResponseDto findDepartmentById(Integer id) {
        DepartmentResponseDto department;
        try {
            department = DepartmentMapper.getInstance().toDto(departmentDao.findById(id));
            if (department == null) {
                throw new NotFoundException("Department with id " + id + " not found");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return department;
    }

    @Override
    public DepartmentResponseDto updateDepartment(DepartmentRequestDto departmentRequestDto, Integer id) {
        Department updatedDepartment = DepartmentMapper.getInstance().fromDto(departmentRequestDto);
        updatedDepartment.setId(id);
        DepartmentResponseDto department;
        try {
            department = DepartmentMapper.getInstance().toDto(departmentDao.update(updatedDepartment));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return department;
    }

    @Override
    public void deleteDepartment(Integer id) {
        try {
            departmentDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public DepartmentResponseDto saveDepartment(DepartmentRequestDto departmentRequestDto) {
        Department savedDepartment = DepartmentMapper.getInstance().fromDto(departmentRequestDto);
        DepartmentResponseDto department;
        try {
            department = DepartmentMapper.getInstance().toDto(departmentDao.save(savedDepartment));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return department;
    }
}
