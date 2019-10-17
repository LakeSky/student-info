package com.kzh.busi.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.busi.model.Course;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseDao extends GenericRepository<Course, String> {
    Set<Course> findByIdIn(Collection<String> ids);
}
