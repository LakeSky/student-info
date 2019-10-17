package com.kzh.busi.service;

import com.kzh.busi.dao.GradeDao;
import com.kzh.busi.model.Grade;
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

import com.kzh.busi.dao.CourseDao;
import com.kzh.busi.model.Course;

@Service
@Transactional
public class CourseService extends BaseService<Course> {
    private static final Logger logger = Logger.getLogger(CourseService.class);

    @Resource
    private CourseDao courseDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Resource
    private GradeDao gradeDao;

    @Override
    public GenericRepository getDao() {
        return courseDao;
    }

    public Page<Course> page(Course entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Course> page = courseDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveCourse(Course entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Grade grade = gradeDao.findOne(entity.getGrade().getId());
        entity.setGrade(grade);
        Course tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = courseDao.save(entity);
        } else {
            //编辑
            Course course = courseDao.getOne(entity.getId());
            course.initModifyFields(entity);
            course.setGrade(grade);
            tDb = courseDao.save(course);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Course> os = courseDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                courseDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Course> os = courseDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Course o : os) {
                o.setDelFlag(true);
            }
            courseDao.save(os);
        }
    }

    public Course get(String id) {
        Course o = courseDao.findOne(id);
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

    public List<Course> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return courseDao.findAll(expressions);
    }
}
