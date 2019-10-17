package com.kzh.busi.controller;

import com.alibaba.fastjson.JSON;
import com.kzh.busi.enums.Sex;
import com.kzh.busi.model.Clazz;
import com.kzh.busi.model.Course;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.model.User;
import com.kzh.sys.enums.UpState;
import com.kzh.sys.util.CollectionUtil;
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
import com.kzh.busi.model.Teacher;

@Controller
@RequestMapping(value = "/busi/teacher", name = "教师管理")
public class TeacherController {
    private static final Logger logger = Logger.getLogger(Teacher.class);

    @Resource
    private TeacherService service;
    @Resource
    private FieldService fieldService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Teacher.class));
        return "/busi/teacher/home";
    }

    @RequestMapping(value = "/page", name = "分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, Teacher entity, WorldPage worldPage) throws Exception {
        worldPage.setProperties("createTime");
        Page<Teacher> page = service.page(entity, worldPage);
        return page;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Teacher entity) throws Exception {
        Result result = service.saveTeacher(entity);
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
        model.addAttribute("fieldMap", FieldService.getFieldMap(Teacher.class));
        model.addAttribute("page", "add");
        model.addAttribute("sexes", Sex.values());
        return "/busi/teacher/edit";
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Teacher.class));
        Teacher object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "edit");
        model.addAttribute("sexes", Sex.values());
        return "/busi/teacher/edit";
    }

    @RequestMapping(value = "/view", name = "查看")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Teacher.class));
        Teacher object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "view");
        return "/busi/teacher/view";
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
        Workbook workbook = fieldService.export(params, Teacher.class);
        workbook.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() throws Exception {
        List<Teacher> objects = service.getAll();
        return new Result(true, "", objects);
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public Object info(String id) {
        Teacher obj = service.get(id);
        
        return new Result(true, "", obj);
    }
}
