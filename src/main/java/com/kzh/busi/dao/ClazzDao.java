package com.kzh.busi.dao;

import com.kzh.busi.model.Clazz;
import com.kzh.sys.core.dao.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface ClazzDao extends GenericRepository<Clazz, String> {
    Set<Clazz> findByIdIn(Collection<String> ids);
}
