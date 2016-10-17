package com.jifenke.lepluslive.weixin.repository;



import com.jifenke.lepluslive.weixin.domain.entities.Dictionary;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 16/5/25.
 */
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

}
