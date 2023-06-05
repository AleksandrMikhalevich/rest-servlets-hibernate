package by.astontrainee.mappers;

import by.astontrainee.dto.ProjectRequestDto;
import by.astontrainee.dto.ProjectResponseDto;
import by.astontrainee.entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
@Mapper
public interface ProjectMapper {

    static ProjectMapper getInstance() {
        return Mappers.getMapper(ProjectMapper.class);
    }

    ProjectResponseDto toDto(Project project);

    List<ProjectResponseDto> toListDto(List<Project> projects);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Project fromDto(ProjectRequestDto projectRequestDto);
}
