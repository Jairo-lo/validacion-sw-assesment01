package com.viraj.sample.controller;

import com.viraj.sample.entity.Employee;
import com.viraj.sample.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(path = "/hello")
    public String getMessage() {
        return "Hello boot";
    }

    @PostMapping("/save")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        if (employee == null) {
            return ResponseEntity.badRequest().build();
        }
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/update")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        if (employee == null || employee.getEmployeeId() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/getone/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        if (employeeId == null || employeeId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Employee employee = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        if (employeeId == null || employeeId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
