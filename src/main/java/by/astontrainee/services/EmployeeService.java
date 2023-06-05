package by.astontrainee.services;

import by.astontrainee.dto.EmployeeRequestDto;
import by.astontrainee.dto.EmployeeResponseDto;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public interface EmployeeService {

    List<EmployeeResponseDto> findAllEmployees();

    EmployeeResponseDto findEmployeeById(Integer id);

    EmployeeResponseDto updateEmployee(EmployeeRequestDto departmentRequestDto, Integer id);

    void deleteEmployee(Integer id);

    EmployeeResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto);
}
