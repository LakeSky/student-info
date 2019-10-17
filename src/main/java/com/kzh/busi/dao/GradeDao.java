package com.kzh.busi.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.busi.model.Grade;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeDao extends GenericRepository<Grade, String> {

}
