package by.astontrainee.controllers;

import by.astontrainee.controllers.exceptions.BadRequestException;
import by.astontrainee.controllers.exceptions.NotFoundException;
import by.astontrainee.dto.DepartmentRequestDto;
import by.astontrainee.dto.DepartmentResponseDto;
import by.astontrainee.services.DepartmentService;
import by.astontrainee.services.DepartmentServiceImpl;
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
@WebServlet(value = "/department/*")
public class DepartmentController extends HttpServlet {

    private final DepartmentService departmentService = new DepartmentServiceImpl();

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String json;
        if (uri.substring("/department/".length()).isEmpty()) {
            json = GSON.toJson(departmentService.findAllDepartments());
            resp.setStatus(200);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/department/".length()));
                json = GSON.toJson(departmentService.findDepartmentById(id));
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
        if (!uri.substring("/department/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            responceJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            String json = JsonReaderUtils.readInputStream(req.getInputStream());
            DepartmentRequestDto department = GSON.fromJson(json, DepartmentRequestDto.class);
            try {
                DepartmentResponseDto createdDepartment = departmentService.saveDepartment(department);
                responceJson = GSON.toJson(createdDepartment);
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
        DepartmentRequestDto department;
        if (uri.substring("/department/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            resultJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/department/".length()));
                String json = JsonReaderUtils.readInputStream(req.getInputStream());
                department = GSON.fromJson(json, DepartmentRequestDto.class);
                DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(department, id);
                resultJson = GSON.toJson(updatedDepartment);
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
        if (uri.substring("/department/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("No id provided!");
            json = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/department/".length()));
                departmentService.deleteDepartment(id);
                json = GSON.toJson("Department was deleted successfully!");
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
