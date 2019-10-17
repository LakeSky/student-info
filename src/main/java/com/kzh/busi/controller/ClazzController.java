package com.kzh.busi.controller;

import com.kzh.busi.model.Clazz;
import com.kzh.busi.model.Course;
import com.kzh.busi.model.Grade;
import com.kzh.busi.service.ClazzService;
import com.kzh.busi.service.GradeService;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.util.DateUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
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

@Controller
@RequestMapping(value = "/busi/clazz", name = "班级管理")
public class ClazzController {
    private static final Logger logger = Logger.getLogger(Clazz.class);

    @Resource
    private ClazzService service;
    @Resource
    private FieldService fieldService;
    @Resource
    private GradeService gradeService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Clazz.class));
        return "/busi/clazz/home";
    }

    @RequestMapping(value = "/page", name = "分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, Clazz entity, WorldPage worldPage) throws Exception {
        worldPage.setProperties("createTime");
        Page<Clazz> page = service.page(entity, worldPage);
        return page;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Clazz entity) throws Exception {
        if (SysUtil.isEmpty(entity.getGrade().getId())) {
            return new Result(false, "请选择年级");
        }
        Result result = service.saveClazz(entity);
        return result;
    }

    @RequestMapping(value = "/del", name = "删除")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public Object info(String id) {
        Clazz clazz = service.get(id);
        return new Result(true, "", clazz);
    }

    @RequestMapping(value = "/add", name = "添加")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Clazz.class));
        model.addAttribute("page", "add");
        List<Grade> grades = gradeService.getAll();
        model.addAttribute("grades", grades);
        return "/busi/clazz/edit";
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Clazz.class));
        Clazz object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "edit");
        List<Grade> grades = gradeService.getAll();
        model.addAttribute("grades", grades);
        return "/busi/clazz/edit";
    }

    @RequestMapping(value = "/view", name = "查看")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Clazz.class));
        Clazz object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "view");
        return "/busi/clazz/view";
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
        Workbook workbook = fieldService.export(params, Clazz.class);
        workbook.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() throws Exception {
        List<Clazz> clazzes = service.getAll();
        return new Result(true, "", clazzes);
    }
}
