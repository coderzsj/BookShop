package cn.ganin.dao;

import cn.ganin.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
//    根据用户id和产品id查询
    Cart selectCartByUserIdProductId(@RequestParam("userId") Integer userId,@RequestParam("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int  selectCartProductCheckedStatusByUserId(Integer userId);

//删除
    int deleteByUserIdProductIds(@Param("userId") Integer userId,@RequestParam("productIdList") List<String> productIdList);

//    全选或全不选
    int checkOrUncheckProduct(@Param("userId") Integer userId,@RequestParam("productId") Integer productId,@Param("checked")Integer checked);

//获取产品id
    int selectCartProductCount(@Param("userId") Integer userId);

}