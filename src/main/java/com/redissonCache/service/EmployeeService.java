package com.redissonCache.service;

import com.redissonCache.entity.Employee;
import com.redissonCache.repository.EmployeeRepository;
import org.redisson.api.RMapCache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository empRepository;

    /*@Autowired
    private RedissonClient redissonClient;*/

    @Autowired
    private RMapCache<Long, Employee> rMapCache;



   public  Employee save(final Employee employee){
       this.rMapCache.put(employee.getId(), employee);
       return employee;
   }


    public List<Employee>  findAll(){
        List<Employee> empList= new ArrayList<>();
        this.empRepository.findAll().forEach(empList::add);
        return empList;
    }

    public Employee  findById(final long id){
        return this.rMapCache.get(id);
    }

    public List<Employee> findByName(final String name){
       return this.empRepository.findByName(name);
    }


    public void updateNameForId(final long id, final String name) {
        final Employee employee = this.empRepository.findById(id);
        employee.setName(name);
        this.empRepository.save(employee);
    }
}
