package by.astontrainee.controllers;

import by.astontrainee.controllers.exceptions.BadRequestException;
import by.astontrainee.dto.AssignmentDto;
import by.astontrainee.dto.EmployeeResponseDto;
import by.astontrainee.services.EmployeeServiceImpl;
import by.astontrainee.services.exceptions.ServiceException;
import by.astontrainee.utils.JsonReaderUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author Alex Mikhalevich
 */
@WebServlet(value = "/assignment/employee/*")
public class AssignmentController extends HttpServlet {

    private final EmployeeServiceImpl employeeService = new EmployeeServiceImpl();

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String resultJson;
        if (uri.substring("/assignment/employee/".length()).isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("Cannot process the request");
            resultJson = GSON.toJson(badRequestException.getMessage());
            resp.setStatus(400);
        } else {
            Integer id = Integer.parseInt(uri.substring("/assignment/employee/".length()));
            String json = JsonReaderUtils.readInputStream(req.getInputStream());
            AssignmentDto project = GSON.fromJson(json, AssignmentDto.class);
            try {
                EmployeeResponseDto assignedEmployee = employeeService.assignEmployee(id, project.projectId());
                resultJson = GSON.toJson(assignedEmployee);
                resp.setStatus(200);
            } catch (ServiceException e) {
                resultJson = GSON.toJson(e.getMessage());
                resp.setStatus(500);
            }
        }
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(resultJson);
    }
}
