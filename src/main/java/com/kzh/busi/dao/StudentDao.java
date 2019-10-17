package com.kzh.busi.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.busi.model.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao extends GenericRepository<Student, String> {

}
