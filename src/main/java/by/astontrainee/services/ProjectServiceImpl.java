package by.astontrainee.services;

import by.astontrainee.controllers.exceptions.NotFoundException;
import by.astontrainee.dao.Dao;
import by.astontrainee.dao.ProjectDao;
import by.astontrainee.dao.exceptions.DaoException;
import by.astontrainee.dto.ProjectRequestDto;
import by.astontrainee.dto.ProjectResponseDto;
import by.astontrainee.entities.Project;
import by.astontrainee.mappers.ProjectMapper;
import by.astontrainee.services.exceptions.ServiceException;

import java.util.List;

/**
 * @author Alex Mikhalevich
 */
public class ProjectServiceImpl implements ProjectService {

    private final Dao<Project> projectDao = new ProjectDao();

    @Override
    public List<ProjectResponseDto> findAllProjects() {
        List<ProjectResponseDto> projects;
        try {
            projects = ProjectMapper.getInstance().toListDto(projectDao.findAll());
            if (projects == null) {
                throw new NotFoundException("There are no projects yet");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return projects;
    }

    @Override
    public ProjectResponseDto findProjectById(Integer id) {
        ProjectResponseDto project;
        try {
            project = ProjectMapper.getInstance().toDto(projectDao.findById(id));
            if (project == null) {
                throw new NotFoundException("Project with id " + id + " not found");
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return project;
    }

    @Override
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Integer id) {
        Project updatedProject = ProjectMapper.getInstance().fromDto(projectRequestDto);
        updatedProject.setId(id);
        ProjectResponseDto project;
        try {
            project = ProjectMapper.getInstance().toDto(projectDao.update(updatedProject));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return project;
    }

    @Override
    public void deleteProject(Integer id) {
        try {
            projectDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ProjectResponseDto saveProject(ProjectRequestDto projectRequestDto) {
        Project savedProject = ProjectMapper.getInstance().fromDto(projectRequestDto);
        ProjectResponseDto project;
        try {
            project = ProjectMapper.getInstance().toDto(projectDao.save(savedProject));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        return project;
    }
}
