package com.kzh.busi.service;

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
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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

import com.kzh.busi.dao.GradeDao;
import com.kzh.busi.model.Grade;

@Service
@Transactional
public class GradeService extends BaseService<Grade> {
    private static final Logger logger = Logger.getLogger(GradeService.class);

    @Resource
    private GradeDao gradeDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    @Override
    public GenericRepository getDao() {
        return gradeDao;
    }

    public Page<Grade> page(Grade entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Grade> page = gradeDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveGrade(Grade entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Grade tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = gradeDao.save(entity);
        } else {
            //编辑
            Grade grade = gradeDao.getOne(entity.getId());
            grade.initModifyFields(entity);
            tDb = gradeDao.save(grade);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Grade> os = gradeDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                gradeDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Grade> os = gradeDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Grade o : os) {
                o.setDelFlag(true);
            }
            gradeDao.save(os);
        }
    }

    public Grade get(String id) {
        Grade o = gradeDao.findOne(id);
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

    public List<Grade> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return gradeDao.findAll(expressions);
    }
}
