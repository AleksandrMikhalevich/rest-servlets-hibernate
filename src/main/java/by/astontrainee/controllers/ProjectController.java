package by.astontrainee.controllers;

import by.astontrainee.controllers.exceptions.BadRequestException;
import by.astontrainee.controllers.exceptions.NotFoundException;
import by.astontrainee.dto.ProjectRequestDto;
import by.astontrainee.dto.ProjectResponseDto;
import by.astontrainee.services.ProjectService;
import by.astontrainee.services.ProjectServiceImpl;
import by.astontrainee.services.exceptions.ServiceException;
import by.astontrainee.utils.JsonReaderUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author Alex Mikhalevich
 */
@WebServlet(value = "/project/*")
public class ProjectController extends HttpServlet {

    private final ProjectService projectService = new ProjectServiceImpl();

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String json;
        if (uri.substring("/project/".length()).isEmpty()) {
            json = GSON.toJson(projectService.findAllProjects());
            resp.setStatus(200);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/project/".length()));
                json = GSON.toJson(projectService.findProjectById(id));
                resp.setStatus(200);
            } catch (NotFoundException e) {
                json = GSON.toJson(e.getMessage());
                resp.setStatus(404);
            } catch (NumberFormatException e) {
                json = GSON.toJson("Incorrect id provided!");
                resp.setStatus(400);
            }
        }
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String responceJson;
        if (!uri.substring("/project/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            responceJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            String json = JsonReaderUtils.readInputStream(req.getInputStream());
            ProjectRequestDto project = GSON.fromJson(json, ProjectRequestDto.class);
            try {
                ProjectResponseDto createdProject = projectService.saveProject(project);
                responceJson = GSON.toJson(createdProject);
                resp.setStatus(201);
            } catch (ServiceException e) {
                responceJson = GSON.toJson(e.getMessage());
                resp.setStatus(500);
            } catch (JsonSyntaxException e) {
                responceJson = GSON.toJson("Incorrect data provided!");
                resp.setStatus(400);
            }
        }
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(responceJson);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String resultJson;
        ProjectRequestDto project;
        if (uri.substring("/project/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            resultJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/project/".length()));
                String json = JsonReaderUtils.readInputStream(req.getInputStream());
                project = GSON.fromJson(json, ProjectRequestDto.class);
                ProjectResponseDto updatedProject = projectService.updateProject(project, id);
                resultJson = GSON.toJson(updatedProject);
                resp.setStatus(200);
            } catch (ServiceException e) {
                resultJson = GSON.toJson(e.getMessage());
                resp.setStatus(500);
            } catch (NumberFormatException | JsonSyntaxException e) {
                resultJson = GSON.toJson("Incorrect id provided!");
                resp.setStatus(400);
            }
        }
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(resultJson);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String json;
        if (uri.substring("/project/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("No id provided!");
            json = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/project/".length()));
                projectService.deleteProject(id);
                json = GSON.toJson("Project was deleted successfully!");
                resp.setStatus(200);
            } catch (ServiceException e) {
                json = GSON.toJson(e.getMessage());
                resp.setStatus(500);
            } catch (NumberFormatException e) {
                json = GSON.toJson("Incorrect id provided!");
                resp.setStatus(400);
            }
        }
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(json);
    }
}
