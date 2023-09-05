package com.redissonCache.repository;

import com.redissonCache.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    List<Employee> findByName(final String name);
    Employee findById(long id);
}
