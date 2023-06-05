package by.astontrainee.services;

import by.astontrainee.dto.ProjectRequestDto;
import by.astontrainee.dto.ProjectResponseDto;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public interface ProjectService {

    List<ProjectResponseDto> findAllProjects();

    ProjectResponseDto findProjectById(Integer id);

    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Integer id);

    void deleteProject(Integer id);

    ProjectResponseDto saveProject(ProjectRequestDto projectRequestDto);
}
