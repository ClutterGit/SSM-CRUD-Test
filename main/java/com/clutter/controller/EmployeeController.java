package com.clutter.controller;

import com.clutter.pojo.Employee;
import com.clutter.pojo.Msg;
import com.clutter.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工CRUD的请求
 */
@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;


    /**
     * 单个删除
     */
    //@RequestMapping(value ="/emp/{id}",method = RequestMethod.DELETE)
    //@ResponseBody
    //public Msg deleteEmpById(@PathVariable("id") Integer id){
    //    employeeService.deleteEmp(id);
    //    return Msg.success();
    //}

    /**
     * 单个  批量 二合一
     * 单个：1
     * 批量：1-2-3-4
     * @param ids
     * @return
     */
    @RequestMapping(value ="/emp/{ids}",method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmp(@PathVariable("ids") String ids){
        if(ids.contains("-")){
            List<Integer> del_ids = new ArrayList<>();
            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String id:str_ids){
                del_ids.add(Integer.parseInt(id));
            }
            employeeService.deleteBatch(del_ids);
        }else{
            int id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }

        return Msg.success();
    }





    /**
     * 要能支持直接发送PUT之类的请求，还要封装请求体中的数据
     * 需要在web.xml中配置HttpPutFormContentFilter
     *  作用：将请求体中的数据解析包装成一个map
     *  request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
     * 员工更新
     */
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    @ResponseBody
    public Msg updataEmp(Employee employee){
        employeeService.updataEmp(employee);
        return Msg.success();
    }

    /**
     * 查询员工信息
     */
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }
    /**
     * 校验用户名是否可用
     * @param empName
     * @return
     */
    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkuser(@RequestParam("empName") String empName){
        //先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9]{6,16}$)|(^[\u2E80-\u9FFF{2,5])";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg","用户名必须是6-16位数字和字母的组合或者2-5位中文");
        }
        //数据库用户名重复校验
        Boolean b = employeeService.checkUser(empName);
        if(b){
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg","用户名不可用");
        }
    }
    /**
     * 员工保存
     * 1.支持JSR303校验
     * 2.导入Hibernate-Validator
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saceEmp(@Valid Employee employee, BindingResult result){
        if(result.hasErrors()){
            //校验失败，返回失败，再模态框中显示校验失败的错误信息
            Map<String,Object> map = new HashMap();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for(FieldError fieldError:fieldErrors){
                System.out.println("错误的字段名："+fieldError.getField());
                System.out.println("错误的信息："+fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorField",map);
        }else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }

    }
    /**
     * ResponseBody想要正常工作，需要导入jackson包
     * 查询所有员工信息
     * @param pn
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1")Integer pn){
        PageHelper.startPage(pn,9);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> list = employeeService.getAll();
        //使用pageInfo包装查询后的结果。只需要将pageInfo交给页面
        // 封装了详细的分页信息,包括查询出来的数据，PageInfo(查询出来的数据，传入连续显示的页数)
        PageInfo page = new PageInfo(list,7);
        return Msg.success().add("pageInfo",page);
    }
    /**
     * 查询员工数据（分页查询）
     * @return
     */
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1")Integer pn,
                          Model model){
        //数据冗余 不是分页查询
        //引入PageHelper分页插件
        //使用：在查询之前调用  PageHelper.startPage(页码，每页的大小)
        PageHelper.startPage(pn,9);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> list = employeeService.getAll();
        //使用pageInfo包装查询后的结果。只需要将pageInfo交给页面
        // 封装了详细的分页信息,包括查询出来的数据，PageInfo(查询出来的数据，传入连续显示的页数)
        PageInfo page = new PageInfo(list,7);
        model.addAttribute("pageInfo",page);

        return "list";
    }
}
