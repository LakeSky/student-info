package com.kzh.busi.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.busi.model.Teacher;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface TeacherDao extends GenericRepository<Teacher, String> {
    Set<Teacher> findByIdIn(Collection<String> ids);
}
