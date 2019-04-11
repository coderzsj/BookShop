package cn.ganin.service;

import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @Author agamgn
 * @Date 2018-08-05
 **/
public interface IShippingService {
    ServerResponses add(Integer userId, Shipping shipping);
    ServerResponses<String> del(Integer userId, Integer shippId);
    ServerResponses update(Integer userId, Shipping shipping);
    ServerResponses<Shipping> select(Integer userId, Integer shippId);
    ServerResponses<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
