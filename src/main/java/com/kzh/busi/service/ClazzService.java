package com.kzh.busi.service;

import com.kzh.busi.dao.CourseDao;
import com.kzh.busi.dao.GradeDao;
import com.kzh.busi.dao.TeacherDao;
import com.kzh.busi.model.Course;
import com.kzh.busi.model.Grade;
import com.kzh.busi.model.Teacher;
import com.kzh.sys.app.AppConstant;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.User;
import com.kzh.sys.enums.UpState;
import com.kzh.sys.service.sys.BaseService;
import com.kzh.sys.service.sys.UserService;
import com.kzh.sys.util.PinyinTool;
import com.kzh.sys.util.AES;
import com.kzh.sys.util.SysUtil;
import com.kzh.sys.util.CollectionUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.core.exception.WorldValidateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kzh.busi.dao.ClazzDao;
import com.kzh.busi.model.Clazz;

@Service
@Transactional
public class ClazzService extends BaseService<Clazz> {
    private static final Logger logger = Logger.getLogger(ClazzService.class);

    @Resource
    private ClazzDao clazzDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Resource
    private GradeDao gradeDao;
    @Resource
    private CourseDao courseDao;
    @Resource
    private TeacherDao teacherDao;

    @Override
    public GenericRepository getDao() {
        return clazzDao;
    }

    public Page<Clazz> page(Clazz entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Clazz> page = clazzDao.findByPage(expressions,  worldPage.getPageRequest());
        return page;
    }

    public Result saveClazz(Clazz entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Grade grade = gradeDao.findOne(entity.getGrade().getId());
        entity.setGrade(grade);

        if (CollectionUtil.isNotEmpty(entity.getCourseIds())) {
            Set<Course> courses = courseDao.findByIdIn(entity.getCourseIds());
            entity.setCourses(courses);
        }

        if (CollectionUtil.isNotEmpty(entity.getTeacherIds())) {
            Set<Teacher> teachers = teacherDao.findByIdIn(entity.getTeacherIds());
            entity.setTeachers(teachers);
        }

        Clazz tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = clazzDao.save(entity);
        } else {
            //编辑
            Clazz clazz = clazzDao.getOne(entity.getId());
            clazz.initModifyFields(entity);
            clazz.setGrade(grade);
            clazz.setCourses(entity.getCourses());
            clazz.setTeachers(entity.getTeachers());
            tDb = clazzDao.save(clazz);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Clazz> os = clazzDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                clazzDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Clazz> os = clazzDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Clazz o : os) {
                o.setDelFlag(true);
            }
            clazzDao.save(os);
        }
    }

    public Clazz get(String id) {
        Clazz o = clazzDao.findOne(id);
        return o;
    }

    public List<SimpleExpression> dataExpressions(User user) {
        List<SimpleExpression> expressions = new ArrayList<>();
        switch (userService.getDataLevel(user)) {
            case ALL:
                break;
            default:
                expressions.add(Restrictions.eq("id", "-1", true));
                break;
        }
        return expressions;
    }

    public List<Clazz> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return clazzDao.findAll(expressions);
    }
}
