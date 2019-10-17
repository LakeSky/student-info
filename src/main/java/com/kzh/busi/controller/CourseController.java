package com.kzh.busi.controller;

import com.kzh.busi.model.Clazz;
import com.kzh.busi.model.Grade;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.model.User;
import com.kzh.sys.enums.UpState;
import com.kzh.sys.util.DateUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.kzh.sys.util.NumberUtil;
import org.apache.log4j.Logger;

import com.kzh.busi.service.*;
import com.kzh.busi.model.Course;

@Controller
@RequestMapping(value = "/busi/course", name = "课程管理")
public class CourseController {
    private static final Logger logger = Logger.getLogger(Course.class);

    @Resource
    private CourseService service;
    @Resource
    private FieldService fieldService;
    @Resource
    private GradeService gradeService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Course.class));
        return "/busi/course/home";
    }

    @RequestMapping(value = "/page", name = "分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, Course entity, WorldPage worldPage) throws Exception {
        worldPage.setProperties("createTime");
        Page<Course> page = service.page(entity, worldPage);
        return page;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Course entity) throws Exception {
        Result result = service.saveCourse(entity);
        return result;
    }

    @RequestMapping(value = "/del", name = "删除")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/add", name = "添加")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Course.class));
        model.addAttribute("page", "add");
        List<Grade> grades = gradeService.getAll();
        model.addAttribute("grades", grades);
        return "/busi/course/edit";
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Course.class));
        Course object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "edit");
        List<Grade> grades = gradeService.getAll();
        model.addAttribute("grades", grades);
        return "/busi/course/edit";
    }

    @RequestMapping(value = "/view", name = "查看")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Course.class));
        Course object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "view");
        return "/busi/course/view";
    }

    @RequestMapping(value = "/export", name = "导出")
    public void exportExcel(HttpServletResponse response, String params, String fileName) throws Exception {
        OutputStream os = null;
        String dateStr = DateUtil.format("yyyyMMddHHmmss", new Date());
        String filename = fileName + dateStr + ".xls";//设置下载时客户端Excel的名称
        filename = new String(filename.getBytes("utf-8"), "iso-8859-1");//处理中文文件名
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        os = response.getOutputStream();
        Workbook workbook = fieldService.export(params, Course.class);
        workbook.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() throws Exception {
        List<Course> courses = service.getAll();
        return new Result(true, "", courses);
    }
}
