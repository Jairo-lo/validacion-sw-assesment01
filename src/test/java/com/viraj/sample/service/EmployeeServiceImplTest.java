package com.viraj.sample.service;

import com.viraj.sample.entity.Employee;
import com.viraj.sample.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

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
    void testSaveEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.saveEmployee(employee);

        assertNotNull(result);
        assertEquals(employee.getEmployeeId(), result.getEmployeeId());
        assertEquals(employee.getEmployeeName(), result.getEmployeeName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testSaveEmployeeNull() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.saveEmployee(null));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employee.setEmployeeName("John Updated");
        Employee result = employeeService.updateEmployee(employee);

        assertNotNull(result);
        assertEquals("John Updated", result.getEmployeeName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployeeNullId() {
        Employee emp = new Employee();
        emp.setEmployeeId(0L);
        emp.setEmployeeName("Test");

        assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(emp));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employees.add(employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(employee.getEmployeeName(), result.get(0).getEmployeeName());
        assertEquals(employee2.getEmployeeName(), result.get(1).getEmployeeName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEmployeesEmpty() {
        List<Employee> employees = new ArrayList<>();

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployee(1L);

        assertNotNull(result);
        assertEquals(employee.getEmployeeId(), result.getEmployeeId());
        assertEquals(employee.getEmployeeName(), result.getEmployeeName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeNotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(99L));
        verify(employeeRepository, times(1)).findById(99L);
    }

    @Test
    void testGetEmployeeNullId() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployee(null));
        verify(employeeRepository, never()).findById(anyLong());
    }

    @Test
    void testGetEmployeeInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployee(0L));
        verify(employeeRepository, never()).findById(anyLong());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(anyLong());

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeNullId() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.deleteEmployee(null));
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployeeInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.deleteEmployee(0L));
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateEmployeeDescription() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employee.setEmployeeDescription("Updated Description");
        Employee result = employeeService.updateEmployee(employee);

        assertNotNull(result);
        assertEquals("Updated Description", result.getEmployeeDescription());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
}
