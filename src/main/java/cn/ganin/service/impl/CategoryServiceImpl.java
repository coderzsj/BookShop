package cn.ganin.service.impl;

import cn.ganin.common.ServerResponses;
import cn.ganin.dao.CategoryMapper;
import cn.ganin.pojo.Category;
import cn.ganin.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @Author agamgn
 * @Date 2018-08-07
 **/
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponses addCategory(String categoryName, Integer parentId){
        if (parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponses.createByErroeMessage("添加商品参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//表示这个分类
        int rowCount=categoryMapper.insert(category);
        if (rowCount>0){
            return ServerResponses.createBySuccess("添加品类成功");
        }
        return ServerResponses.createByErroeMessage("添加品类失败");
    }




    /**
     * 更新categoryname
     * @param categoryId
     * @param categoryName
     * @return
     */
    public ServerResponses updateCategoryName(Integer categoryId,String categoryName){
        if (categoryId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponses.createByErroeMessage("添加商品参数错误");
        }
        Category category=new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount>0){
            return ServerResponses.createBySuccess("更新品类成功");
        }
        return ServerResponses.createByErroeMessage("更新品类失败");
    }

    /**
     * 查询子节点分类
     * @param categoryId
     * @return
     */
    public ServerResponses<List<Category>> getChildrenParallelCategory(Integer categoryId){
            List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
            if (CollectionUtils.isEmpty(categoryList)){
                logger.info("未找到当前分类的子分类");
            }
            return ServerResponses.createBySuccess(categoryList);
    }


    /**
     * 递归查询本节点的id及孩子节点的id
     * 问:Sets，lists是什么
     * @param categoryId
     * @return
     */
    public ServerResponses<List<Integer>> selectCategoryAndChildrenBuId(Integer categoryId){
        Set<Category> categoryset= Sets.newHashSet();
        findChildCategory(categoryset,categoryId);
        List<Integer> categoryIdList= Lists.newArrayList();
        if (categoryId!=null){
            for (Category categoryItem:categoryset){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponses.createBySuccess(categoryIdList);
    }



//    递归算法，算出子节点
   private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categorySet.add(category);
        }
//        查找子节点，队规算法的一个退出条件
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem:categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
   }

}
