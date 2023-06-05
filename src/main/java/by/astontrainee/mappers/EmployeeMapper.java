package by.astontrainee.mappers;

import by.astontrainee.dto.EmployeeRequestDto;
import by.astontrainee.dto.EmployeeResponseDto;
import by.astontrainee.entities.Department;
import by.astontrainee.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
@Mapper
public interface EmployeeMapper {

    static EmployeeMapper getInstance() {
        return Mappers.getMapper(EmployeeMapper.class);
    }

    EmployeeResponseDto toDto(Employee employee);

    List<EmployeeResponseDto> toListDto(List<Employee> employees);

    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "id", ignore = true)
    Employee fromDto(EmployeeRequestDto employeeRequestDto);

    default Department map(Integer id) {
        Department department = new Department();
        department.setId(id);
        return id != null ? department : null;
    }
}
