package com.kzh.busi.service;

import com.kzh.busi.dao.ClazzDao;
import com.kzh.busi.model.Clazz;
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

import com.kzh.busi.dao.StudentDao;
import com.kzh.busi.model.Student;

@Service
@Transactional
public class StudentService extends BaseService<Student> {
    private static final Logger logger = Logger.getLogger(StudentService.class);

    @Resource
    private StudentDao studentDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Resource
    private ClazzDao clazzDao;

    @Override
    public GenericRepository getDao() {
        return studentDao;
    }

    public Page<Student> page(Student entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Student> page = studentDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveStudent(Student entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Student tDb;
        Clazz clazz = clazzDao.findOne(entity.getClazz().getId());
        entity.setClazz(clazz);
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = studentDao.save(entity);
        } else {
            //编辑
            Student student = studentDao.getOne(entity.getId());
            student.initModifyFields(entity);
            student.setClazz(entity.getClazz());
            tDb = studentDao.save(student);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Student> os = studentDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                studentDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Student> os = studentDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Student o : os) {
                o.setDelFlag(true);
            }
            studentDao.save(os);
        }
    }

    public Student get(String id) {
        Student o = studentDao.findOne(id);
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

    public List<Student> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return studentDao.findAll(expressions);
    }
}
