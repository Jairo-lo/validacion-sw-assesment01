package com.viraj.sample.repository;

import com.viraj.sample.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeName("Test Employee");
        employee.setEmployeeDescription("Test Description");
    }

    @Test
    void testSaveEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee);
        assertNotNull(savedEmployee.getEmployeeId());
        assertEquals("Test Employee", savedEmployee.getEmployeeName());
        assertEquals("Test Description", savedEmployee.getEmployeeDescription());
    }

    @Test
    void testFindEmployeeById() {
        Employee savedEmployee = employeeRepository.save(employee);

        Employee foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId()).orElse(null);

        assertNotNull(foundEmployee);
        assertEquals(savedEmployee.getEmployeeId(), foundEmployee.getEmployeeId());
        assertEquals("Test Employee", foundEmployee.getEmployeeName());
    }

    @Test
    void testFindEmployeeByIdNotFound() {
        boolean exists = employeeRepository.findById(9999L).isPresent();

        assertFalse(exists);
    }

    @Test
    void testDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        Long employeeId = savedEmployee.getEmployeeId();

        employeeRepository.deleteById(employeeId);
        boolean exists = employeeRepository.findById(employeeId).isPresent();

        assertFalse(exists);
    }

    @Test
    void testFindAllEmployees() {
        Employee employee2 = new Employee();
        employee2.setEmployeeName("Another Employee");
        employee2.setEmployeeDescription("Another Description");

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        Iterable<Employee> employees = employeeRepository.findAll();
        long count = 0;
        for (Employee emp : employees) {
            count++;
        }

        assertTrue(count >= 2);
    }

    @Test
    void testUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);

        savedEmployee.setEmployeeName("Updated Employee");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertEquals("Updated Employee", updatedEmployee.getEmployeeName());
        assertEquals(savedEmployee.getEmployeeId(), updatedEmployee.getEmployeeId());
    }
}
