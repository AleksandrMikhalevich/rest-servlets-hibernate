package by.astontrainee.services;

import by.astontrainee.controllers.exceptions.NotFoundException;
import by.astontrainee.dao.Dao;
import by.astontrainee.dao.DepartmentDao;
import by.astontrainee.dao.EmployeeDao;
import by.astontrainee.dao.ProjectDao;
import by.astontrainee.dao.exceptions.DaoException;
import by.astontrainee.dto.EmployeeRequestDto;
import by.astontrainee.dto.EmployeeResponseDto;
import by.astontrainee.entities.Department;
import by.astontrainee.entities.Employee;
import by.astontrainee.entities.Project;
import by.astontrainee.mappers.EmployeeMapper;
import by.astontrainee.services.exceptions.ServiceException;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public class EmployeeServiceImpl implements EmployeeService {

    private final Dao<Employee> employeeDao = new EmployeeDao();

    private final Dao<Department> departmentDao = new DepartmentDao();

    private final Dao<Project> projectDao = new ProjectDao();

    @Override
    public List<EmployeeResponseDto> findAllEmployees() {
        List<EmployeeResponseDto> employees;
        try {
            employees = EmployeeMapper.getInstance().toListDto(employeeDao.findAll());
            if (employees == null) {
                throw new NotFoundException("There are no employees yet");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return employees;
    }

    @Override
    public EmployeeResponseDto findEmployeeById(Integer id) {
        EmployeeResponseDto employee;
        try {
            employee = EmployeeMapper.getInstance().toDto(employeeDao.findById(id));
            if (employee == null) {
                throw new NotFoundException("Employee with id " + id + " not found");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return employee;
    }

    @Override
    public EmployeeResponseDto updateEmployee(EmployeeRequestDto employeeRequestDto, Integer id) {
        Employee updatedEmployee = EmployeeMapper.getInstance().fromDto(employeeRequestDto);
        updatedEmployee.setId(id);
        EmployeeResponseDto employee;
        try {
            Department department = departmentDao.findById(updatedEmployee.getDepartment().getId());
            if (department == null) {
                throw new NotFoundException("Could not update employee: department with id " + updatedEmployee.getDepartment().getId() + " doesn't exist");
            }
            employee = EmployeeMapper.getInstance().toDto(employeeDao.update(updatedEmployee));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return employee;
    }

    @Override
    public void deleteEmployee(Integer id) {
        try {
            employeeDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee savedEmployee = EmployeeMapper.getInstance().fromDto(employeeRequestDto);
        EmployeeResponseDto employee;
        try {
            employee = EmployeeMapper.getInstance().toDto(employeeDao.save(savedEmployee));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return employee;
    }

    public EmployeeResponseDto assignEmployee(Integer id, Integer projectId) {
        Employee assignedEmployee = employeeDao.findById(id);
        Project assignedProject = projectDao.findById(projectId);
        EmployeeResponseDto employee;
        try {
            assignedEmployee.getProjects().add(assignedProject);
            employee = EmployeeMapper.getInstance().toDto(employeeDao.update(assignedEmployee));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return employee;
    }
}
