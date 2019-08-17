package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.Category;
import com.jifenke.lepluslive.weixin.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * 数据字典 Created by zhangwen on 2016/11/01.
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

  @Inject
  private CategoryRepository repository;

  /**
   * 获取某一大分类下的所有的可用小分类 16/11/01
   *
   * @param category 分类序号
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Category> findAllByCategory(Integer category) {
    return repository.findByCategoryAndState(category, 1);
  }
}
