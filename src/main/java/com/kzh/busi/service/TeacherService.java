package com.kzh.busi.service;

import com.kzh.busi.dao.ClazzDao;
import com.kzh.busi.dao.CourseDao;
import com.kzh.busi.model.Clazz;
import com.kzh.busi.model.Course;
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

import com.kzh.busi.dao.TeacherDao;
import com.kzh.busi.model.Teacher;

@Service
@Transactional
public class TeacherService extends BaseService<Teacher> {
    private static final Logger logger = Logger.getLogger(TeacherService.class);

    @Resource
    private TeacherDao teacherDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Resource
    private CourseDao courseDao;
    @Resource
    private ClazzDao clazzDao;

    @Override
    public GenericRepository getDao() {
        return teacherDao;
    }

    public Page<Teacher> page(Teacher entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Teacher> page = teacherDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveTeacher(Teacher entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        if (CollectionUtil.isNotEmpty(entity.getCourseIds())) {
            Set<Course> courses = courseDao.findByIdIn(entity.getCourseIds());
            entity.setCourses(courses);
        }
        if (CollectionUtil.isNotEmpty(entity.getClazzIds())) {
            Set<Clazz> clazzes = clazzDao.findByIdIn(entity.getClazzIds());
            entity.setClazzes(clazzes);
        }

        Teacher tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = teacherDao.save(entity);
        } else {
            //编辑
            Teacher teacher = teacherDao.findOne(entity.getId());
            teacher.initModifyFields(entity);
            teacher.setCourses(entity.getCourses());
            teacher.setClazzes(entity.getClazzes());
            tDb = teacherDao.save(teacher);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Teacher> os = teacherDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                teacherDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Teacher> os = teacherDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Teacher o : os) {
                o.setDelFlag(true);
            }
            teacherDao.save(os);
        }
    }

    public Teacher get(String id) {
        Teacher o = teacherDao.findOne(id);
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

    public List<Teacher> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return teacherDao.findAll(expressions);
    }
}
