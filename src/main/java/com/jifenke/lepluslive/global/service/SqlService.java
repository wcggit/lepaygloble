package com.jifenke.lepluslive.global.service;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwen on 2017/9/20.
 */
@Service
public class SqlService {

    @Autowired
    private EntityManager entityManager;

    /**
     * 获取各种List数据 2017/9/20
     */
    @SuppressWarnings(value = "unchecked")
    public List<Map<String, Object>> listBySql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    /**
     * 获取一行统计数据 2017/9/20
     */
    @SuppressWarnings(value = "unchecked")
    public Map<String, Object> countBySql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map) query.getSingleResult();
    }
}
