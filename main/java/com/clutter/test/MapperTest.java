package com.clutter.test;

import com.clutter.dao.DepartmentMapper;
import com.clutter.dao.EmployeeMapper;
import com.clutter.pojo.Employee;
import com.clutter.pojo.EmployeeExample;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;


/**
 * 测试dao层的工作
 * 推荐spring的项目可以使用Spring的单元测试，可以自动注入我们需要的组件
 *  1.导入springTest模块
 *  2.@ContextConfiguration指定Spring配置文件的位置
 *  3.Autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MapperTest {
    /**
     * 测试DepartmentMapper
     */
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlSession;
    @Test
    public void testCRUD(){

        //1.插入部门
        //departmentMapper.insertSelective(new Department(null,"开发部"));
        //departmentMapper.insertSelective(new Department(null,"测试部"));

        //2.生成员工数据
        //employeeMapper.insertSelective(new Employee(null,"Jerry","M","Jerry@163.com",1));
        //employeeMapper.insertSelective(new Employee(null,"Tom","N","Tom@163.com",1));

        //3.批量插入多个员工:使用可以执行批量操作的sqlSession
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for(int i=0;i<1000;i++){
            String uid = UUID.randomUUID().toString().substring(0, 5)+i;
            mapper.insertSelective(new Employee(null,uid,"M",uid+"@163.com",1));
        }
        System.out.println("批量完成");

        //4.删除员工
        //for(int i =5;i<1000;i++){
        //    employeeMapper.deleteByPrimaryKey(i);
        //}

        //5.修改员工信息
        //employeeMapper.updateByPrimaryKeySelective(new Employee(1000,"测试","M","测试@163.com",2));

        ////6.查询所有员工信息
        //List<Employee> employees = employeeMapper.selectByExampleWithDept(new EmployeeExample());
        //for (Employee e:employees){
        //    System.out.println(e);
        //}
    }
}
