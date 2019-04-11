package cn.ganin.controller.portal;

import cn.ganin.common.Const;
import cn.ganin.common.ResponseCode;
import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.User;
import cn.ganin.service.ICartService;
import cn.ganin.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author agamgn
 * @Date 2018-08-07
 **/
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 添加购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponses<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }

    /**
     * 更新购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponses<CartVo> update(HttpSession session, Integer count, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    /**
     * 删除购物车
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping("deleteProduct.do")
    @ResponseBody
    public ServerResponses<CartVo> deleteProduct(HttpSession session,String productIds){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }


    /**
     * 查询购物车
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponses<CartVo> list(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.list(user.getId());

    }


    /**
     * 全选
     * @param session
     * @return
     */
    @RequestMapping("selectAll.do")
    @ResponseBody
    public ServerResponses<CartVo> seleteAll(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrSelete(user.getId(),null,Const.Cart.CHECKED);

    }

    /**
     * 全反选
     * @param session
     * @return
     */
    @RequestMapping("unSelectAll.do")
    @ResponseBody
    public ServerResponses<CartVo> unSseleteAll(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrSelete(user.getId(),null,Const.Cart.UN_CHECKED);

    }


    /**
     * 单选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("select_.do")
    @ResponseBody
    public ServerResponses<CartVo> selete(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrSelete(user.getId(),productId,Const.Cart.CHECKED);

    }

    /**
     * 单反选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("unSelect_.do")
    @ResponseBody
    public ServerResponses<CartVo> unSelete(HttpSession session, Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return iCartService.selectOrSelete(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    /**
     * 获取产品数量
     * @param session
     * @return
     */
    @RequestMapping("getProductCount.do")
    @ResponseBody
    public ServerResponses<Integer> getCartProductCount(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());

    }

}
