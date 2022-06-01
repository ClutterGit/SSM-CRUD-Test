package com.clutter.service;

import com.clutter.dao.DepartmentMapper;
import com.clutter.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper mapper;
    public List<Department> getDepts() {
        List<Department> departments = mapper.selectByExample(null);
        return departments;
    }
}
