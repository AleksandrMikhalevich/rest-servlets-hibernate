package by.astontrainee.dto;

import java.util.Set;

/**
 * @author Alex Mikhalevich
 */
public record EmployeeResponseDto(Integer id, String name, DepartmentResponseDto department,
                                  Set<ProjectResponseDto> projects) {
}
