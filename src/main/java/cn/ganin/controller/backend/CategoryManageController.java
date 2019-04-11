package cn.ganin.controller.backend;

import cn.ganin.common.Const;
import cn.ganin.common.ResponseCode;
import cn.ganin.common.ServerResponses;
import cn.ganin.pojo.User;
import cn.ganin.service.ICategoryService;
import cn.ganin.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author agamgn
 * @Date 2018-08-07
 **/
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;
    /**
     * 添加商铺分类
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "addCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0")int parentId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登录");

        }
//        校验下,是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()){

            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }

    /**
     * 跟新categoryname的
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "setCategoryName.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses setCategoryName(HttpSession session,Integer categoryId,String categoryName){

        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }


    /**
     * 查询子节点id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "getCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponses getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponses.createByErroeMessage("无权限操作");
        }
    }


    /**
     * 获取当前id并且递归查询他的子节点的id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "getDeepCategory.do",method = RequestMethod.GET)
@ResponseBody
public ServerResponses getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
    User user= (User) session.getAttribute(Const.CURRENT_USER);
    if (user==null){
        return ServerResponses.createByErroeCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登录");
    }
    if (iUserService.checkAdminRole(user).isSuccess()){
    return iCategoryService.selectCategoryAndChildrenBuId(categoryId);

    }else{
        return ServerResponses.createByErroeMessage("无权限操作");
    }
}

}
