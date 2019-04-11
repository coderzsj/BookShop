package cn.ganin.dao;

import cn.ganin.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

//    查询分类的
    List<Category> selectCategoryChildrenByParentId(Integer parent_id);
}