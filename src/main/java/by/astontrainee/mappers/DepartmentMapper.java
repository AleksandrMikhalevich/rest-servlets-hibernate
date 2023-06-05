package by.astontrainee.mappers;

import by.astontrainee.dto.DepartmentRequestDto;
import by.astontrainee.dto.DepartmentResponseDto;
import by.astontrainee.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
@Mapper
public interface DepartmentMapper {

    static DepartmentMapper getInstance() {
        return Mappers.getMapper(DepartmentMapper.class);
    }

    DepartmentResponseDto toDto(Department department);

    List<DepartmentResponseDto> toListDto(List<Department> departments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department fromDto(DepartmentRequestDto departmentRequestDto);
}
