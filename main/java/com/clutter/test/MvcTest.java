package com.clutter.test;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



/**
 * 使用spring测试模块提供的测试请求功能，测试crud请求的正确性
 * java.lang.NoClassDefFoundError: javax/servlet/SessionCookieConfig
 * 因为Spring4测试的时候需要   servle3.0来支持
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml","classpath:spring/springmvc-config.xml"})
public class MvcTest {
    //传入Springmvc的ioc
    @Autowired
    WebApplicationContext context;
    //虚拟mvc请求，获取处理结果。
    MockMvc mockMvc;
    @Before
    public void initMokcMvc(){
        MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    public void testPage() throws Exception {
        //模拟请求拿到返回值
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/emps")
                .param("pn", "5")).andReturn();

        //请求成功以后，请求域中会有pageInfo；取出pageInfo进行验证
        //MockHttpServletRequest request = result.getRequest();
        //PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
        //System.out.println("当前页码："+pageInfo.getPageNum());
        //System.out.println("总页码："+pageInfo.getPages());
        //System.out.println("总记录数："+pageInfo.getTotal());
        //System.out.println("在页面需要连续显示的页码");
        //int[] navigatepageNums = pageInfo.getNavigatepageNums();
        //for(int i: navigatepageNums){
        //    System.out.println(""+i);
        //}
        //
        ////获取员工数据
        //List<Employee> list = pageInfo.getList();
        //for(Employee e:list){
        //    System.out.println("ID:"+e.getEmpId()+"---->Name:"+e.getEmpName());
        //}
    }
}
