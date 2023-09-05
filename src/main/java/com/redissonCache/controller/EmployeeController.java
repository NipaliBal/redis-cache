package com.redissonCache.controller;

import com.redissonCache.entity.Employee;
import com.redissonCache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/redis")
public class EmployeeController {

    @Autowired
    private  EmployeeService empService;

    @GetMapping
    @RequestMapping(value="/{id}")
    public Employee getEmployee(@PathVariable final Long id){
        return this.empService.findById(id);
    }

    @GetMapping
    @RequestMapping("/all")
    public List<Employee> getAllEmployee(){
    return this.empService.findAll();
    }

    @PostMapping
    public void save(@RequestBody Employee employee){
        this.empService.save(employee);
    }

    @PutMapping(value="/{id}/name/{newName}")
    public void updateName(@PathVariable final Long id, @PathVariable final String newName){
        this.empService.updateNameForId(id,newName);
    }
}
