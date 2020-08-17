package com.example.mybatis.mybatisdemo.controller;

import com.example.mybatis.mybatisdemo.model.DepartmentDO;
import com.example.mybatis.mybatisdemo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangrenzhe
 * @date 2020/8/2 23:11
 */
@RestController
public class DeptController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/dept/{id}")
    public DepartmentDO getDept(@PathVariable Integer id) {

        return departmentService.getById(id);
    }

    @GetMapping("/deptByName/{name}")
    public List<DepartmentDO> getDeptName(@PathVariable String name) {

        return departmentService.getDepByName(name);
    }

    @GetMapping("/deptUpdate/{id}")
    public DepartmentDO updateDepById(@PathVariable Integer id) {
        return departmentService.updateById(id);
    }

    @GetMapping("/deptDelete/{id}")
    public String delDepById(@PathVariable Integer id) {
        departmentService.delDep(id);
        return "success";
    }
}
