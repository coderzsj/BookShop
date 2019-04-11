package cn.ganin.service;

import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.Category;

import java.util.List;

/**
 * @Author agamgn
 * @Date 2018-08-07
 **/
public interface ICategoryService {
    ServerResponses addCategory(String categoryName, Integer parentId);
    ServerResponses updateCategoryName(Integer categoryId,String categoryName);
    ServerResponses<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponses<List<Integer>> selectCategoryAndChildrenBuId(Integer categoryId);
}
