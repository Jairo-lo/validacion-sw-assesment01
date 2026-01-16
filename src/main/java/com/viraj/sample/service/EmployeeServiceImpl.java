package com.viraj.sample.service;

import com.viraj.sample.entity.Employee;
import com.viraj.sample.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (employee.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("Employee ID must be valid");
        }
        return employeeRepository.save(employee);
    }


    @Override
    public List<Employee> getAllEmployees() {
        return (List<Employee>) employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(Long employeeId) {
        if (employeeId == null || employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be valid");
        }
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.orElseThrow(() -> new NoSuchElementException("Employee not found with ID: " + employeeId));
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (employeeId == null || employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be valid");
        }
        employeeRepository.deleteById(employeeId);
    }
}
