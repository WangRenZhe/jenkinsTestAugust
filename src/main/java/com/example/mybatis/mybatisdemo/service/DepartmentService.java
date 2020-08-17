package com.example.mybatis.mybatisdemo.service;

import com.example.mybatis.mybatisdemo.dao.DepartmentDOMapper;
import com.example.mybatis.mybatisdemo.model.DepartmentDO;
import com.example.mybatis.mybatisdemo.model.DepartmentDOExample;
import com.example.mybatis.mybatisdemo.util.CriteriaSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangrenzhe
 * @date 2020/8/2 23:07
 */
@Service
public class DepartmentService {
    @Autowired
    private DepartmentDOMapper departmentDOMapper;

    @Cacheable(cacheNames = "department")
    public DepartmentDO getById(Integer id) {
        System.out.println("进来查询开发部门");
        return departmentDOMapper.selectByPrimaryKey(id);
    }

    @CachePut(cacheNames = "department")
    public DepartmentDO updateById(Integer id) {

        DepartmentDO current = departmentDOMapper.selectByPrimaryKey(id);
        current.setDepartmentName(current.getDepartmentName() + "update");
        System.out.println("update dep" + id);
        departmentDOMapper.updateByPrimaryKey(current);
        return current;
    }

    @CacheEvict(cacheNames = "department", allEntries = true)
    public void delDep(Integer id) {
        departmentDOMapper.deleteByPrimaryKey(id);
    }

    @Caching(cacheable = {@Cacheable(cacheNames = "departmentHaha", key = "#name")},
        put = {@CachePut(value = "departmentHaha", key = "#result[0].id"),
            @CachePut(value = "departmentHaha", key = "#result[0].departmentName")})
    public List<DepartmentDO> getDepByName(String name) {
        DepartmentDOExample departmentDOExample = new DepartmentDOExample();
        departmentDOExample.createCriteria().andDepartmentNameEqualTo(name);
        return departmentDOMapper.selectByExample(departmentDOExample);
    }

}
