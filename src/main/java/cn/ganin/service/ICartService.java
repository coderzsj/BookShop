package cn.ganin.service;

import cn.ganin.common.ServerResponses;
import cn.ganin.vo.CartVo;

/**
 * @Author agamgn
 * @Date 2018-08-07
 **/
public interface ICartService {
    ServerResponses<CartVo> add(Integer userId, Integer productId, Integer count);
    ServerResponses<CartVo> update(Integer userId,Integer productId,Integer count);
    ServerResponses<CartVo> deleteProduct(Integer userId,String productIds);
    ServerResponses<CartVo> list(Integer userId);
    ServerResponses<CartVo> selectOrSelete(Integer userId,Integer productId,Integer checked);
    ServerResponses<Integer> getCartProductCount(Integer userId);
}
