package cn.ganin.service.impl;

import cn.ganin.common.ServerResponses;
import cn.ganin.dao.ShippingMapper;
import cn.ganin.pojo.Shipping;
import cn.ganin.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author agamgn
 * @Date 2018-08-05
 **/
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;


    /**
     * 增加地址
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponses add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount=shippingMapper.insert(shipping);
        if (rowCount>0){
            Map result=Maps.newHashMap();
            result.put("shipingId",shipping.getId());
            return ServerResponses.createBySuccess("新建地址成功",result);
        }
        return ServerResponses.createByErroeMessage("新建地址失败");
    }

    /**
     * 删除地址
     * @param userId
     * @param shippId
     * @return
     */
    public ServerResponses<String> del(Integer userId, Integer shippId){

        int rowCount=shippingMapper.deleteByShipIdUserId(userId,shippId);
        if (rowCount>0){
            return ServerResponses.createBySuccess("删除地址成功");
        }
        return ServerResponses.createByErroeMessage("删除地址失败");
    }

    /**
     * 更新地址
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponses update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount=shippingMapper.updateByShipping(shipping);
        if (rowCount>0){
            return ServerResponses.createBySuccess("更新地址成功");
        }
        return ServerResponses.createByErroeMessage("更新地址失败");
    }

    /**
     * 查询地址
     * @param userId
     * @param shippId
     * @return
     */
    public ServerResponses<Shipping> select(Integer userId, Integer shippId){
        Shipping shipping=shippingMapper.selectByShippingIdUserId(userId,shippId);
        if (shipping==null){
            return ServerResponses.createByErroeMessage("无法查询该地址");
        }
        return ServerResponses.createBySuccess("查询地址成功",shipping);
    }

    /**
     * 地址列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponses<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponses.createBySuccess(pageInfo);

    }






}
