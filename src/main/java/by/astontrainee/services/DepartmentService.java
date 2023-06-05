package by.astontrainee.services;

import by.astontrainee.dto.DepartmentRequestDto;
import by.astontrainee.dto.DepartmentResponseDto;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public interface DepartmentService {

    List<DepartmentResponseDto> findAllDepartments();

    DepartmentResponseDto findDepartmentById(Integer id);

    DepartmentResponseDto updateDepartment(DepartmentRequestDto departmentRequestDto, Integer id);

    void deleteDepartment(Integer id);

    DepartmentResponseDto saveDepartment(DepartmentRequestDto departmentRequestDto);
}
