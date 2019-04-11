package cn.ganin.dao;

import cn.ganin.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);


//防止横向越权的删除
    int deleteByShipIdUserId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);
//防止横向越权
    int updateByShipping(Shipping record);

    Shipping selectByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);

//    分页
    List<Shipping> selectByUserId(@Param("userId") Integer userId);


}