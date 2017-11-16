package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/5/25.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * 获取某一大分类下的所有的可用小分类 16/11/01
   *
   * @param category 分类序号
   * @param state    状态
   */
  List<Category> findByCategoryAndState(Integer category, Integer state);

}
