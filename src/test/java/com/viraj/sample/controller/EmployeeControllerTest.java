package com.viraj.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viraj.sample.entity.Employee;
import com.viraj.sample.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");
        employee.setEmployeeDescription("Senior Developer");

        employee2 = new Employee();
        employee2.setEmployeeId(2L);
        employee2.setEmployeeName("Jane Smith");
        employee2.setEmployeeDescription("Project Manager");
    }

    @Test
    void testGetMessage() throws Exception {
        mockMvc.perform(get("/employee/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello boot"));
    }

    @Test
    void testSaveEmployee() throws Exception {
        when(employeeService.saveEmployee(any())).thenReturn(employee);

        mockMvc.perform(post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.employeeName", is("John Doe")))
                .andExpect(jsonPath("$.employeeDescription", is("Senior Developer")));

        verify(employeeService, times(1)).saveEmployee(any());
    }

    @Test
    void testSaveEmployeeNull() throws Exception {
        mockMvc.perform(post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateEmployee() throws Exception {
        employee.setEmployeeName("John Updated");

        when(employeeService.updateEmployee(any())).thenReturn(employee);

        mockMvc.perform(put("/employee/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.employeeName", is("John Updated")));

        verify(employeeService, times(1)).updateEmployee(any());
    }

    @Test
    void testUpdateEmployeeInvalidId() throws Exception {
        Employee invalidEmployee = new Employee();
        invalidEmployee.setEmployeeId(0L);
        invalidEmployee.setEmployeeName("Test");

        mockMvc.perform(put("/employee/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employees.add(employee2);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeName", is("John Doe")))
                .andExpect(jsonPath("$[1].employeeName", is("Jane Smith")));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetAllEmployeesEmpty() throws Exception {
        List<Employee> employees = new ArrayList<>();

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employee/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employee);

        mockMvc.perform(get("/employee/getone/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.employeeName", is("John Doe")));

        verify(employeeService, times(1)).getEmployee(1L);
    }

    @Test
    void testGetEmployeeNotFound() throws Exception {
        when(employeeService.getEmployee(anyLong())).thenThrow(new NoSuchElementException("Employee not found with ID: 99"));

        mockMvc.perform(get("/employee/getone/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).getEmployee(99L);
    }

    @Test
    void testGetEmployeeInvalidId() throws Exception {
        mockMvc.perform(get("/employee/getone/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyLong());

        mockMvc.perform(delete("/employee/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void testDeleteEmployeeInvalidId() throws Exception {
        mockMvc.perform(delete("/employee/delete/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveEmployeeSuccess() throws Exception {
        when(employeeService.saveEmployee(any())).thenReturn(employee);

        mockMvc.perform(post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated());

        verify(employeeService, times(1)).saveEmployee(any());
    }
}
