package by.astontrainee.controllers;

import by.astontrainee.controllers.exceptions.BadRequestException;
import by.astontrainee.controllers.exceptions.NotFoundException;
import by.astontrainee.dto.EmployeeRequestDto;
import by.astontrainee.dto.EmployeeResponseDto;
import by.astontrainee.services.EmployeeService;
import by.astontrainee.services.EmployeeServiceImpl;
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
@WebServlet(value = "/employee/*")
public class EmployeeController extends HttpServlet {

    private final EmployeeService employeeService = new EmployeeServiceImpl();

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String json;
        if (uri.substring("/employee/".length()).isEmpty()) {
            json = GSON.toJson(employeeService.findAllEmployees());
            resp.setStatus(200);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/employee/".length()));
                json = GSON.toJson(employeeService.findEmployeeById(id));
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
        if (!uri.substring("/employee/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            responceJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                String json = JsonReaderUtils.readInputStream(req.getInputStream());
                EmployeeRequestDto employee = GSON.fromJson(json, EmployeeRequestDto.class);
                EmployeeResponseDto createdEmployee = employeeService.saveEmployee(employee);
                responceJson = GSON.toJson(createdEmployee);
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
        EmployeeRequestDto employee;
        if (uri.substring("/employee/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            resultJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/employee/".length()));
                String json = JsonReaderUtils.readInputStream(req.getInputStream());
                employee = GSON.fromJson(json, EmployeeRequestDto.class);
                EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(employee, id);
                resultJson = GSON.toJson(updatedEmployee);
                resp.setStatus(200);
            } catch (ServiceException | NotFoundException e) {
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
        if (uri.substring("/employee/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("No id provided!");
            json = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            try {
                Integer id = Integer.parseInt(uri.substring("/employee/".length()));
                employeeService.deleteEmployee(id);
                json = GSON.toJson("Employee was deleted successfully!");
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
