package cn.ganin.controller.portal;

import cn.ganin.common.Const;
import cn.ganin.common.ResponseCode;
import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.Shipping;
import cn.ganin.pojo.User;
import cn.ganin.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author agamgn
 * @Date 2018-08-05
 **/
@Controller
@RequestMapping("/shopping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 新增地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponses add(HttpSession session, Shipping shipping){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return iShippingService.add(user.getId(),shipping);
    }


    /**
     * 删除地址
     * @param session
     * @param shipId
     * @return
     */
    @RequestMapping(value = "del.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses del(HttpSession session, Integer shipId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return iShippingService.del(user.getId(),shipId);
    }


    /**
     * 更新地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping(value = "update.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses update(HttpSession session, Shipping shipping){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return iShippingService.update(user.getId(),shipping);
    }


    /**
     * 查询地址
     * @param session
     * @param shipId
     * @return
     */
    @RequestMapping(value = "select.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<Shipping> select(HttpSession session, Integer shipId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(),shipId);
    }

    /**
     * 地址列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses<PageInfo> list(HttpSession session,
            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);

    }







}
